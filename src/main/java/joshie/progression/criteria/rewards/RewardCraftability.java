package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.filters.FilterTypeAction;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ProgressionRule(name="crafting", color=0xFF660000)
public class RewardCraftability extends RewardBaseItemFilter implements ICustomDescription, ICustomWidth, ICustomTooltip, ISpecialFieldProvider, IAdditionalTooltip<ItemStack> {
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
        list.add(TextFormatting.BLUE + "Action Unlocked!");
        if (actionfilters.size() >= 1) { //Always add the first 5 actions
            for (int i = 0; i < Math.min(5, actionfilters.size()); i++) {
                list.add("• " + ActionType.getCraftingActionFromIcon((ItemStack) actionfilters.get(i).getProvided().getRandom(MCClientHelper.getPlayer())).getDisplayName());
            }

            //Now if we have more than 5 actions, add some extra text
            if (actionfilters.size() > 5) {
                if (GuiScreen.isShiftKeyDown()) {
                    for (int i = 5; i < actionfilters.size(); i++) {
                        list.add("• " + ActionType.getCraftingActionFromIcon((ItemStack) actionfilters.get(i).getProvided().getRandom(MCClientHelper.getPlayer())).getDisplayName());
                    }
                } else list.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + " Hold Shift for list of additional Actions");
            }
        } else list.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + " This reward is broken");

        list.add("------");
        list.add(TextFormatting.GOLD + "Item");
        list.add(TextFormatting.GRAY + " " +  stack.getDisplayName());
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
    public void onAdded(boolean isClient) {
        for (ActionType type : getTypesFromFilter()) {
            IHasEventBus bus = type.getCustomBus();
            if (bus != null) {
                if (craftRegistry.add(bus)) {
                    bus.getEventBus().register(bus);
                }
            }

            CraftingRegistry.get(isClient).addRequirement(type, getProvider().getCriteria(), filters);
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

    @Override
    public void addHoverTooltip(String field, ItemStack stack, List<String> tooltip) {
        if (field.equals("actionfilters")) {
            tooltip.clear(); //How dare you try to display the itemstacks tooltip!
            tooltip.add(ActionType.getCraftingActionFromIcon(stack).getDisplayName());
        }
    }
}
