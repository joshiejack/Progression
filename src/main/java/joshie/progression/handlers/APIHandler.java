package joshie.progression.handlers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import joshie.progression.api.IConditionType;
import joshie.progression.api.IProgressionAPI;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ITriggerType;
import joshie.progression.crafting.ActionType;
import joshie.progression.criteria.Condition;
import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Reward;
import joshie.progression.criteria.Tab;
import joshie.progression.criteria.Trigger;
import joshie.progression.criteria.rewards.RewardBaseAction;
import joshie.progression.criteria.rewards.RewardBreakBlock;
import joshie.progression.criteria.rewards.RewardCrafting;
import joshie.progression.criteria.rewards.RewardFurnace;
import joshie.progression.criteria.rewards.RewardHarvestDrop;
import joshie.progression.criteria.rewards.RewardLivingDrop;
import joshie.progression.criteria.triggers.data.DataBoolean;
import joshie.progression.criteria.triggers.data.DataCount;
import joshie.progression.criteria.triggers.data.DataCrafting;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import com.google.gson.JsonObject;

public class APIHandler implements IProgressionAPI {
    //This is the registry for trigger type and reward type creation
    public static final HashMap<String, ITriggerType> triggerTypes = new HashMap();
    public static final HashMap<String, IRewardType> rewardTypes = new HashMap();
    public static final HashMap<String, IConditionType> conditionTypes = new HashMap();

    //These four maps are registries for fetching the various types
    public static HashMap<String, Tab> tabs;
    public static HashMap<String, Criteria> criteria;

    @Override
    //Fired Server Side only
    public boolean fireTrigger(UUID uuid, String string, Object... data) {
        return PlayerTracker.getServerPlayer(uuid).getMappings().fireAllTriggers(string, data);
    }

    @Override
    //Fired Server Side only
    public boolean fireTrigger(EntityPlayer player, String string, Object... data) {
        if (!player.worldObj.isRemote) {
            return fireTrigger(PlayerHelper.getUUIDForPlayer(player), string, data);
        } else return false;
    }

