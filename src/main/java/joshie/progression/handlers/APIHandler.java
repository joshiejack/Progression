package joshie.progression.handlers;

import com.google.gson.JsonObject;
import joshie.progression.api.ICustomDataBuilder;
import joshie.progression.api.IProgressionAPI;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.ICustomIcon;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.IRequestItem;
import joshie.progression.crafting.ActionType;
import joshie.progression.criteria.*;
import joshie.progression.criteria.rewards.RewardHurt;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.PacketFireTrigger;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketRequestItem;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import java.util.*;

import static joshie.progression.gui.core.GuiList.CORE;

public class APIHandler implements IProgressionAPI {
    //This is the registry for trigger type and reward type creation
    public static final HashMap<String, ITriggerProvider> triggerTypes = new HashMap();
    public static final HashMap<String, IRewardProvider> rewardTypes = new HashMap();
    public static final HashMap<String, IConditionProvider> conditionTypes = new HashMap();
    public static final HashMap<String, IFilterProvider> filterTypes = new HashMap();

    public static IRule getGenericFromType(IRule type) {
        if (type instanceof ITrigger) return triggerTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else if (type instanceof IReward) return rewardTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else if (type instanceof IFilter) return filterTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else if (type instanceof ICondition) return conditionTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else return null; //Will never return null;
    }

    public static Collection<IRuleProvider> getCollectionFromType(IRule type) {
        if (type instanceof ITrigger) return new ArrayList(triggerTypes.values());
        else if (type instanceof IReward) return new ArrayList(rewardTypes.values());
        else if (type instanceof ICondition) return new ArrayList(conditionTypes.values());
        else if (type instanceof IFilter) return new ArrayList(filterTypes.values());
        else return null; //Will never return null;
    }

    //These four maps are registries for fetching the various types
    public static APICache serverCache;
    public static APICache clientCache;

    public static IRuleProvider getDefault(IRule provider) {
        if (provider instanceof ITrigger) return triggerTypes.get(provider.getProvider().getUnlocalisedName());
        if (provider instanceof IReward) return rewardTypes.get(provider.getProvider().getUnlocalisedName());
        if (provider instanceof ICondition) return conditionTypes.get(provider.getProvider().getUnlocalisedName());
        if (provider instanceof IFilter) return filterTypes.get(provider.getProvider().getUnlocalisedName());

        //WHAT
        return null;
    }

    public static void resetAPIHandler(boolean isClient) {
        if (isClient) {
            clientCache = new APICache();
        } else serverCache = new APICache();
    }

    public static APICache getClientCache() {
        return clientCache;
    }

    public static APICache getServerCache() {
        return serverCache;
    }

