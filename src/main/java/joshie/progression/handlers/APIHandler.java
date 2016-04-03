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
    public static final HashMap<String, ITrigger> triggerTypes = new HashMap();
    public static final HashMap<String, IReward> rewardTypes = new HashMap();
    public static final HashMap<String, ICondition> conditionTypes = new HashMap();
    public static final HashMap<String, IFilter> filterTypes = new HashMap();

    public static ICanHaveEvents getGenericFromType(ICanHaveEvents type) {
        if (type instanceof ITrigger) return triggerTypes.get(type.getUnlocalisedName());
        else if (type instanceof IReward) return rewardTypes.get(type.getUnlocalisedName());
        else if (type instanceof IFilter) return filterTypes.get(type.getUnlocalisedName());
        else return null; //Will never return null;
    }

    public static Collection<ICanHaveEvents> getCollectionFromType(ICanHaveEvents type) {
        if (type instanceof ITrigger) return new ArrayList(triggerTypes.values());
        else if (type instanceof IReward) return new ArrayList(rewardTypes.values());
        else if (type instanceof IFilter) return new ArrayList(filterTypes.values());
        else return null; //Will never return null;
    }

    //These four maps are registries for fetching the various types
    public static APICache serverCache;
    @SideOnly(Side.CLIENT)
    public static APICache clientCache;

    public static IFieldProvider getDefault(IFieldProvider provider) {
        if (provider instanceof ITrigger) return triggerTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IReward) return rewardTypes.get(provider.getUnlocalisedName());
        if (provider instanceof ICondition) return conditionTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IFilter) return filterTypes.get(provider.getUnlocalisedName());

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

    public static HashMap<UUID, ICriteria> getCriteria() {
        return getCache().getCriteria();
    }

    public static HashMap<UUID, ITab> getTabs() {
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
    public ICondition registerConditionType(ICondition type) {
        conditionTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public ITrigger registerTriggerType(ITrigger type) {
        triggerTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public IReward registerRewardType(IReward type) {
        rewardTypes.put(type.getUnlocalisedName(), type);
        return type;
    }

    @Override
    public IFilter registerFilter(IFilter filter) {
        filterTypes.put(filter.getUnlocalisedName(), filter);
        return filter;
    }

    @Override
    public void registerDamageSource(DamageSource source) {
        RewardHurt.sources.put(source.damageType, source);
    }

    public static ICriteria newCriteria(ITab tab, UUID name, boolean isClientside) {
        ICriteria theCriteria = new Criteria(tab, name, isClientside);
        tab.getCriteria().add(theCriteria);
        getCriteria().put(name, theCriteria);
        return theCriteria;
    }

    public static ITab newTab(UUID name) {
        ITab iTab = new Tab().setUniqueName(name);
        getTabs().put(name, iTab);
        return iTab;
    }

    public static ICondition newCondition(ITrigger trigger, UUID uuid, String type, JsonObject data) {
        ICondition oldConditionType = conditionTypes.get(type);
        if (oldConditionType == null) return null;
        ICondition newConditionType = oldConditionType;

        try {
            if (uuid == null) uuid = UUID.randomUUID();
            newConditionType = oldConditionType.getClass().newInstance();
            newConditionType.setTrigger(trigger, uuid);
            JSONHelper.readJSON(data, newConditionType);
            trigger.getConditions().add(newConditionType);
        } catch (Exception e) {}
        return newConditionType;
    }

    public static ITrigger newTrigger(ICriteria criteria, UUID uuid, String type, JsonObject data) {
        ITrigger oldTriggerType = triggerTypes.get(type);
        if (oldTriggerType == null) return null;

        ITrigger newTriggerType = oldTriggerType;

        try {
            if (uuid == null) uuid = UUID.randomUUID();
            newTriggerType = oldTriggerType.getClass().newInstance();
            newTriggerType.setCriteria(criteria, uuid);
            JSONHelper.readJSON(data, newTriggerType);
            criteria.getTriggers().add(newTriggerType);
        } catch (Exception e) {}

        return newTriggerType;
    }

    public static IReward newReward(ICriteria criteria, UUID uuid, String type, JsonObject data) {
        IReward oldRewardType = rewardTypes.get(type);
        if (oldRewardType == null) return null;

        IReward newRewardType = oldRewardType;
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

    public static IFilter newFilter(String typeName, JsonObject typeData) {
        IFilter type = filterTypes.get(typeName);
        if (type != null) {
            try {
                type = type.getClass().newInstance();
                EventsManager.onAdded(type);
                JSONHelper.readJSON(typeData, type);
            } catch (Exception e) { e.printStackTrace(); }
        }

        return type;
    }

    public static ITrigger cloneTrigger(ICriteria criteria, ITrigger oldTriggerType) {
        ITrigger newTriggerType = oldTriggerType;

        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
            newTriggerType.setCriteria(criteria, UUID.randomUUID());
            EventsManager.onAdded(newTriggerType);
            criteria.getTriggers().add(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit) newTriggerType).init();
        } catch (Exception e) {}

        return newTriggerType;
    }

    public static IReward cloneReward(ICriteria criteria, IReward oldRewardType) {
        IReward newRewardType = oldRewardType;

        try {
            newRewardType = oldRewardType.getClass().newInstance();
            newRewardType.setCriteria(criteria, UUID.randomUUID());
            EventsManager.onAdded(newRewardType);
            criteria.getRewards().add(newRewardType);
            if (newRewardType instanceof IInit) ((IInit) newRewardType).init();
        } catch (Exception e) {}

        return newRewardType;
    }

    public static ICondition cloneCondition(ITrigger trigger, ICondition oldConditionType) {
        ICondition newConditionType = oldConditionType;

        try {
            newConditionType = oldConditionType.getClass().newInstance();
            newConditionType.setTrigger(trigger, UUID.randomUUID());
            trigger.getConditions().add(newConditionType);
            if (newConditionType instanceof IInit) ((IInit) newConditionType).init();
        } catch (Exception e) {}

        return newConditionType;
    }

    public static void cloneFilter(ItemFilterField field, IFilter filter) {
        try {
            IFilter newFilter = filter.getClass().newInstance();
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

    public static ICriteria getCriteriaFromName(UUID name) {
        return getCache().getCriteria().get(name);
    }

    public static ITab getTabFromName(UUID name) {
        return getCache().getTabs().get(name);
    }

    public static void removeCriteria(UUID uuid, boolean skipTab) {
        ICriteria c = getCriteria().get(uuid);
        //Remove the criteria from the tab
        if (!skipTab) {
            Iterator<ICriteria> itC = c.getTab().getCriteria().iterator();
            while (itC.hasNext()) {
                ICriteria ic = itC.next();
                if (ic.equals(c)) {
                    itC.remove();
                }
            }
        }

        //Remove this from all the conflict lists
        for (ICriteria conflict : c.getConflicts()) {
            Iterator<ICriteria> it = conflict.getConflicts().iterator();
            while (it.hasNext()) {
                ICriteria ct = it.next();
                if (ct.equals(c)) {
                    it.remove();
                }
            }
        }

        //Remove this from all the requirement lists
        for (ICriteria require : getCriteria().values()) {
            Iterator<ICriteria> it = require.getPreReqs().iterator();
            while (it.hasNext()) {
                ICriteria ct = it.next();
                if (ct.equals(c)) {
                    it.remove();
                }
            }
        }

        //Remove all rewards associated with this criteria
        for (IReward reward : c.getRewards()) {
            EventsManager.onRemoved(reward);
            if (reward instanceof IHasFilters) {
                for (IFilter filter: ((IHasFilters)reward).getAllFilters()) {
                    EventsManager.onRemoved(filter);
                }
            }
        }

        //Remove all triggers associated with this criteria
        for (ITrigger trigger : c.getTriggers()) {
            EventsManager.onRemoved(trigger);
            for (ICondition condition: trigger.getConditions()) {
                if (condition instanceof IHasFilters) {
                    for (IFilter filter: ((IHasFilters)condition).getAllFilters()) {
                        EventsManager.onRemoved(filter);
                    }
                }
            }

            if (trigger instanceof IHasFilters) {
                for (IFilter filter: ((IHasFilters)trigger).getAllFilters()) {
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
