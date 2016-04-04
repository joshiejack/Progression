package joshie.progression.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import joshie.progression.Progression;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.IInit;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.EventsManager;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.StackHelper;
import joshie.progression.lib.CriteriaNotFoundException;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

public class JSONLoader {
    public static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
    }

    private static String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) (Math.max(1, Math.ceil(((s.length() / (double) interval)))));
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
    public static int serverHashcode;
    public static String serverName;

    public final static int MAX_LENGTH = 10000;

    public static String getClientTabJsonData() {
        DefaultSettings loader = null;
        try {
            File file = new File("config" + File.separator + ProgressionInfo.MODPATH + File.separator + serverName + File.separator + "criteria.json");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            if (!file.exists()) {
                return "";
            } else {
                String json = FileUtils.readFileToString(file);
                clientTabJsonData = splitStringEvery(json, MAX_LENGTH);
                return json;
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static DefaultSettings getTabs() {
        DefaultSettings loader = null;
        try {
            File fileNew = new File("config" + File.separator + ProgressionInfo.MODPATH + File.separator + RemappingHandler.getHostName() + File.separator + "criteria.json");
            File fileOld = new File("config" + File.separator + ProgressionInfo.MODPATH + File.separator + "criteria.json"); //Attempt to copy from the ssp or smp folders
            boolean sspToSmpConversion = false;
            if (!RemappingHandler.getHostName().equals("ssp")) { //Only copy from the ssp folder if we're a live server
                if (!fileOld.exists()) {
                    fileOld = new File("config" + File.separator + ProgressionInfo.MODPATH + File.separator + "ssp" + File.separator + "criteria.json");
                    sspToSmpConversion = true;
                }
            }

            if (fileOld.exists()) { //If we still have the old file
                if (!fileNew.exists()) {
                    FileUtils.copyFile(fileOld, fileNew); //Copy it to it's new directory
                }

                fileOld.delete(); //And then delete the old one
                if (sspToSmpConversion) { //Delete the folder if we're converting from ssp to smp
                    fileOld.getParentFile().delete();
                }
            }

            //Make the directory if it doesn't exist yet
            if (!fileNew.getParentFile().exists()) {
                fileNew.getParentFile().mkdir();
            }

            if (!fileNew.exists()) {
                loader = new DefaultSettings().setDefaults();
                String json = gson.toJson(loader);
                serverHashcode = json.hashCode();
                serverTabJsonData = splitStringEvery(json, MAX_LENGTH);
                if (Options.debugMode) Progression.logger.log(Level.INFO, "Writing to the file is being done at getTabs(");
                Writer writer = new OutputStreamWriter(new FileOutputStream(fileNew), "UTF-8");
                writer.write(json);
                writer.close();
                return loader;
            } else {
                String json = FileUtils.readFileToString(fileNew);
                serverHashcode = json.hashCode();
                serverTabJsonData = splitStringEvery(json, MAX_LENGTH);
                return gson.fromJson(json, DefaultSettings.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return loader; //Return it whether it's null or not
    }

    //Client side only
    public static boolean setTabsAndCriteriaFromString(String json, boolean create) {
        try {
            DefaultSettings tab = gson.fromJson(json, DefaultSettings.class);
            loadJSON(true, tab);

            if (create) {
                if (Options.debugMode) Progression.logger.log(Level.INFO, "Writing to the file is being done at setTabsAndCriteriaFromString");
                //Attempt to write
                File file = new File("config" + File.separator + ProgressionInfo.MODPATH + File.separator + serverName + File.separator + "criteria.json");
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(json);
                writer.close();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void loadJSON(boolean isClientside, DefaultSettings settings) {
        if (Options.debugMode) {
            Progression.logger.log(Level.INFO, "Reloaded JSON at " + System.currentTimeMillis() + " " + isClientside);
        }

        Options.settings = settings;
        for (DataTab data : settings.tabs) {
            ItemStack stack = null;
            if (data.stack != null) {
                stack = StackHelper.getStackFromString(data.stack);
            }

            if (stack == null) {
                stack = new ItemStack(Items.book);
            }

            ITab iTab = APIHandler.newTab(data.uuid);
            iTab.setDisplayName(data.displayName).setVisibility(data.isVisible).setStack(stack).setSortIndex(data.sortIndex);

            /** Step 1: we create add all instances of criteria to the registry **/
            for (DataCriteria criteria : data.criteria) {
                APIHandler.newCriteria(iTab, criteria.uuid, isClientside);
            }

            /** Step 2 : Register all the conditions and triggers for this criteria **/
            for (DataCriteria criteria : data.criteria) {
                ICriteria theCriteria = APIHandler.getCriteriaFromName(criteria.uuid);
                if (theCriteria == null) {
                    throw new CriteriaNotFoundException(criteria.uuid);
                }

                if (criteria.triggers != null) {
                    for (DataTrigger trigger: criteria.triggers) {
                        ITriggerProvider iTrigger = APIHandler.newTrigger(theCriteria, trigger.uuid, trigger.type, trigger.data);
                        if (trigger.conditions != null && iTrigger != null) {
                            for (DataGeneric generic : trigger.conditions) {
                                APIHandler.newCondition(iTrigger, generic.uuid, generic.type, generic.data);
                            }
                        }
                    }
                }

                //Add the Rewards
                if (criteria.rewards != null) {
                    for (DataGeneric reward : criteria.rewards) {
                        APIHandler.newReward(theCriteria, reward.uuid, reward.type, reward.data);
                    }
                }
            }

            /** Step 3, nAdd the extra data **/
            for (DataCriteria criteria : data.criteria) {
                ICriteria theCriteria = APIHandler.getCriteriaFromName(criteria.uuid);
                if (theCriteria == null) {
                    Progression.logger.log(org.apache.logging.log4j.Level.WARN, "Criteria was not found, do not report this.");
                    throw new CriteriaNotFoundException(criteria.uuid);
                }

                ICriteria[] thePrereqs = new ICriteria[0];
                if (criteria.prereqs != null) {
                    thePrereqs = new ICriteria[criteria.prereqs.length];
                    for (int i = 0; i < thePrereqs.length; i++)
                        thePrereqs[i] = APIHandler.getCriteriaFromName(criteria.prereqs[i]);
                }

                ICriteria[] theConflicts = new ICriteria[0];
                if (criteria.conflicts != null) {
                    theConflicts = new ICriteria[criteria.conflicts.length];
                    for (int i = 0; i < theConflicts.length; i++)
                        theConflicts[i] = APIHandler.getCriteriaFromName(criteria.conflicts[i]);
                }

                boolean allRequired = criteria.allTasks;
                int tasksRequired = criteria.tasksRequired;
                boolean allRewards = criteria.allRewards;
                int rewardsGiven = criteria.rewardsGiven;
                boolean isVisible = criteria.isVisible;
                boolean achievement = criteria.displayAchievement;
                boolean infinite = criteria.infinite;
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

                theCriteria.init(thePrereqs, theConflicts, display, isVisible, achievement, repeatable, icon, allRequired, tasksRequired, infinite, allRewards, rewardsGiven, x, y);
            }
        }
        
        //Now that everything has been loaded in, we should go and init all the data
        for (ITab tab : APIHandler.getTabs().values()) {
            for (ICriteria criteria: tab.getCriteria()) {
                for (ITriggerProvider provider: criteria.getTriggers()) {
                    ITrigger trigger = provider.getProvided();
                    if (trigger instanceof IInit) ((IInit)trigger).init();
                    if (trigger instanceof IHasFilters) {
                        for (IFilterProvider filter: ((IHasFilters)trigger).getAllFilters()) {
                            if (filter.getProvided() instanceof IInit) ((IInit)filter.getProvided()).init();
                        }
                    }
                    
                    EventsManager.onAdded(trigger);
                    
                    for (IConditionProvider conditionProvider: provider.getConditions()) {
                        ICondition condition = conditionProvider.getProvided();
                        if (condition instanceof IInit) ((IInit)condition).init();
                        if (condition instanceof IHasFilters) {
                            for (IFilterProvider filter: ((IHasFilters)condition).getAllFilters()) {
                                if (filter.getProvided() instanceof IInit) ((IInit)filter.getProvided()).init();
                            }
                        }
                    }
                 }
                
                for (IRewardProvider provider: criteria.getRewards()) {
                    if (provider.getProvided() instanceof IInit) ((IInit)provider.getProvided()).init();
                    if (provider.getProvided() instanceof IHasFilters) {
                        for (IFilterProvider filter: ((IHasFilters)provider.getProvided()).getAllFilters()) {
                            if (filter.getProvided() instanceof IInit) ((IInit)filter.getProvided()).init();
                        }
                    }
                    
                    EventsManager.onAdded(provider.getProvided());
                }
            }
        }
    }

    public static void saveJSON(DefaultSettings toSave) {
        File file = new File("config" + File.separator + ProgressionInfo.MODPATH + File.separator + serverName + File.separator + "criteria.json");
        try {
            if (Options.debugMode) Progression.logger.log(Level.INFO, "Writing to the file is being done at saveJSON");
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(gson.toJson(toSave));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Options.debugMode) Progression.logger.log(Level.INFO, "Saved JSON at " + System.currentTimeMillis());
    }

    public static void saveData() {
        if (Options.debugMode) Progression.logger.log(Level.INFO, "Begin logging");
        HashSet<UUID> tabNames = new HashSet();
        Collection<ITab> allTabs = APIHandler.getTabs().values();
        HashSet<UUID> names = new HashSet();
        DefaultSettings forJSONTabs = new DefaultSettings();
        for (ITab tab : allTabs) {
            ArrayList<DataCriteria> list = new ArrayList();
            if (!tabNames.add(tab.getUniqueID())) continue;
            DataTab tabData = new DataTab();
            tabData.uuid = tab.getUniqueID();
            tabData.displayName = tab.getDisplayName();
            tabData.sortIndex = tab.getSortIndex();
            tabData.isVisible = tab.isVisible();
            tabData.stack = StackHelper.getStringFromStack(tab.getStack());
            for (ICriteria c : tab.getCriteria()) {
                if (!names.add(c.getUniqueID())) continue;
                if (c.getIcon() == null) continue;
                DataCriteria data = new DataCriteria();          
                data.x = c.getX();
                data.y = c.getY();
                data.isVisible = c.isVisible();
                data.displayAchievement = c.displayAchievement();
                data.repeatable = c.getRepeatAmount();
                data.infinite = c.canRepeatInfinite();
                data.displayName = c.getLocalisedName();
                data.tasksRequired = c.getTasksRequired();
                data.allTasks = c.getIfRequiresAllTasks();
                data.rewardsGiven = c.getAmountOfRewards();
                data.allRewards = c.givesAllRewards();
                if (Options.debugMode) Progression.logger.log(Level.INFO, "Saved the display name " + c.getLocalisedName());
                data.uuid = c.getUniqueID();
                data.displayStack = StackHelper.getStringFromStack(c.getIcon());
                List<ITriggerProvider> triggers = c.getTriggers();
                List<IRewardProvider> rewards = c.getRewards();
                List<ICriteria> prereqs = c.getPreReqs();
                List<ICriteria> conflicts = c.getConflicts();

                ArrayList<DataTrigger> theTriggers = new ArrayList();
                ArrayList<DataGeneric> theRewards = new ArrayList();
                if (triggers.size() > 0) {
                    for (ITriggerProvider provider : triggers) {
                        ArrayList<DataGeneric> theConditions = null;
                        if (provider.getConditions().size() > 0) {
                            theConditions = new ArrayList();
                            for (IConditionProvider conditionProvider : provider.getConditions()) {
                                JsonObject conditionData = new JsonObject();
                                conditionProvider.writeToJSON(conditionData);
                                JSONHelper.writeJSON(conditionData, conditionProvider.getProvided());
                                DataGeneric dCondition = new DataGeneric(conditionProvider.getUniqueID(), conditionProvider.getUnlocalisedName(), conditionData);
                                theConditions.add(dCondition);
                            }
                        }

                        JsonObject triggerData = new JsonObject();
                        provider.writeToJSON(triggerData);
                        JSONHelper.writeJSON(triggerData, provider.getProvided());
                        DataTrigger dTrigger = new DataTrigger(provider.getUniqueID(), provider.getUnlocalisedName(), triggerData, theConditions);
                        theTriggers.add(dTrigger);
                    }
                }

                if (rewards.size() > 0) {
                    for (IRewardProvider provider : rewards) {
                        JsonObject rewardData = new JsonObject();
                        provider.writeToJSON(rewardData);
                        JSONHelper.writeJSON(rewardData, provider.getProvided());
                        DataGeneric dReward = new DataGeneric(provider.getUniqueID(), provider.getUnlocalisedName(), rewardData);
                        theRewards.add(dReward);
                    }
                }

                UUID[] thePrereqs = new UUID[prereqs.size()];
                UUID[] theConflicts = new UUID[conflicts.size()];
                for (int i = 0; i < thePrereqs.length; i++)
                    thePrereqs[i] = prereqs.get(i).getUniqueID();
                for (int i = 0; i < theConflicts.length; i++)
                    theConflicts[i] = conflicts.get(i).getUniqueID();
                if (theTriggers.size() > 0) data.triggers = theTriggers;
                if (theRewards.size() > 0) data.rewards = theRewards;
                if (thePrereqs.length > 0) data.prereqs = thePrereqs;
                if (theConflicts.length > 0) data.conflicts = theConflicts;
                list.add(data);
            }

            tabData.criteria = list;
            forJSONTabs.tabs.add(tabData);
        }

        saveJSON(forJSONTabs);
    }
}