    public static APICache getCache(boolean isClient) {
        return isClient ? getClientCache() : getServerCache();
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

    public static IConditionProvider registerConditionType(IRule rule, String unlocalised) {
        try {
            String name = "condition." + unlocalised;
            IConditionProvider dummy = new Condition((ICondition)rule.getClass().newInstance(), name);
            conditionTypes.put(name, dummy);
            return dummy;
        } catch (Exception e) {return null; }
    }

    public static ITriggerProvider registerTriggerType(IRule rule, String unlocalised, int color) {
        try {
            String name = "trigger." + unlocalised;
            ITriggerProvider dummy = new Trigger((ITrigger)rule.getClass().newInstance(), name, color);
            triggerTypes.put(name, dummy);
            return dummy;
        } catch (Exception e) { return null; }
    }

    public static IRewardProvider registerRewardType(IRule rule, String unlocalised, int color) {
        try {
            String name = "reward." + unlocalised;
            IRewardProvider dummy = new Reward((IReward)rule.getClass().newInstance(), name, color);
            rewardTypes.put(name, dummy);
            return dummy;
        } catch (Exception e) { return null; }
    }


    public static IFilterProvider registerFilterType(IRule rule, String unlocalised, int color) {
        try {
            IFilter filter = (IFilter)rule.getClass().newInstance();
            String name = "filter." + filter.getType().getName() + "." + unlocalised;
            IFilterProvider dummy = new Filter(filter, name, color);
            filterTypes.put(name, dummy);
            return dummy;
        } catch (Exception e) { return null; }
    }

    @Override
    public void registerDamageSource(DamageSource source) {
        RewardHurt.sources.put(source.damageType, source);
    }

    public static ICriteria newCriteria(ITab tab, UUID name, boolean isClient) {
        ICriteria theCriteria = new Criteria(tab, name);
        tab.getCriteria().add(theCriteria);
        getCache(isClient).addCriteria(theCriteria);
        return theCriteria;
    }

    public static ITab newTab(UUID name, boolean isClient) {
        ITab iTab = new Tab().setUniqueName(name);
        getCache(isClient).getTabs().put(name, iTab);
        return iTab;
    }

    public static IConditionProvider newCondition(ITriggerProvider trigger, UUID uuid, String type, JsonObject data, boolean isClient) {
        IConditionProvider dummy = conditionTypes.get(type);
        if (dummy == null) return null;
        try {
            if (uuid == null) uuid = UUID.randomUUID();
            ICondition newConditionType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the trigger
            JSONHelper.readJSON(data, newConditionType, isClient);
            ItemStack icon = dummy.getProvided() instanceof ICustomIcon ? new ItemStack(Items.written_book) : dummy.getIcon();
            IConditionProvider provider = new Condition(trigger, uuid, newConditionType, icon, dummy.getUnlocalisedName());
            provider.readFromJSON(data);
            EventsManager.onAdded(newConditionType);
            trigger.getConditions().add(provider);
            if (newConditionType instanceof IInit) ((IInit) newConditionType).init(isClient);
            return provider;
        } catch (Exception e) { return null; }
    }

    public static ITriggerProvider newTrigger(ICriteria criteria, UUID uuid, String type, JsonObject data, boolean isClient) {
        ITriggerProvider dummy = triggerTypes.get(type);
        if (dummy == null) return null;
        try {
            if (uuid == null) uuid = UUID.randomUUID();
            ITrigger newTriggerType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the trigger
            JSONHelper.readJSON(data, newTriggerType, isClient);
            ItemStack icon = dummy.getProvided() instanceof ICustomIcon ? new ItemStack(Items.rabbit_foot) : dummy.getIcon();
            ITriggerProvider provider = new Trigger(criteria, uuid, newTriggerType, icon, dummy.getUnlocalisedName(), dummy.getColor(), dummy.isCancelable());
            provider.readFromJSON(data);
            criteria.getTriggers().add(provider);
            EventsManager.onAdded(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit) newTriggerType).init(isClient);
            //Register with the cache
            getCache(isClient).addTrigger(provider);
            return provider;
        } catch (Exception e) { return null; }
    }

    public static void newReward(ICriteria criteria, UUID uuid, String type, JsonObject data, boolean isClient) {
        IRewardProvider dummy = rewardTypes.get(type);
        if (dummy == null) return;
        try {
            if (uuid == null) uuid = UUID.randomUUID();
            IReward newRewardType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the reward
            JSONHelper.readJSON(data, newRewardType, isClient);
            ItemStack icon = dummy.getProvided() instanceof ICustomIcon ? new ItemStack(Items.gold_ingot) : dummy.getIcon();
            IRewardProvider provider = new Reward(criteria, uuid, newRewardType, icon, dummy.getUnlocalisedName(), dummy.getColor());
            provider.readFromJSON(data);
            criteria.getRewards().add(provider);
            EventsManager.onAdded(newRewardType);
            if (newRewardType instanceof IInit) ((IInit) newRewardType).init(isClient);
            //Register with the cache
            getCache(isClient).addReward(provider);
        } catch (Exception e) {}
    }

    public static IFilterProvider newFilter(IRuleProvider master, String type, JsonObject data, boolean isClient) {
        IFilterProvider dummy = filterTypes.get(type);
        if (dummy == null) return null;
        try {
            IFilter newFilterType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the reward
            JSONHelper.readJSON(data, newFilterType, isClient);
            IFilterProvider provider = new Filter(master, UUID.randomUUID(), newFilterType, dummy.getUnlocalisedName(), dummy.getColor());
            EventsManager.onAdded(newFilterType);
            if (newFilterType instanceof IInit) ((IInit) newFilterType).init(isClient);
            return provider;
        } catch (Exception e) { e.printStackTrace();  return  null; }
    }

