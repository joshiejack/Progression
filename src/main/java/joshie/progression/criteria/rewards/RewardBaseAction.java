package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.Progression;
import joshie.progression.api.EventBusType;
import joshie.progression.api.IItemFilter;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.IItemFilterSetterCallback;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public abstract class RewardBaseAction extends RewardBaseItemFilter implements IItemFilterSetterCallback {
    public ActionType type = ActionType.CRAFTING;
    public boolean usage = true;
    public boolean crafting = true;

    /** Moving actions to be seperated from one another **/
    public RewardBaseAction(String name, int color) {
        super(name, color);
        list.add(new BooleanField("usage", this));
        list.add(new BooleanField("crafting", this));
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        crafting = JSONHelper.getBoolean(data, "disableCrafting", crafting);
        usage = JSONHelper.getBoolean(data, "disableUsage", usage);
        if (Progression.JEI_LOADED) {
            boolean hide = JSONHelper.getBoolean(data, "hideFromNEI", false);
            if (hide) {
                isAdded = false;
                //JEISupport.helpers.getItemBlacklist().addItemToBlacklist(stack);
            }
        }
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setBoolean(data, "disableCrafting", crafting, true);
        JSONHelper.setBoolean(data, "disableUsage", usage, true);
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
        CraftingRegistry.addRequirement(type, criteria, filters, usage, crafting);
    }

    @Override
    public void onRemoved() {
        CraftingRegistry.removeRequirement(type, criteria, filters, usage, crafting);
    }

    @Override
    public void setFilter(String fieldName, List<IItemFilter> list) {
        onRemoved();
        filters = list;
        onAdded();
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Allow " + type.getDisplayName());
        ItemStack stack = preview == null ? BROKEN : preview;
        list.add(stack.getDisplayName());
    }
}
