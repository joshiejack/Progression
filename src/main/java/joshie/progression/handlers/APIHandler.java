package joshie.progression.handlers;

import com.google.gson.JsonObject;
import joshie.progression.api.ICustomDataBuilder;
import joshie.progression.api.IProgressionAPI;
import joshie.progression.api.criteria.*;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import static com.sun.media.jfxmediaimpl.AudioClipProvider.getProvider;

public class APIHandler implements IProgressionAPI {
    //Caches


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
    @SideOnly(Side.CLIENT)
    public static APICache clientCache;

    public static IRuleProvider getDefault(IRule provider) {
        if (provider instanceof ITrigger) return triggerTypes.get(provider.getProvider().getUnlocalisedName());
        if (provider instanceof IReward) return rewardTypes.get(provider.getProvider().getUnlocalisedName());
        if (provider instanceof ICondition) return conditionTypes.get(provider.getProvider().getUnlocalisedName());
        if (provider instanceof IFilter) return filterTypes.get(provider.getProvider().getUnlocalisedName());

        //WHAT
        return null;
    }

    public static void resetAPIHandler() {
        serverCache = new APICache();
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            clientCache = new APICache();
        }
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

    public static IConditionProvider registerConditionType(IRule rule, String unlocalised, int color) {
        try {
            String name = "condition." + unlocalised;
            IConditionProvider dummy = new Condition((ICondition)rule.getClass().newInstance(), name, color);
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

    public static IConditionProvider newCondition(ITriggerProvider trigger, UUID uuid, String type, JsonObject data) {
        IConditionProvider dummy = conditionTypes.get(type);
        if (dummy == null) return null;
        try {
            if (uuid == null) uuid = UUID.randomUUID();
            ICondition newConditionType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the trigger
            JSONHelper.readJSON(data, newConditionType);
            IConditionProvider provider = new Condition(trigger, uuid, newConditionType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor());
            provider.readFromJSON(data);
            EventsManager.onAdded(newConditionType);
            trigger.getConditions().add(provider);
            if (newConditionType instanceof IInit) ((IInit) newConditionType).init();
            return provider;
        } catch (Exception e) { return null; }
    }

    public static ITriggerProvider newTrigger(ICriteria criteria, UUID uuid, String type, JsonObject data) {
        ITriggerProvider dummy = triggerTypes.get(type);
        if (dummy == null) return null;
        try {
            if (uuid == null) uuid = UUID.randomUUID();
            ITrigger newTriggerType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the trigger
            JSONHelper.readJSON(data, newTriggerType);
            ITriggerProvider provider = new Trigger(criteria, uuid, newTriggerType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor(), dummy.isCancelable());
            provider.readFromJSON(data);
            criteria.getTriggers().add(provider);
            EventsManager.onAdded(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit) newTriggerType).init();
            return provider;
        } catch (Exception e) { return null; }
    }

    public static void newReward(ICriteria criteria, UUID uuid, String type, JsonObject data) {
        IRewardProvider dummy = rewardTypes.get(type);
        if (dummy == null) return;
        try {
            if (uuid == null) uuid = UUID.randomUUID();
            IReward newRewardType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the reward
            JSONHelper.readJSON(data, newRewardType);
            IRewardProvider provider = new Reward(criteria, uuid, newRewardType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor());
            provider.readFromJSON(data);
            criteria.getRewards().add(provider);
            EventsManager.onAdded(newRewardType);
            if (newRewardType instanceof IInit) ((IInit) newRewardType).init();
        } catch (Exception e) {}
    }

    public static IFilterProvider newFilter(IRuleProvider master, String type, JsonObject data) {
        IFilterProvider dummy = filterTypes.get(type);
        if (dummy == null) return null;
        try {
            IFilter newFilterType = dummy.getProvided().getClass().newInstance(); //Create a new instance of the reward
            JSONHelper.readJSON(data, newFilterType);
            IFilterProvider provider = new Filter(master, UUID.randomUUID(), newFilterType, dummy.getUnlocalisedName(), dummy.getColor());
            EventsManager.onAdded(newFilterType);
            if (newFilterType instanceof IInit) ((IInit) newFilterType).init();
            return provider;
        } catch (Exception e) { e.printStackTrace();  return  null; }
    }

    public static void cloneTrigger(ICriteria criteria, ITriggerProvider dummy) {
        try {
            ITrigger newTriggerType = dummy.getProvided().getClass().newInstance();
            criteria.getTriggers().add(new Trigger(criteria, UUID.randomUUID(), newTriggerType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor(), dummy.isCancelable()));
            EventsManager.onAdded(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit) newTriggerType).init();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void cloneReward(ICriteria criteria, IRewardProvider dummy) {
        try {
            IReward newRewardType = dummy.getProvided().getClass().newInstance();
            criteria.getRewards().add(new Reward(criteria, UUID.randomUUID(), newRewardType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor()));
            EventsManager.onAdded(newRewardType);
            if (newRewardType instanceof IInit) ((IInit) newRewardType).init();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void cloneCondition(ITriggerProvider trigger, IConditionProvider dummy) {
        try {
            ICondition newConditionType = dummy.getProvided().getClass().newInstance();
            trigger.getConditions().add(new Condition(trigger, UUID.randomUUID(), newConditionType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor()));
            EventsManager.onAdded(newConditionType);
            if (newConditionType instanceof IInit) ((IInit) newConditionType).init();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void cloneFilter(ItemFilterField field, IFilterProvider dummy) {
        try {
            IFilter newFilter = dummy.getProvided().getClass().newInstance();
            field.add(new Filter(dummy.getMaster(), UUID.randomUUID(), newFilter, dummy.getUnlocalisedName(), dummy.getColor()));
            EventsManager.onAdded(newFilter);
            if (newFilter instanceof IInit) ((IInit) newFilter).init();
        } catch (Exception e) { e.printStackTrace(); }
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
        getCriteria().remove(uuid);
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
