package joshie.progression.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.api.IConditionType;
import joshie.progression.api.ICriteria;
import joshie.progression.api.ICustomDataBuilder;
import joshie.progression.api.IFilter;
import joshie.progression.api.IProgressionAPI;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITab;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ITriggerType;
import joshie.progression.api.fields.IFieldProvider;
import joshie.progression.api.fields.IInit;
import joshie.progression.crafting.ActionType;
import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Tab;
import joshie.progression.criteria.triggers.data.DataBoolean;
import joshie.progression.criteria.triggers.data.DataCount;
import joshie.progression.criteria.triggers.data.DataCrafting;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.PacketFireTrigger;
import joshie.progression.network.PacketHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class APIHandler implements IProgressionAPI {
    //This is the registry for trigger type and reward type creation
    public static final HashMap<String, ITriggerType> triggerTypes = new HashMap();
    public static final HashMap<String, IRewardType> rewardTypes = new HashMap();
    public static final HashMap<String, IConditionType> conditionTypes = new HashMap();
    public static final HashMap<String, IFilter> itemFilterTypes = new HashMap();

    //These four maps are registries for fetching the various types
    @SideOnly(Side.CLIENT)
    private static HashMap<String, ITab> tabsClient;
    private static HashMap<String, ITab> tabsServer;
    @SideOnly(Side.CLIENT)
    private static HashMap<String, ICriteria> criteriaClient;
    private static HashMap<String, ICriteria> criteriaServer;

    public static IFieldProvider getDefault(IFieldProvider provider) {
        if (provider instanceof ITriggerType) return triggerTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IRewardType) return rewardTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IConditionType) return conditionTypes.get(provider.getUnlocalisedName());
        if (provider instanceof IFilter) return itemFilterTypes.get(provider.getUnlocalisedName());

        //WHAT
        return null;
    }

    public static void resetAPIHandler() {
        tabsServer = new HashMap();
        criteriaServer = new HashMap();

        if (isClientSide()) {
            tabsClient = new HashMap();
            criteriaClient = new HashMap();
        }
    }

    public static HashMap<String, ICriteria> getCriteria() {
        return isClientSide() ? criteriaClient : criteriaServer;
    }

    public static HashMap<String, ITab> getTabs() {
        return isClientSide() ? tabsClient : tabsServer;
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

    @Override
    public IFilter registerItemFilter(IFilter filter) {
        itemFilterTypes.put(filter.getUnlocalisedName(), filter);
        return filter;
    }

    public static ICriteria newCriteria(ITab tab, String name, boolean isClientside) {
        ICriteria theCriteria = new Criteria(tab, name, isClientside);
        tab.getCriteria().add(theCriteria);
        getCriteria().put(name, theCriteria);
        return theCriteria;
    }

    public static ITab newTab(String name) {
        ITab iTab = new Tab().setUniqueName(name);
        getTabs().put(name, iTab);
        return iTab;
    }

    public static IConditionType newCondition(ITriggerType trigger, String type, JsonObject data) {
        IConditionType oldConditionType = conditionTypes.get(type);
        if (oldConditionType == null) return null;
        IConditionType newConditionType = oldConditionType;

        try {
            newConditionType = oldConditionType.getClass().newInstance();
            newConditionType.setTrigger(trigger);
            JSONHelper.readJSON(data, newConditionType);
            trigger.getConditions().add(newConditionType);
        } catch (Exception e) {}
        return newConditionType;
    }

    public static ITriggerType newTrigger(ICriteria criteria, String type, JsonObject data) {
        ITriggerType oldTriggerType = triggerTypes.get(type);
        if (oldTriggerType == null) return null;

        ITriggerType newTriggerType = oldTriggerType;

        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
            newTriggerType.setCriteria(criteria);
            JSONHelper.readJSON(data, newTriggerType);
            EventsManager.onTriggerAdded(newTriggerType);
            criteria.getTriggers().add(newTriggerType);
        } catch (Exception e) {}

        return newTriggerType;
    }

    public static IRewardType newReward(ICriteria criteria, String type, JsonObject data) {
        IRewardType oldRewardType = rewardTypes.get(type);
        if (oldRewardType == null) return null;

        IRewardType newRewardType = oldRewardType;
        boolean optional = data.get("optional") != null ? data.get("optional").getAsBoolean() : false;

        try {
            newRewardType = oldRewardType.getClass().newInstance(); //Create a new instance of the reward
            newRewardType.setCriteria(criteria); //Let the reward know which criteria is attached to
            JSONHelper.readJSON(data, newRewardType);
            EventsManager.onRewardAdded(newRewardType);
            criteria.getRewards().add(newRewardType);
        } catch (Exception e) {}

        return newRewardType;
    }


   public static IFilter newFilter(String typeName, JsonObject typeData) {
        IFilter type = itemFilterTypes.get(typeName);
        if (type != null) {
            try {
                type = type.getClass().newInstance();
                JSONHelper.readJSON(typeData, type);
            } catch (Exception e) {}
        }

        return type;
    }

    public static ITriggerType cloneTrigger(ICriteria criteria, ITriggerType oldTriggerType) {
        ITriggerType newTriggerType = oldTriggerType;

        try {
            newTriggerType = oldTriggerType.getClass().newInstance();
            newTriggerType.setCriteria(criteria);
            EventsManager.onTriggerAdded(newTriggerType);
            criteria.getTriggers().add(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit)newTriggerType).init();
        } catch (Exception e) {}

        return newTriggerType;
    }

    public static IRewardType cloneReward(ICriteria criteria, IRewardType oldRewardType) {
        IRewardType newRewardType = oldRewardType;

        try {
            newRewardType = oldRewardType.getClass().newInstance();
            newRewardType.setCriteria(criteria);
            EventsManager.onRewardAdded(newRewardType);
            criteria.getRewards().add(newRewardType);
            if (newRewardType instanceof IInit) ((IInit)newRewardType).init();
        } catch (Exception e) {}

        return newRewardType;
    }

    public static IConditionType cloneCondition(ITriggerType trigger, IConditionType oldConditionType) {
        IConditionType newConditionType = oldConditionType;

        try {
            newConditionType = oldConditionType.getClass().newInstance();
            newConditionType.setTrigger(trigger);
            trigger.getConditions().add(newConditionType);
            if (newConditionType instanceof IInit) ((IInit)newConditionType).init();
        } catch (Exception e) {}

        return newConditionType;
    }
    

    public static void cloneFilter(ItemFilterField field, IFilter filter) {
        try {
            IFilter newFilter = filter.getClass().newInstance();
            if (newFilter instanceof IInit) ((IInit)newFilter).init();
            field.add(newFilter);
        } catch (Exception e) {}
    }

    @Override
    public void registerActionType(String name) {
        new ActionType(name.toUpperCase()); //WOOT!
    }

    public static ICriteria getCriteriaFromName(String name) {
        return getCriteria().get(name);
    }

    public static ITab getTabFromName(String name) {
        return getTabs().get(name);
    }

    public static void removeCriteria(String unique, boolean skipTab) {
        ICriteria c = getCriteria().get(unique);
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
        for (IRewardType reward : c.getRewards()) {
            EventsManager.onRewardRemoved(reward);
            reward.onRemoved();
        }

        //Remove all triggers associated with this criteria
        for (ITriggerType trigger : c.getTriggers()) {
            EventsManager.onTriggerRemoved(trigger);
        }

        //Remove it in general
        getCriteria().remove(unique);
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