    @Override
    public IConditionType registerConditionType(IConditionType type) {
        conditionTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public ITriggerType registerTriggerType(ITriggerType type) {
        triggerTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public IRewardType registerRewardType(IRewardType type) {
        rewardTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    public static Criteria newCriteria(Tab tab, String name) {
        Criteria theCriteria = new Criteria(tab, name);
        tab.addCriteria(theCriteria);
        criteria.put(name, theCriteria);
        return theCriteria;
    }

    public static Tab newTab(String name) {
        Tab iTab = new Tab().setUniqueName(name);
        tabs.put(name, iTab);
        return iTab;
    }

    public static Condition newCondition(Trigger trigger, String type, JsonObject data) {
        IConditionType oldConditionType = conditionTypes.get(type);
        if (oldConditionType == null) return null;
        IConditionType newConditionType = oldConditionType;

        boolean inverted = data.get("inverted") != null ? data.get("inverted").getAsBoolean() : false;

        try {
            newConditionType = oldConditionType.getClass().newInstance();
        } catch (Exception e) {}

        Condition condition = new Condition(trigger.getCriteria(), trigger, newConditionType, inverted);
        condition.getType().readFromJSON(data);
        trigger.addCondition(condition);
        return condition;
    }

    public static Trigger newTrigger(Criteria criteria, String type, JsonObject data) {
        ITriggerType oldTriggerType = triggerTypes.get(type);
        if (oldTriggerType == null) return null;

        ITriggerType newTriggerType = oldTriggerType;

        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
        } catch (Exception e) {}

        Trigger trigger = new Trigger(criteria, newTriggerType);
        trigger.getType().readFromJSON(data);
        EventsManager.onTriggerAdded(trigger);
        criteria.addTriggers(trigger);
        return trigger;
    }

    public static Reward newReward(Criteria criteria, String type, JsonObject data) {
        IRewardType oldRewardType = rewardTypes.get(type);
        if (oldRewardType == null) return null;

        IRewardType newRewardType = oldRewardType;
        boolean optional = data.get("optional") != null ? data.get("optional").getAsBoolean() : false;

        try {
            newRewardType = oldRewardType.getClass().newInstance();
        } catch (Exception e) {}
        
        /** SPECIAL CASE SWITCHING OF OLD REWARDS TO NEW **/
        if (newRewardType instanceof RewardBaseAction && JSONHelper.getExists(data, "craftingType")) {
            //If we are trying to create the old reward actions
            if (JSONHelper.getString(data, "craftingType", "").equals("breakblock")) newRewardType = new RewardBreakBlock();
            if (JSONHelper.getString(data, "craftingType", "").equals("entitydrop")) newRewardType = new RewardLivingDrop();
            if (JSONHelper.getString(data, "craftingType", "").equals("harvestdrop")) newRewardType = new RewardHarvestDrop();
            if (JSONHelper.getString(data, "craftingType", "").equals("crafting")) newRewardType = new RewardCrafting();
            if (JSONHelper.getString(data, "craftingType", "").equals("repair")) newRewardType = new RewardCrafting();
            if (JSONHelper.getString(data, "craftingType", "").equals("furnace")) newRewardType = new RewardFurnace();
        }

        Reward reward = new Reward(criteria, newRewardType, optional);
        reward.getType().readFromJSON(data);
        EventsManager.onRewardAdded(reward);
        criteria.addRewards(reward);
        return reward;
    }

    public static Trigger cloneTrigger(Criteria criteria, ITriggerType oldTriggerType) {
        ITriggerType newTriggerType = oldTriggerType;

        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
        } catch (Exception e) {}

        Trigger newTrigger = new Trigger(criteria, newTriggerType);
        EventsManager.onTriggerAdded(newTrigger);
        criteria.addTriggers(newTrigger);
        return newTrigger;
    }

    public static Reward cloneReward(Criteria criteria, IRewardType oldRewardType) {
        IRewardType newRewardType = oldRewardType;

        try {
            newRewardType = oldRewardType.getClass().newInstance();
        } catch (Exception e) {}

        Reward newReward = new Reward(criteria, newRewardType, false);
        EventsManager.onRewardAdded(newReward);
        criteria.addRewards(newReward);
        return newReward;
    }

    public static Condition cloneCondition(Trigger trigger, IConditionType oldConditionType) {
        IConditionType newConditionType = oldConditionType;

        try {
            newConditionType = oldConditionType.getClass().newInstance();
        } catch (Exception e) {}

        Condition newCondition = new Condition(trigger.getCriteria(), trigger, newConditionType, false);
        trigger.addCondition(newCondition);
        return newCondition;
    }
    
    /** Load in the setup method in enumhelper **/
    private static Method setup = null;
    static {
        try {
            setup = EnumHelper.class.getDeclaredMethod("setup");
            setup.setAccessible(true); //if security settings allow this
        } catch (Exception e) {}
    }
    
    @SuppressWarnings("rawtypes")
    private static Class[][] actionType = {
        {ActionType.class}
    };

    @Override
    public void registerActionType(String name) {
        try {
            setup.invoke(null);
            EnumHelper.addEnum(actionType, ActionType.class, name);
        } catch (Exception e) {}
    }

    public static Criteria getCriteriaFromName(String name) {
        return criteria.get(name);
    }

    public static Tab getTabFromName(String name) {
        return tabs.get(name);
    }

    public static void removeCriteria(String unique, boolean skipTab) {
        Criteria c = criteria.get(unique);
        //Remove the criteria from the tab
        if (!skipTab) {
            Iterator<Criteria> itC = c.tab.getCriteria().iterator();
            while (itC.hasNext()) {
                Criteria ic = itC.next();
                if (ic.equals(c)) {
                    itC.remove();
                }
            }
        }

        //Remove this from all the conflict lists
        for (Criteria conflict : c.conflicts) {
            Iterator<Criteria> it = conflict.conflicts.iterator();
            while (it.hasNext()) {
                Criteria ct = it.next();
                if (ct.equals(c)) {
                    it.remove();
                }
            }
        }

        //Remove this from all the requirement lists
        for (Criteria require : criteria.values()) {
            Iterator<Criteria> it = require.prereqs.iterator();
            while (it.hasNext()) {
                Criteria ct = it.next();
                if (ct.equals(c)) {
                    it.remove();
                }
            }
        }

        //Remove all rewards associated with this criteria
        for (Reward reward : c.rewards) {
            EventsManager.onRewardRemoved(reward);
            reward.getType().onRemoved();
        }

        //Remove all triggers associated with this criteria
        for (Trigger trigger : c.triggers) {
            EventsManager.onTriggerRemoved(trigger);
        }

        //Remove it in general
        criteria.remove(unique);
    }

    //Returns the next unique string for this crafting api
    public static String getNextUnique() {
        return "" + System.currentTimeMillis();
    }

    public static ITriggerData newData(String string) {
        if (string.equalsIgnoreCase("count")) {
            return new DataCount();
        } else if (string.equalsIgnoreCase("boolean")) {
            return new DataBoolean();
        } else if (string.equalsIgnoreCase("crafting")) {
            return new DataCrafting();
        }

        return null;
    }

    @Override
    public boolean canObtainFromAction(String actionType, ItemStack stack, Object tileOrPlayer) {
        ActionType type = ActionType.getCraftingActionFromName(actionType);
        return CraftingHelper.canCraftItem(type, tileOrPlayer, stack);
    }

    @Override
    public boolean canUseToPerformAction(String actionType, ItemStack stack, Object tileOrPlayer) {
        ActionType type = ActionType.getCraftingActionFromName(actionType);
        return CraftingHelper.canUseItemForCrafting(type, tileOrPlayer, stack);
    }
}
