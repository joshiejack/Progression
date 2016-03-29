package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.filters.FilterSelectorAction;
import joshie.progression.gui.filters.FilterSelectorItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RewardCraftability extends RewardBaseItemFilter implements ISpecialFieldProvider, ISpecialFilters {
    public static HashSet<IHasEventBus> craftRegistry = new HashSet();
    public List<IProgressionFilter> actionfilters = new ArrayList();
    public boolean general = true;
    public boolean usage = true;
    public boolean crafting = true;

    public RewardCraftability() {
        super("crafting", 0xFFCCCCCC);
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterFieldPreview("filters", this, 0, 30, 2.8F));
            fields.add(new ItemFilterFieldPreview("actionfilters", this, 45, 30, 2.8F));
        }
    }

    @Override
    public IProgressionFilterSelector getFilterForField(String fieldname) {
        return fieldname.equals("actionfilters") ? FilterSelectorAction.INSTANCE : FilterSelectorItem.INSTANCE;
    }
    
    @Override
    public List<IProgressionFilter> getAllFilters() {
        ArrayList<IProgressionFilter> all = new ArrayList();
        all.addAll(filters);
        all.addAll(actionfilters);
        return all;
    }

    public List<ActionType> getTypesFromFilter() {
        List<ActionType> list = new ArrayList();
        for (IProgressionFilter filter : actionfilters) {
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
