package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.*;
import joshie.progression.api.special.*;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.filters.FilterTypeAction;
import joshie.progression.gui.filters.FilterTypeItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ProgressionRule(name="crafting", color=0xFF660000)
public class RewardCraftability extends RewardBaseItemFilter implements ICustomDescription, ICustomWidth, ICustomTooltip, ISpecialFieldProvider {
    private static final HashSet<IHasEventBus> craftRegistry = new HashSet();
    public List<IFilterProvider> actionfilters = new ArrayList();

    protected transient IField field;

    public RewardCraftability() {
        field = new ItemFilterField("actionfilters", this);
    }

    @Override
    public String getDescription() {
        return (String) field.getField();
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 85 : 100;
    }

    @Override
    public void addTooltip(List list) {
        ItemStack stack = preview == null ? BROKEN : preview;
        list.add(stack.getDisplayName());
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if(mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterFieldPreview("filters", this, 5, 44, 1.9F));
            fields.add(new ItemFilterFieldPreview("actionfilters", this, 45, 44, 1.9F));
        } else fields.add(new ItemFilterFieldPreview("filters", this, 35, 40, 2F));
    }

    @Override
    public List<IFilterProvider> getAllFilters() {
        ArrayList<IFilterProvider> all = new ArrayList();
        all.addAll(filters);
        all.addAll(actionfilters);
        return all;
    }

    @Override
    public IFilterType getFilterForField(String fieldname) {
        return fieldname.equals("actionfilters") ? FilterTypeAction.INSTANCE : FilterTypeItem.INSTANCE;
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

            CraftingRegistry.addRequirement(type, getProvider().getCriteria(), filters);
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

            
            CraftingRegistry.removeRequirement(type, getProvider().getCriteria(), filters);
        }
    }

    //Helped methods
    private List<ActionType> getTypesFromFilter() {
        List<ActionType> list = new ArrayList();
        for (IFilterProvider filter : actionfilters) {
            if (filter.getProvided().getType() == FilterTypeAction.INSTANCE) {
                ItemStack icon = (ItemStack) filter.getProvided().getRandom(null);
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
}
