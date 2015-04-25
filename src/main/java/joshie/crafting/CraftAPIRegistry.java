package joshie.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import joshie.crafting.api.IConditionType;
import joshie.crafting.api.IRegistry;
import joshie.crafting.api.IRewardType;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.api.ITriggerType;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.player.PlayerTracker;
import joshie.crafting.trigger.data.DataBoolean;
import joshie.crafting.trigger.data.DataCount;
import joshie.crafting.trigger.data.DataCrafting;
import net.minecraft.entity.player.EntityPlayer;

import com.google.gson.JsonObject;

public class CraftAPIRegistry implements IRegistry {
    //This is the registry for trigger type and reward type creation
    public static final HashMap<String, ITriggerType> triggerTypes = new HashMap();
    public static final HashMap<String, IRewardType> rewardTypes = new HashMap();
    public static final HashMap<String, IConditionType> conditionTypes = new HashMap();

    //These four maps are registries for fetching the various types
    public static HashMap<String, Tab> tabs;
    public static HashMap<String, Criteria> criteria;
    public static Set<Trigger> triggers;
    public static Set<Condition> conditions;
    public static Set<Reward> rewards;

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
        boolean inverted = data.get("inverted") != null ? data.get("inverted").getAsBoolean() : false;
        IConditionType oldConditionType = conditionTypes.get(type);
        IConditionType newConditionType = oldConditionType;
        
        try {
            newConditionType = oldConditionType.getClass().newInstance();
        } catch (Exception e) {}
        
        Condition condition = new Condition(trigger.getCriteria(), trigger, newConditionType);
        condition.getType().readFromJSON(data);
        conditions.add(condition);
        return condition;
    }

    public static Trigger newTrigger(Criteria criteria, String type, JsonObject data) {
        ITriggerType oldTriggerType = triggerTypes.get(type);
        ITriggerType newTriggerType = oldTriggerType;
        
        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
        } catch (Exception e) {}
        
        Trigger trigger = new Trigger(criteria, newTriggerType);
        trigger.getType().readFromJSON(data);
        CraftingEventsManager.onTriggerAdded(trigger);
        triggers.add(trigger);
        return trigger;
    }

    public static Reward newReward(Criteria criteria, String type, JsonObject data) {
        IRewardType oldRewardType = rewardTypes.get(type);
        IRewardType newRewardType = oldRewardType;
        
        try {
            newRewardType = oldRewardType.getClass().newInstance();
        } catch (Exception e) {}
        
        Reward reward = new Reward(criteria, newRewardType);
        reward.getType().readFromJSON(data);
        CraftingEventsManager.onRewardAdded(reward);
        rewards.add(reward);
        return reward;
    }

    public static Trigger cloneTrigger(Criteria criteria, ITriggerType oldTriggerType) {
        ITriggerType newTriggerType = oldTriggerType;
        
        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
        } catch (Exception e) {}
        
        
        Trigger newTrigger = new Trigger(criteria, newTriggerType);
        CraftingEventsManager.onTriggerAdded(newTrigger);
        triggers.add(newTrigger);
        criteria.addTriggers(newTrigger);
        return newTrigger;
    }

    public static Reward cloneReward(Criteria criteria, IRewardType oldRewardType) {
        IRewardType newRewardType = oldRewardType;
        
        try {
            newRewardType = oldRewardType.getClass().newInstance();
        } catch (Exception e) {}
        
        
        Reward newReward = new Reward(criteria, newRewardType);
        CraftingEventsManager.onRewardAdded(newReward);
        rewards.add(newReward);
        criteria.addRewards(newReward);
        return newReward;
    }

    public static Condition cloneCondition(Trigger trigger, IConditionType oldConditionType) {
        IConditionType newConditionType = oldConditionType;
        
        try {
            newConditionType = oldConditionType.getClass().newInstance();
        } catch (Exception e) {}
        
        
        Condition newCondition = new Condition(trigger.getCriteria(), trigger, newConditionType);
        conditions.add(newCondition);
        return newCondition;
    }

    public static Criteria getCriteriaFromName(String name) {
        return criteria.get(name);
    }

    public static Tab getTabFromName(String name) {
        return tabs.get(name);
    }

    public static Collection<Criteria> getCriteria() {
        return criteria.values();
    }

    /** Convenience methods for removals **/
    public static void removeCondition(Condition condition) {
        conditions.remove(condition);
    }

    public static void removeTrigger(Trigger trigger) {
        CraftingEventsManager.onTriggerRemoved(trigger);
        triggers.remove(trigger);
    }

    public static void removeReward(Reward reward) {
        CraftingEventsManager.onRewardRemoved(reward);
        reward.getType().onRemoved();
        rewards.remove(reward);
    }

    public static void removeTab(Tab tab) {
        for (Criteria c: tab.getCriteria()) {
            removeCriteria(c.uniqueName, true);
        }
        
        tabs.remove(tab.getUniqueName());
    }
    
    public static void removeCriteria(String unique) {
        removeCriteria(unique, false);
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
            removeReward(reward);
        }

        //Remove all triggers associated with this criteria
        for (Trigger trigger : c.triggers) {
            removeTrigger(trigger);
        }

        //Remove it in general
        criteria.remove(unique);
    }

    //Returns the next unique string for this crafting api
    public static String getNextUnique() {
        return "" + System.currentTimeMillis();
    }

    @Override
    public ITriggerData newData(String string) {
        if (string.equalsIgnoreCase("count")) {
            return new DataCount();
        } else if (string.equalsIgnoreCase("boolean")) {
            return new DataBoolean();
        } else if (string.equalsIgnoreCase("crafting")) {
            return new DataCrafting();
        }
        
        return null;
    }
}
