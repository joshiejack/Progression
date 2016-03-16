package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.Progression;
import joshie.progression.api.EventBusType;
import joshie.progression.api.IItemFilter;
import joshie.progression.api.ISetterCallback;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public abstract class RewardBaseAction extends RewardBaseItemFilter implements ISetterCallback {
    protected ActionType type = ActionType.CRAFTING;
    public boolean disableUsage = true;
    public boolean disableCrafting = true;

    /** Moving actions to be seperated from one another **/
    public RewardBaseAction(String name, int color) {
        super(name, color);
        list.add(new BooleanField("disableUsage", this));
        list.add(new BooleanField("disableCrafting", this));
        list.add(new ItemFilterField("filters", this));
        list.add(new ItemFilterFieldPreview("filters", this, 25, 30, 26, 70, 25, 75, 2.8F));
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    private boolean isAdded = true;

    @Override
    public void reward(UUID uuid) {
        if (Progression.JEI_LOADED && !isAdded) {
            //JEISupport.itemRegistry.getItemList().add(stack);
            isAdded = true;
        }
    }

    @Override
    public void onAdded() {
        CraftingRegistry.addRequirement(type, criteria, filters, disableUsage, disableCrafting);
    }

    @Override
    public void onRemoved() {
        CraftingRegistry.removeRequirement(type, criteria, filters, disableUsage, disableCrafting);
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        if (fieldName.equals("filters")) {
            onRemoved();
            filters = (List<IItemFilter>) object;
            onAdded();
            return true;
        }

        return false;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Allow " + type.getDisplayName());
        ItemStack stack = preview == null ? BROKEN : preview;
        list.add(stack.getDisplayName());
    }
}
