package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.filters.FilterTypeAction;
import joshie.progression.gui.filters.FilterTypeItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RewardCraftability extends RewardBaseItemFilter implements ISpecialFieldProvider, ISpecialFilters {
    public static HashSet<IHasEventBus> craftRegistry = new HashSet();
    public List<IFilter> actionfilters = new ArrayList();
    protected IFilter actionpreview;
    protected int actionticker;

    public RewardCraftability() {
        super("crafting", 0xFFCCCCCC);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if(mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterFieldPreview("filters", this, 5, 44, 1.9F));
            fields.add(new ItemFilterFieldPreview("actionfilters", this, 45, 44, 1.9F));
        } else {
            fields.add(new ItemFilterFieldPreview("filters", this, 35, 40, 2F));
            //fields.add(new ItemFilterFieldPreview("actionfilters", this, 60, 44, 1.9F));
        }
    }

    @Override
    public IFilterType getFilterForField(String fieldname) {
        return fieldname.equals("actionfilters") ? FilterTypeAction.INSTANCE : FilterTypeItem.INSTANCE;
    }
    
    @Override
    public List<IFilter> getAllFilters() {
        ArrayList<IFilter> all = new ArrayList();
        all.addAll(filters);
        all.addAll(actionfilters);
        return all;
    }

    public List<ActionType> getTypesFromFilter() {
        List<ActionType> list = new ArrayList();
        for (IFilter filter : actionfilters) {
            if (filter.getType() == FilterTypeAction.INSTANCE) {
                ItemStack icon = (ItemStack) filter.getRandom(null);
                if (icon != null) {
                    ActionType type = null;
                    for (ActionType action : ActionType.values()) {
                        if (action.getIcon().getItem() == icon.getItem() && action.getIcon().getItemDamage() == icon.getItemDamage()) {
                            type = action;
                            break;
                        }
                    }

                    if (type != null) list.add(type);
                }
            }
        }

        return list;
    }

    @Override
    public void onAdded() {
        for (ActionType type : getTypesFromFilter()) {
            IHasEventBus bus = type.getCustomBus();
            if (bus != null) {
                if (craftRegistry.add(bus)) {
                    bus.getEventBus().register(bus);
                }
            }

            CraftingRegistry.addRequirement(type, criteria, filters);
        }
    }

    @Override
    public void onRemoved() {
        for (ActionType type : getTypesFromFilter()) {
            IHasEventBus bus = type.getCustomBus();
            if (bus != null) {
                if (craftRegistry.remove(bus)) {
                    bus.getEventBus().unregister(bus);
                }
            }

            
            CraftingRegistry.removeRequirement(type, criteria, filters);
        }
    }

    @Override
    public void addTooltip(List list) {
        ItemStack stack = preview == null ? BROKEN : preview;
        list.add(stack.getDisplayName());
    }

    public String getFilter() {
        if (actionticker == 0 || actionticker >= 400) {
            actionpreview = FilterBase.getRandomFilterFromFilters(actionfilters);
            actionticker = 1;
        }

        if (!GuiScreen.isShiftKeyDown())  actionticker++;

        return actionpreview == null ? "": actionpreview.getDescription();
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 85 : 100;
    }

    @Override
    public String getDescription() {
        return getFilter();
    }
}
