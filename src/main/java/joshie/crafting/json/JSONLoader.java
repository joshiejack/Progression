package joshie.crafting.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.CraftingRemapper;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.lib.CraftingInfo;
import joshie.crafting.lib.Exceptions.ConditionNotFoundException;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class JSONLoader {
    public static Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
    }

    /** Load in the json **/
    public static Options getOptions(Class clazz) {
        try {
            File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + clazz.getSimpleName().toLowerCase() + ".json");
            if (!file.exists()) { //If the json file doesn't exist, let make one with default values
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(gson.toJson(clazz.newInstance()));
                writer.close();
            }

            return gson.fromJson(FileUtils.readFileToString(file), clazz);
        } catch (Exception e) {} //Fail JSON Silently
        return null; //Return it whether it's null or not
    }
    
    private static String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } 
        
        result[lastIndex] = s.substring(j);
        return result;
    }

    @SideOnly(Side.CLIENT)
    public static String[] clientJsonData;
    public static String[] serverJsonData;
    
    public static Criteria getCriteria(Class clazz) {     
        final int MAX_LENGTH = 10000;
        Criteria loader = null;
        try {
            File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + clazz.getSimpleName().toLowerCase() + ".json");
            if (!file.exists()) { //If the json file doesn't exist, let make one with default values
                loader = new Criteria().setDefaults();
                String json = gson.toJson(loader);
                serverJsonData = splitStringEvery(json, MAX_LENGTH);
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(json);
                writer.close();
                return loader;
            } else {
                String json = FileUtils.readFileToString(file);
                serverJsonData = splitStringEvery(json, MAX_LENGTH);
                return gson.fromJson(json, clazz);
            }
        } catch (Exception e) { e.printStackTrace(); } 
        return loader; //Return it whether it's null or not
    }
    
    //Resets the registries, and loads in new json from the string
    public static boolean setCriteriaFromString(String json) {
        try {
            Criteria criteria = gson.fromJson(json, Criteria.class);
            CraftingRemapper.resetRegistries();
            loadJSON(criteria);
            return true;
        } catch (Exception e) { return false; }
    }

    private static CraftingType getCraftingTypeFromName(String name) {
        for (CraftingType type : CraftingType.craftingTypes) {
            if (name.equalsIgnoreCase(type.name)) return type;
        }

        return CraftingType.CRAFTING;
    }
    
    public static void loadServerJSON() {
        loadJSON(JSONLoader.getCriteria(Criteria.class));
    }

    public static void loadJSON(Criteria criterian) {
        /** Step 1: we create add all instances of criteria to the registry **/
        for (DataCriteria criteria : (HashSet<DataCriteria>) criterian.getSet()) {
            CraftingAPI.registry.newCriteria(criteria.name);
        }

        /** Step 2 : Register all the conditions and triggers for this criteria **/
        for (DataCriteria criteria : (HashSet<DataCriteria>) criterian.getSet()) {
            ICriteria theCriteria = CraftingAPI.registry.getCriteriaFromName(criteria.name);
            if (theCriteria == null) {
                throw new ConditionNotFoundException(criteria.name);
            }

            ITrigger[] triggerz = new ITrigger[criteria.triggers.size()];
            for (int j = 0; j < triggerz.length; j++) {
                DataTrigger trigger = criteria.triggers.get(j);
                ITrigger iTrigger = CraftingAPI.registry.newTrigger(theCriteria, trigger.type, trigger.data);
                ICondition[] conditionz = new ICondition[trigger.conditions.size()];
                for (int i = 0; i < conditionz.length; i++) {
                    DataGeneric condition = trigger.conditions.get(i);
                    conditionz[i] = CraftingAPI.registry.newCondition(theCriteria, condition.type, condition.data);
                }

                iTrigger.setConditions(conditionz);
                triggerz[j] = iTrigger;
            }

            //Add the Rewards
            IReward[] rewardz = new IReward[criteria.rewards.size()];
            for (int k = 0; k < criteria.rewards.size(); k++) {
                DataGeneric reward = criteria.rewards.get(k);
                rewardz[k] = CraftingAPI.registry.newReward(theCriteria, reward.type, reward.data);
            }

            theCriteria.addTriggers(triggerz).addRewards(rewardz);
        }

        /** Step 3, nAdd the extra data **/
        for (DataCriteria criteria : (HashSet<DataCriteria>) criterian.getSet()) {
            ICriteria theCriteria = CraftingAPI.registry.getCriteriaFromName(criteria.name);
            if (theCriteria == null) {
                throw new ConditionNotFoundException(criteria.name);
            }

            ICriteria[] thePrereqs = new ICriteria[criteria.prereqs.length];
            ICriteria[] theConflicts = new ICriteria[criteria.conflicts.length];
            for (int i = 0; i < thePrereqs.length; i++)
                thePrereqs[i] = CraftingAPI.registry.getCriteriaFromName(criteria.prereqs[i]);
            for (int i = 0; i < theConflicts.length; i++)
                theConflicts[i] = CraftingAPI.registry.getCriteriaFromName(criteria.conflicts[i]);
            int repeatable = criteria.repeatable;
            int x = criteria.x;
            int y = criteria.y;
            if (repeatable <= 1) {
                repeatable = 1;
            }

            theCriteria.addRequirements(thePrereqs).addConflicts(theConflicts).setRepeatAmount(repeatable).getTreeEditor().setCoordinates(x, y);
        }
        
        criterian = null; //Clear out this object
    }

    public static void saveJSON(Object toSave) {
        File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + toSave.getClass().getSimpleName().toLowerCase() + ".json");
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(gson.toJson(toSave));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveCriteria() {
        HashSet<String> names = new HashSet();
        Collection<ICriteria> criteria = CraftAPIRegistry.criteria.values();
        Criteria forJSONCriteria = new Criteria();
        for (ICriteria c : criteria) {
            if (!names.add(c.getUniqueName())) continue;
            
            DataCriteria data = new DataCriteria();
            data.x = c.getTreeEditor().getX();
            data.y = c.getTreeEditor().getY();
            data.repeatable = c.getRepeatAmount();
            data.name = c.getUniqueName();
            List<ITrigger> triggers = c.getTriggers();
            List<IReward> rewards = c.getRewards();
            List<ICriteria> prereqs = c.getRequirements();
            List<ICriteria> conflicts = c.getConflicts();

            ArrayList<DataTrigger> theTriggers = new ArrayList();
            ArrayList<DataGeneric> theRewards = new ArrayList();
            for (ITrigger trigger : c.getTriggers()) {
                ArrayList<DataGeneric> theConditions = new ArrayList();
                for (ICondition condition : trigger.getConditions()) {
                    JsonObject object = new JsonObject();
                    if (condition.isInverted()) {
                        object.addProperty("inverted", true);
                    }

                    condition.serialize(object);
                    DataGeneric dCondition = new DataGeneric(condition.getTypeName(), object);
                    theConditions.add(dCondition);
                }

                JsonObject triggerData = new JsonObject();
                trigger.serialize(triggerData);
                DataTrigger dTrigger = new DataTrigger(trigger.getTypeName(), triggerData, theConditions);
                theTriggers.add(dTrigger);
            }

            for (IReward reward : c.getRewards()) {
                JsonObject rewardData = new JsonObject();
                reward.serialize(rewardData);
                DataGeneric dReward = new DataGeneric(reward.getTypeName(), rewardData);
                theRewards.add(dReward);
            }

            String[] thePrereqs = new String[prereqs.size()];
            String[] theConflicts = new String[conflicts.size()];
            for (int i = 0; i < thePrereqs.length; i++)
                thePrereqs[i] = prereqs.get(i).getUniqueName();
            for (int i = 0; i < theConflicts.length; i++)
                theConflicts[i] = conflicts.get(i).getUniqueName();
            data.triggers = theTriggers;
            data.rewards = theRewards;
            data.prereqs = thePrereqs;
            data.conflicts = theConflicts;
            forJSONCriteria.data.add(data);
        }

        saveJSON(forJSONCriteria);
    }

    /** Set up the default conditions **/
    public static class Criteria {
        private Set<DataCriteria> data = new HashSet();

        public Set getSet() {
            return data;
        }

        public Criteria setDefaults() {
            ArrayList<DataGeneric> rewardsNewCondition = new ArrayList();
            ArrayList<DataTrigger> triggersNewCondition = new ArrayList();
            ArrayList<DataGeneric> rewardsNamedCondition = new ArrayList();
            ArrayList<DataTrigger> triggersNamedCondition = new ArrayList();
            ArrayList<DataGeneric> rewardsGoldenPig = new ArrayList();
            ArrayList<DataTrigger> triggersGoldenPig = new ArrayList();
            ArrayList<DataGeneric> rewardsLapis = new ArrayList();
            ArrayList<DataTrigger> triggersLapis = new ArrayList();
            ArrayList<DataGeneric> conditions = new ArrayList();
            ArrayList<DataGeneric> nightCondition = new ArrayList();

            //Conditions
            JsonObject night = new JsonObject();
            nightCondition.add(new DataGeneric("daytime", night));

            //Triggers
            JsonObject object = new JsonObject();
            ItemStack stack = new ItemStack(Blocks.bookshelf);
            String serial = StackHelper.getStringFromStack(stack);
            object.addProperty("item", serial);
            object.addProperty("amount", 5);
            triggersNewCondition.add(new DataTrigger("breakBlock", object, conditions));
            JsonObject iron = new JsonObject();
            iron.addProperty("researchName", "Iron Heights");
            triggersNamedCondition.add(new DataTrigger("research", iron, conditions));
            JsonObject pig = new JsonObject();
            pig.addProperty("entity", "Pig");
            triggersGoldenPig.add(new DataTrigger("kill", pig, nightCondition));
            JsonObject crafting = new JsonObject();
            crafting.addProperty("item", "minecraft:diamond_block");
            triggersLapis.add(new DataTrigger("crafting", crafting, conditions));

            //Rewards
            JsonObject speed = new JsonObject();
            speed.addProperty("speed", 0.1F);
            rewardsNewCondition.add(new DataGeneric("speed", speed));
            JsonObject speed2 = new JsonObject();
            speed2.addProperty("speed", 0.5F);
            rewardsNamedCondition.add(new DataGeneric("speed", speed2));

            JsonObject iron2 = new JsonObject();
            iron2.addProperty("item", "minecraft:iron_block");
            rewardsNamedCondition.add(new DataGeneric("crafting", iron2));
            JsonObject gold = new JsonObject();
            gold.addProperty("item", "minecraft:gold_block");
            rewardsGoldenPig.add(new DataGeneric("crafting", gold));
            JsonObject lapis = new JsonObject();
            lapis.addProperty("item", "minecraft:lapis_block");
            rewardsNewCondition.add(new DataGeneric("crafting", lapis));
            JsonObject lapis2 = new JsonObject();
            lapis2.addProperty("item", "minecraft:lapis_block");
            rewardsLapis.add(new DataGeneric("crafting", lapis2));

            //Criteria
            data.add(new DataCriteria("NEW CONDITION", triggersNewCondition, rewardsNewCondition, new String[] { "GoldenPig" }, new String[] {}, 55, 0));
            data.add(new DataCriteria("NamedCondition", triggersNamedCondition, rewardsNamedCondition, new String[] {}, new String[] {}, 0, 55));
            data.add(new DataCriteria("GoldenPig", triggersGoldenPig, rewardsGoldenPig, new String[] {}, new String[] {}, 0, 0));
            data.add(new DataCriteria("EnableLapis", triggersLapis, rewardsLapis, new String[] {}, new String[] {}, 0, 110));
            return this;
        }
    }
}
