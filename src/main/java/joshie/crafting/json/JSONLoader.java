package joshie.crafting.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import joshie.crafting.Condition;
import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.CraftingMod;
import joshie.crafting.Criteria;
import joshie.crafting.Reward;
import joshie.crafting.Tab;
import joshie.crafting.Trigger;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.lib.CraftingInfo;
import joshie.crafting.lib.Exceptions.ConditionNotFoundException;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class JSONLoader {
    public static Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
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
    public static String[] clientTabJsonData;
    public static String[] serverTabJsonData;
    
    public static DefaultSettings getTabs() {     
        final int MAX_LENGTH = 10000;
        DefaultSettings loader = null;
        try {
            File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + "criteria.json");
            if (!file.exists()) {
                loader = new DefaultSettings().setDefaults();
                String json = gson.toJson(loader);
                serverTabJsonData = splitStringEvery(json, MAX_LENGTH);
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(json);
                writer.close();
                return loader;
            } else {
                String json = FileUtils.readFileToString(file);
                serverTabJsonData = splitStringEvery(json, MAX_LENGTH);
                return gson.fromJson(json, DefaultSettings.class);
            }
        } catch (Exception e) { e.printStackTrace(); } 
        return loader; //Return it whether it's null or not
    }
    
    public static boolean setTabsAndCriteriaFromString(String json) {
        try {
            DefaultSettings tab = gson.fromJson(json, DefaultSettings.class);
            loadJSON(tab);
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
        loadJSON(JSONLoader.getTabs()); //Repackage the tabs
    }
    
    private static void loadJSON(DefaultSettings tab) {
    	boolean isClient = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        for (DataTab data: tab.tabs) {
            ItemStack stack = null;
            if (data.stack != null) {
                stack = StackHelper.getStackFromString(data.stack);
            }
            
            if (stack == null) {
                stack = new ItemStack(Items.book);
            }

            Tab iTab = CraftAPIRegistry.newTab(data.uniqueName);
            iTab.setDisplayName(data.displayName).setVisibility(data.isVisible).setStack(stack).setSortIndex(data.sortIndex);
            
            /** Step 1: we create add all instances of criteria to the registry **/
            for (DataCriteria criteria : data.criteria) {
                CraftAPIRegistry.newCriteria(iTab, criteria.uniqueName);
            }

            /** Step 2 : Register all the conditions and triggers for this criteria **/
            for (DataCriteria criteria : data.criteria) {
                Criteria theCriteria = CraftAPIRegistry.getCriteriaFromName(criteria.uniqueName);
                if (theCriteria == null) {
                    throw new ConditionNotFoundException(criteria.uniqueName);
                }

                Trigger[] triggerz = new Trigger[criteria.triggers.size()];
                for (int j = 0; j < triggerz.length; j++) {
                    DataTrigger trigger = criteria.triggers.get(j);
                    Trigger iTrigger = CraftAPIRegistry.newTrigger(theCriteria, trigger.type, trigger.data);
                    Condition[] conditionz = new Condition[trigger.conditions.size()];
                    for (int i = 0; i < conditionz.length; i++) {
                        DataGeneric condition = trigger.conditions.get(i);
                        conditionz[i] = CraftAPIRegistry.newCondition(iTrigger, condition.type, condition.data);
                    }

                    iTrigger.setConditions(conditionz);
                    triggerz[j] = iTrigger;
                }

                //Add the Rewards
                Reward[] rewardz = new Reward[criteria.rewards.size()];
                for (int k = 0; k < criteria.rewards.size(); k++) {
                    DataGeneric reward = criteria.rewards.get(k);
                    rewardz[k] = CraftAPIRegistry.newReward(theCriteria, reward.type, reward.data);
                }

                theCriteria.addTriggers(triggerz);
                theCriteria.addRewards(rewardz);
            }

            /** Step 3, nAdd the extra data **/
            for (DataCriteria criteria : data.criteria) {
                Criteria theCriteria = CraftAPIRegistry.getCriteriaFromName(criteria.uniqueName);
                if (theCriteria == null) {
                    CraftingMod.logger.log(org.apache.logging.log4j.Level.WARN, "Criteria was not found, do not report this.");
                    throw new ConditionNotFoundException(criteria.uniqueName);
                }
                
                Criteria[] thePrereqs = new Criteria[criteria.prereqs.length];
                Criteria[] theConflicts = new Criteria[criteria.conflicts.length];
                for (int i = 0; i < thePrereqs.length; i++)
                    thePrereqs[i] = CraftAPIRegistry.getCriteriaFromName(criteria.prereqs[i]);
                for (int i = 0; i < theConflicts.length; i++)
                    theConflicts[i] = CraftAPIRegistry.getCriteriaFromName(criteria.conflicts[i]);
                boolean isVisible = criteria.isVisible;
                int repeatable = criteria.repeatable;
                int x = criteria.x;
                int y = criteria.y;
                
                ItemStack icon = null;
                if (criteria.displayStack != null) {
                    icon = StackHelper.getStackFromString(criteria.displayStack);
                }
                
                if (icon == null) {
                    icon = new ItemStack(Blocks.stone);
                }
                
                String display = criteria.displayName;
                if (repeatable <= 1) {
                    repeatable = 1;
                }
                
                theCriteria.init(thePrereqs, theConflicts, display, isVisible, repeatable, icon);

                if (isClient) {
                	theCriteria.treeEditor.setCoordinates(x, y);
                }
            }
        }

        
        tab = null; //Clear out this object
    }

    public static void saveJSON(Object toSave, String name) {
        File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + name + ".json");
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(gson.toJson(toSave));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        HashSet<String> tabNames = new HashSet();
        Collection<Tab> allTabs = CraftAPIRegistry.tabs.values();
        HashSet<String> names = new HashSet();
        DefaultSettings forJSONTabs = new DefaultSettings();
        for (Tab tab: allTabs) {
            ArrayList<DataCriteria> list = new ArrayList();
            if (!tabNames.add(tab.getUniqueName())) continue;
            DataTab tabData = new DataTab();
            tabData.uniqueName = tab.getUniqueName();
            tabData.displayName = tab.getDisplayName();
            tabData.sortIndex = tab.getSortIndex();
            tabData.isVisible = tab.isVisible();
            tabData.stack = StackHelper.getStringFromStack(tab.getStack());
            for (Criteria c: tab.getCriteria()) {
                if (!names.add(c.uniqueName)) continue;
                if (c.treeEditor == null) continue;
                DataCriteria data = new DataCriteria();
                data.x = c.treeEditor.getX();
                data.y = c.treeEditor.getY();
                data.isVisible = c.isVisible;
                data.repeatable = c.isRepeatable;
                data.displayName = c.displayName;
                data.uniqueName = c.uniqueName;
                data.displayStack = StackHelper.getStringFromStack(c.stack);
                List<Trigger> triggers = c.triggers;
                List<Reward> rewards = c.rewards;
                List<Criteria> prereqs = c.prereqs;
                List<Criteria> conflicts = c.conflicts;

                ArrayList<DataTrigger> theTriggers = new ArrayList();
                ArrayList<DataGeneric> theRewards = new ArrayList();
                for (Trigger trigger : c.triggers) {
                    ArrayList<DataGeneric> theConditions = new ArrayList();
                    for (Condition condition : trigger.getConditions()) {
                        JsonObject object = new JsonObject();
                        if (condition.isInverted()) {
                            object.addProperty("inverted", true);
                        }

                        condition.getType().writeToJSON(object);
                        DataGeneric dCondition = new DataGeneric(condition.getType().getUnlocalisedName(), object);
                        theConditions.add(dCondition);
                    }

                    JsonObject triggerData = new JsonObject();
                    trigger.getType().writeToJSON(triggerData);
                    DataTrigger dTrigger = new DataTrigger(trigger.getType().getUnlocalisedName(), triggerData, theConditions);
                    theTriggers.add(dTrigger);
                }

                for (Reward reward : c.rewards) {
                    JsonObject rewardData = new JsonObject();
                    reward.getType().writeToJSON(rewardData);
                    DataGeneric dReward = new DataGeneric(reward.getType().getUnlocalisedName(), rewardData);
                    theRewards.add(dReward);
                }

                String[] thePrereqs = new String[prereqs.size()];
                String[] theConflicts = new String[conflicts.size()];
                for (int i = 0; i < thePrereqs.length; i++)
                    thePrereqs[i] = prereqs.get(i).uniqueName;
                for (int i = 0; i < theConflicts.length; i++)
                    theConflicts[i] = conflicts.get(i).uniqueName;
                data.triggers = theTriggers;
                data.rewards = theRewards;
                data.prereqs = thePrereqs;
                data.conflicts = theConflicts;
                list.add(data);
            }
            
            tabData.criteria = list;
            forJSONTabs.tabs.add(tabData);
        }
        
        saveJSON(forJSONTabs, "criteria");
    }
}
