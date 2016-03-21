package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import joshie.progression.api.IFilter;
import joshie.progression.api.IHasEventBus;
import joshie.progression.api.ISpecialFilters;
import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.selector.filters.ActionFilter;
import joshie.progression.gui.selector.filters.ItemFilter;
import net.minecraft.item.ItemStack;

public class RewardCraftability extends RewardBaseItemFilter implements ISpecialFieldProvider, ISpecialFilters {
    public static HashSet<IHasEventBus> craftRegistry = new HashSet();
    public List<IFilter> actionfilters = new ArrayList();
    public boolean general = true;
    public boolean usage = true;
    public boolean crafting = true;

    public RewardCraftability() {
        super("crafting", 0xFFCCCCCC);
    }

    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("filters") || name.equals("actionfilters");
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterFieldPreview("filters", this, 0, 30, 2.8F));
            fields.add(new ItemFilterFieldPreview("actionfilters", this, 45, 30, 2.8F));
        }
    }

    @Override
    public IFilterSelectorFilter getFilterForField(String fieldname) {
        return fieldname.equals("actionfilters") ? ActionFilter.INSTANCE : ItemFilter.INSTANCE;
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
            List<ItemStack> matches = filter.getMatches(null);
            if (matches.size() > 0) {
                ItemStack icon = matches.get(0);
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

            CraftingRegistry.addRequirement(type, criteria, filters, usage, crafting, general);
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

            
            CraftingRegistry.removeRequirement(type, criteria, filters, usage, crafting, general);
        }
    }

    @Override
    public void addTooltip(List list) {
        ItemStack stack = preview == null ? BROKEN : preview;
        list.add(stack.getDisplayName());
    }
}
