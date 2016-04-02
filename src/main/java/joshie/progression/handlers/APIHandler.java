package joshie.progression.handlers;

import com.google.gson.JsonObject;
import joshie.progression.api.ICustomDataBuilder;
import joshie.progression.api.IProgressionAPI;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.IInit;
import joshie.progression.crafting.ActionType;
import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Tab;
import joshie.progression.criteria.rewards.RewardHurt;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.PacketFireTrigger;
import joshie.progression.network.PacketHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class APIHandler implements IProgressionAPI {
    //Caches


    //This is the registry for trigger type and reward type creation
    public static final HashMap<String, IProgressionTrigger> triggerTypes = new HashMap();
    public static final HashMap<String, IProgressionReward> rewardTypes = new HashMap();
    public static final HashMap<String, IProgressionCondition> conditionTypes = new HashMap();
    public static final HashMap<String, IProgressionFilter> filterTypes = new HashMap();

    public static ICanHaveEvents getGenericFromType(ICanHaveEvents type) {
        if (type instanceof IProgressionTrigger) return triggerTypes.get(type.getUnlocalisedName());
        else if (type instanceof IProgressionReward) return rewardTypes.get(type.getUnlocalisedName());
        else if (type instanceof IProgressionFilter) return filterTypes.get(type.getUnlocalisedName());
        else return null; //Will never return null;
    }

    public static Collection<ICanHaveEvents> getCollectionFromType(ICanHaveEvents type) {
        if (type instanceof IProgressionTrigger) return new ArrayList(triggerTypes.values());
        else if (type instanceof IProgressionReward) return new ArrayList(rewardTypes.values());
        else if (type instanceof IProgressionFilter) return new ArrayList(filterTypes.values());
        else return null; //Will never return null;
    }

    //These four maps are registries for fetching the various types
    public static APICache serverCache;
    @SideOnly(Side.CLIENT)
    public static APICache clientCache;

    public static IFieldProvider getDefault(IFieldProvider provider) {
        if (provider instanceof IProgressionTrigger) return triggerTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IProgressionReward) return rewardTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IProgressionCondition) return conditionTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IProgressionFilter) return filterTypes.get(provider.getUnlocalisedName());

        //WHAT
        return null;
    }

    public static void resetAPIHandler() {
        serverCache = new APICache();
        clientCache = new APICache();
    }

    @SideOnly(Side.CLIENT)
    public static APICache getClientCache() {
        return clientCache;
    }

    public static APICache getServerCache() {
        return serverCache;
    }

    public static APICache getCache() {
        return isClientSide() ? clientCache : serverCache;
    }

    public static HashMap<UUID, IProgressionCriteria> getCriteria() {
        return getCache().getCriteria();
    }

    public static HashMap<UUID, IProgressionTab> getTabs() {
        return getCache().getTabs();
    }

    private static boolean isClientSide() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    @Override
    //Fired Server Side only
    public Result fireTrigger(UUID uuid, String string, Object... data) {
        return PlayerTracker.getServerPlayer(uuid).getMappings().fireAllTriggers(string, data);
    }

    @Override
    //Fired Server Side only
    public Result fireTrigger(EntityPlayer player, String string, Object... data) {
        if (!player.worldObj.isRemote) {
            return fireTrigger(PlayerHelper.getUUIDForPlayer(player), string, data);
        } else return Result.DEFAULT;
    }

    @Override
    public void fireTriggerClientside(String trigger, Object... data) {
        PacketHandler.sendToServer(new PacketFireTrigger(trigger, data));
    }

    @Override
    public void registerCustomDataBuilder(String trigger, ICustomDataBuilder builder) {
        PacketFireTrigger.handlers.put(trigger, builder);
    }

    @Override
    public IProgressionCondition registerConditionType(IProgressionCondition type) {
        conditionTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public IProgressionTrigger registerTriggerType(IProgressionTrigger type) {
        triggerTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public IProgressionReward registerRewardType(IProgressionReward type) {
        rewardTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public IProgressionFilter registerFilter(IProgressionFilter filter) {
        filterTypes.put(filter.getUnlocalisedName(), filter);
        return filter;
    }

    @Override
    public void registerDamageSource(DamageSource source) {
        RewardHurt.sources.put(source.damageType, source);
    }

    public static IProgressionCriteria newCriteria(IProgressionTab tab, UUID name, boolean isClientside) {
        IProgressionCriteria theCriteria = new Criteria(tab, name, isClientside);
        tab.getCriteria().add(theCriteria);
        getCriteria().put(name, theCriteria);
        return theCriteria;
    }

    public static IProgressionTab newTab(UUID name) {
        IProgressionTab iTab = new Tab().setUniqueName(name);
        getTabs().put(name, iTab);
        return iTab;
    }

    public static IProgressionCondition newCondition(IProgressionTrigger trigger, UUID uuid, String type, JsonObject data) {
        IProgressionCondition oldConditionType = conditionTypes.get(type);
        if (oldConditionType == null) return null;
        IProgressionCondition newConditionType = oldConditionType;

        try {
            if (uuid == null) uuid = UUID.randomUUID();
            newConditionType = oldConditionType.getClass().newInstance();
            newConditionType.setTrigger(trigger, uuid);
            JSONHelper.readJSON(data, newConditionType);
            trigger.getConditions().add(newConditionType);
        } catch (Exception e) {}
        return newConditionType;
    }

    public static IProgressionTrigger newTrigger(IProgressionCriteria criteria, UUID uuid, String type, JsonObject data) {
        IProgressionTrigger oldTriggerType = triggerTypes.get(type);
        if (oldTriggerType == null) return null;

        IProgressionTrigger newTriggerType = oldTriggerType;

        try {
            if (uuid == null) uuid = UUID.randomUUID();
            newTriggerType = oldTriggerType.getClass().newInstance();
            newTriggerType.setCriteria(criteria, uuid);
            JSONHelper.readJSON(data, newTriggerType);
            criteria.getTriggers().add(newTriggerType);
        } catch (Exception e) {}

        return newTriggerType;
    }

    public static IProgressionReward newReward(IProgressionCriteria criteria, UUID uuid, String type, JsonObject data) {
        IProgressionReward oldRewardType = rewardTypes.get(type);
        if (oldRewardType == null) return null;

        IProgressionReward newRewardType = oldRewardType;
        boolean optional = data.get("optional") != null ? data.get("optional").getAsBoolean() : false;

        try {
            if (uuid == null) uuid = UUID.randomUUID();
            newRewardType = oldRewardType.getClass().newInstance(); //Create a new instance of the reward
            newRewardType.setCriteria(criteria, uuid); //Let the reward know which criteria is attached to
            JSONHelper.readJSON(data, newRewardType);
            criteria.getRewards().add(newRewardType);
        } catch (Exception e) {}

        return newRewardType;
    }

    public static IProgressionFilter newFilter(String typeName, JsonObject typeData) {
        IProgressionFilter type = filterTypes.get(typeName);
        if (type != null) {
            try {
                type = type.getClass().newInstance();
                EventsManager.onAdded(type);
                JSONHelper.readJSON(typeData, type);
            } catch (Exception e) { e.printStackTrace(); }
        }

        return type;
    }

    public static IProgressionTrigger cloneTrigger(IProgressionCriteria criteria, IProgressionTrigger oldTriggerType) {
        IProgressionTrigger newTriggerType = oldTriggerType;

        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
            newTriggerType.setCriteria(criteria, UUID.randomUUID());
            EventsManager.onAdded(newTriggerType);
            criteria.getTriggers().add(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit) newTriggerType).init();
        } catch (Exception e) {}

        return newTriggerType;
    }

    public static IProgressionReward cloneReward(IProgressionCriteria criteria, IProgressionReward oldRewardType) {
        IProgressionReward newRewardType = oldRewardType;

        try {
            newRewardType = oldRewardType.getClass().newInstance();
            newRewardType.setCriteria(criteria, UUID.randomUUID());
            EventsManager.onAdded(newRewardType);
            criteria.getRewards().add(newRewardType);
            if (newRewardType instanceof IInit) ((IInit) newRewardType).init();
        } catch (Exception e) {}

        return newRewardType;
    }

    public static IProgressionCondition cloneCondition(IProgressionTrigger trigger, IProgressionCondition oldConditionType) {
        IProgressionCondition newConditionType = oldConditionType;

        try {
            newConditionType = oldConditionType.getClass().newInstance();
            newConditionType.setTrigger(trigger, UUID.randomUUID());
            trigger.getConditions().add(newConditionType);
            if (newConditionType instanceof IInit) ((IInit) newConditionType).init();
        } catch (Exception e) {}

        return newConditionType;
    }

    public static void cloneFilter(ItemFilterField field, IProgressionFilter filter) {
        try {
            IProgressionFilter newFilter = filter.getClass().newInstance();
            if (newFilter instanceof IInit) ((IInit) newFilter).init();
            field.add(newFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerActionType(String name) {
        new ActionType(name.toUpperCase()); //WOOT!
    }

    public static IProgressionCriteria getCriteriaFromName(UUID name) {
        return getCache().getCriteria().get(name);
    }

    public static IProgressionTab getTabFromName(UUID name) {
        return getCache().getTabs().get(name);
    }

    public static void removeCriteria(UUID uuid, boolean skipTab) {
        IProgressionCriteria c = getCriteria().get(uuid);
        //Remove the criteria from the tab
        if (!skipTab) {
            Iterator<IProgressionCriteria> itC = c.getTab().getCriteria().iterator();
            while (itC.hasNext()) {
                IProgressionCriteria ic = itC.next();
                if (ic.equals(c)) {
                    itC.remove();
                }
            }
        }

        //Remove this from all the conflict lists
        for (IProgressionCriteria conflict : c.getConflicts()) {
            Iterator<IProgressionCriteria> it = conflict.getConflicts().iterator();
            while (it.hasNext()) {
                IProgressionCriteria ct = it.next();
                if (ct.equals(c)) {
                    it.remove();
                }
            }
        }

        //Remove this from all the requirement lists
        for (IProgressionCriteria require : getCriteria().values()) {
            Iterator<IProgressionCriteria> it = require.getPreReqs().iterator();
            while (it.hasNext()) {
                IProgressionCriteria ct = it.next();
                if (ct.equals(c)) {
                    it.remove();
                }
            }
        }

        //Remove all rewards associated with this criteria
        for (IProgressionReward reward : c.getRewards()) {
            EventsManager.onRemoved(reward);
            if (reward instanceof IHasFilters) {
                for (IProgressionFilter filter: ((IHasFilters)reward).getAllFilters()) {
                    EventsManager.onRemoved(filter);
                }
            }
        }

        //Remove all triggers associated with this criteria
        for (IProgressionTrigger trigger : c.getTriggers()) {
            EventsManager.onRemoved(trigger);
            for (IProgressionCondition condition: trigger.getConditions()) {
                if (condition instanceof IHasFilters) {
                    for (IProgressionFilter filter: ((IHasFilters)condition).getAllFilters()) {
                        EventsManager.onRemoved(filter);
                    }
                }
            }

            if (trigger instanceof IHasFilters) {
                for (IProgressionFilter filter: ((IHasFilters)trigger).getAllFilters()) {
                    EventsManager.onRemoved(filter);
                }
            }
        }

        //Remove it in general
        getCriteria().remove(uuid);
    }

    @Override
    public boolean canObtainFromAction(String actionType, ItemStack stack, Object tileOrPlayer) {
        ActionType type = ActionType.getCraftingActionFromName(actionType);
        return CraftingHelper.canPerformActionAbstract(type, tileOrPlayer, stack);
    }

    @Override
    public boolean canUseToPerformAction(String actionType, ItemStack stack, Object tileOrPlayer) {
        ActionType type = ActionType.getCraftingActionFromName(actionType);
        return CraftingHelper.canPerformActionAbstract(type, tileOrPlayer, stack);
    }
}