    public static void cloneTrigger(ICriteria criteria, ITriggerProvider dummy) {
        try {
            ITrigger newTriggerType = dummy.getProvided().getClass().newInstance();
            ITriggerProvider clone = new Trigger(criteria, UUID.randomUUID(), newTriggerType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor(), dummy.isCancelable());
            criteria.getTriggers().add(clone);
            EventsManager.onAdded(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit) newTriggerType).init(true);

            //Reinit the currently open gui
            getClientCache().addTrigger(clone);
            CORE.openGui.initData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void cloneReward(ICriteria criteria, IRewardProvider dummy) {
        try {
            IReward newRewardType = dummy.getProvided().getClass().newInstance();
            IRewardProvider clone = new Reward(criteria, UUID.randomUUID(), newRewardType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor());
            criteria.getRewards().add(clone);
            EventsManager.onAdded(newRewardType);
            if (newRewardType instanceof IInit) ((IInit) newRewardType).init(true);

            //Reinit the currently open gui
            getClientCache().addReward(clone);
            CORE.openGui.initData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void cloneCondition(ITriggerProvider trigger, IConditionProvider dummy) {
        try {
            ICondition newConditionType = dummy.getProvided().getClass().newInstance();
            trigger.getConditions().add(new Condition(trigger, UUID.randomUUID(), newConditionType, dummy.getIcon(), dummy.getUnlocalisedName()));
            EventsManager.onAdded(newConditionType);
            if (newConditionType instanceof IInit) ((IInit) newConditionType).init(true);

            //Reinit the currently open gui
            CORE.openGui.initData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void cloneFilter(ItemFilterField field, IFilterProvider dummy) {
        try {
            IFilter newFilter = dummy.getProvided().getClass().newInstance();
            field.add(new Filter(dummy.getMaster(), UUID.randomUUID(), newFilter, dummy.getUnlocalisedName(), dummy.getColor()));
            EventsManager.onAdded(newFilter);
            if (newFilter instanceof IInit) ((IInit) newFilter).init(true);

            //Reinit the currently open gui
            CORE.openGui.initData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public IAction registerActionType(String name) {
        return new ActionType(name.toUpperCase()); //WOOT!
    }

    public static void removeCriteria(UUID uuid, boolean skipTab) {
        ICriteria c = getClientCache().getCriteria(uuid);
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
        for (ICriteria require : getClientCache().getCriteriaSet()) {
            Iterator<ICriteria> it = require.getPreReqs().iterator();
            while (it.hasNext()) {
                ICriteria ct = it.next();
                if (ct.equals(c)) {
                    it.remove();
                }
            }
        }

        //Remove all rewards associated with this criteria
        for (IRewardProvider provider : c.getRewards()) {
            IReward reward = provider.getProvided();
            EventsManager.onRemoved(reward);
            if (reward instanceof IHasFilters) {
                for (IFilterProvider filter: ((IHasFilters)reward).getAllFilters()) {
                    EventsManager.onRemoved(filter.getProvided());
                }
            }
        }

        //Remove all triggers associated with this criteria
        for (ITriggerProvider provider : c.getTriggers()) {
            ITrigger trigger = provider.getProvided();
            EventsManager.onRemoved(trigger);
            for (IConditionProvider conditionProvider: provider.getConditions()) {
                ICondition condition = conditionProvider.getProvided();
                EventsManager.onRemoved(condition);
                if (condition instanceof IHasFilters) {
                    for (IFilterProvider filter: ((IHasFilters)condition).getAllFilters()) {
                        EventsManager.onRemoved(filter.getProvided());
                    }
                }
            }

            if (trigger instanceof IHasFilters) {
                for (IFilterProvider filter: ((IHasFilters)trigger).getAllFilters()) {
                    EventsManager.onRemoved(filter.getProvided());
                }
            }
        }

        //Remove it in general
        getClientCache().removeCriteria(c);
    }

    @Override
    public void requestItem(IRequestItem reward, EntityPlayer player) {
        PacketHandler.sendToClient(new PacketRequestItem(reward.getProvider().getUniqueID()), player);
    }

    @Override
    public boolean canUseToPerformAction(String actionType, ItemStack stack, Object tileOrPlayer) {
        ActionType type = ActionType.getCraftingActionFromName(actionType);
        return CraftingHelper.canPerformActionAbstract(type, tileOrPlayer, stack);
    }
}
