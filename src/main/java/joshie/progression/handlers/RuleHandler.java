package joshie.progression.handlers;

import com.google.gson.JsonObject;
import joshie.progression.ItemProgression.ItemMeta;
import joshie.progression.Progression;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.ICustomIcon;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.IInit;
import joshie.progression.criteria.*;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.StackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.*;

import static joshie.progression.gui.core.GuiList.CORE;

public class RuleHandler {
    //Borrowed from JEI
    public static void registerRules(@Nonnull ASMDataTable asmDataTable) {
        Class annotationClass = ProgressionRule.class;
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<ASMData>(asmDataTable.getAll(annotationClassName));

        topLoop:
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Class<? extends IRule> asmInstanceClass = asmClass.asSubclass(IRule.class);
                IRule instance = asmInstanceClass.newInstance();
                Map<String, Object> data = asmData.getAnnotationInfo();
                String modData = (String) data.get("mod");
                if (modData != null) {
                    String[] mods = modData.replace(" ", "").split(",");
                    for (String mod: mods) {
                        if (mod != null && !Loader.isModLoaded(mod)) continue topLoop;
                    }
                }


                String name = (String) data.get("name");
                int color = 0xFFCCCCCC;
                if (data.get("color") != null) {
                    color = (Integer) data.get("color");
                }

                String icon = (String) data.get("icon");
                String meta = (String) data.get("meta");
                boolean isCancelable = false;
                if (data.get("cancelable") != null) {
                    isCancelable = (Boolean) data.get("cancelable");
                }

                ItemStack stack = StackHelper.getStackFromString(icon);
                if (stack == null) stack = new ItemStack(Progression.item);
                if (meta != null) {
                    for (ItemMeta item: ItemMeta.values()) {
                        if (item.name().equalsIgnoreCase(meta)) {
                            stack.setItemDamage(item.ordinal());
                            break;
                        }
                    }
                }

                if (instance instanceof IReward) {
                    APIHandler.registerRewardType(instance, name, color).setIcon(stack);
                } else if (instance instanceof ITrigger) {
                    ITriggerProvider provider = APIHandler.registerTriggerType(instance, name, color).setIcon(stack);
                    if (isCancelable) {
                        provider.setCancelable();
                    }
                } else if (instance instanceof ICondition) {
                    APIHandler.registerConditionType(instance, name).setIcon(stack);
                } else if (instance instanceof IFilter) {
                    APIHandler.registerFilterType(instance, name, color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public static ITab newTab(UUID name, boolean isClient) {
        return APICache.getCache(isClient).addTab(new Tab().setUniqueName(name));
    }

    public static ICriteria newCriteria(ITab tab, UUID name, boolean isClient) {
        ICriteria theCriteria = new Criteria(tab, name);
        tab.getCriteria().add(theCriteria);
        return APICache.getCache(isClient).addCriteria(theCriteria);
    }

    public static ITriggerProvider newTrigger(ICriteria criteria, UUID uuid, String type, JsonObject data, boolean isClient) {
        ITriggerProvider dummy = APIHandler.triggerTypes.get(type);
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
            return APICache.getCache(isClient).addTrigger(provider);
        } catch (Exception e) { return null; }
    }

    public static IConditionProvider newCondition(ITriggerProvider trigger, UUID uuid, String type, JsonObject data, boolean isClient) {
        IConditionProvider dummy = APIHandler.conditionTypes.get(type);
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
            return APICache.getCache(isClient).addCondition(provider);
        } catch (Exception e) { return null; }
    }

    public static void newReward(ICriteria criteria, UUID uuid, String type, JsonObject data, boolean isClient) {
        IRewardProvider dummy = APIHandler.rewardTypes.get(type);
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
            APICache.getCache(isClient).addReward(provider);
        } catch (Exception e) {}
    }

    public static IFilterProvider newFilter(IRuleProvider master, String type, JsonObject data, boolean isClient) {
        IFilterProvider dummy = APIHandler.filterTypes.get(type);
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

    @SideOnly(Side.CLIENT)
    public static void cloneTrigger(ICriteria criteria, ITriggerProvider dummy) {
        try {
            ITrigger newTriggerType = dummy.getProvided().getClass().newInstance();
            ITriggerProvider clone = new Trigger(criteria, UUID.randomUUID(), newTriggerType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor(), dummy.isCancelable());
            criteria.getTriggers().add(clone);
            EventsManager.onAdded(newTriggerType);
            if (newTriggerType instanceof IInit) ((IInit) newTriggerType).init(true);

            //Reinit the currently open gui
            APICache.getClientCache().addTrigger(clone);
            CORE.openGui.initData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SideOnly(Side.CLIENT)
    public static void cloneCondition(ITriggerProvider trigger, IConditionProvider dummy) {
        try {
            ICondition newConditionType = dummy.getProvided().getClass().newInstance();
            IConditionProvider clone = new Condition(trigger, UUID.randomUUID(), newConditionType, dummy.getIcon(), dummy.getUnlocalisedName());
            trigger.getConditions().add(clone);
            EventsManager.onAdded(newConditionType);
            if (newConditionType instanceof IInit) ((IInit) newConditionType).init(true);

            //Reinit the currently open gui
            APICache.getClientCache().addCondition(clone);
            CORE.openGui.initData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SideOnly(Side.CLIENT)
    public static void cloneReward(ICriteria criteria, IRewardProvider dummy) {
        try {
            IReward newRewardType = dummy.getProvided().getClass().newInstance();
            IRewardProvider clone = new Reward(criteria, UUID.randomUUID(), newRewardType, dummy.getIcon(), dummy.getUnlocalisedName(), dummy.getColor());
            criteria.getRewards().add(clone);
            EventsManager.onAdded(newRewardType);
            if (newRewardType instanceof IInit) ((IInit) newRewardType).init(true);

            //Reinit the currently open gui
            APICache.getClientCache().addReward(clone);
            CORE.openGui.initData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SideOnly(Side.CLIENT)
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

    public static void removeCriteria(UUID uuid, boolean skipTab) {
        ICriteria c = APICache.getClientCache().getCriteria(uuid);
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
        for (ICriteria require : APICache.getClientCache().getCriteriaSet()) {
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
        APICache.getClientCache().removeCriteria(c);
    }
}
