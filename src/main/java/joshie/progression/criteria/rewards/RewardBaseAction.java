package joshie.progression.criteria.rewards;

import com.google.gson.JsonObject;
import joshie.progression.Progression;
import joshie.progression.api.EventBusType;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.IItemCallback;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.jei.JEISupport;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.UUID;

public abstract class RewardBaseAction extends RewardBase implements IItemCallback {
    public ItemStack stack = new ItemStack(Blocks.furnace);
    public ActionType type = ActionType.CRAFTING;
    public String orename = "IGNORE";
    public boolean matchDamage = true;
    public boolean matchNBT = false;
    public boolean usage = true;
    public boolean crafting = true;
    public String modid = "IGNORE";
    
    /** Moving actions to be seperated from one another **/
    public RewardBaseAction(String name, int color) {
        super(name, color);
        ItemField field = new ItemField("stack", this, 80, 34, 1.4F, 67, 100, 33, 58, Type.REWARD);     
        list.add(new BooleanField("matchDamage", this));
        list.add(new BooleanField("matchNBT", this));
        list.add(new TextField("orename", this));
        list.add(new BooleanField("usage", this));
        list.add(new BooleanField("crafting", this));
        list.add(new TextField("modid", this));
        list.add(field);
    }
    
    @Override
    public EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", stack);
        matchDamage = JSONHelper.getBoolean(data, "matchDamage", matchDamage);
        matchNBT = JSONHelper.getBoolean(data, "matchNBT", matchNBT);
        crafting = JSONHelper.getBoolean(data, "disableCrafting", crafting);
        usage = JSONHelper.getBoolean(data, "disableUsage", usage);
        modid = JSONHelper.getString(data, "modid", modid);

        if (Progression.JEI_LOADED) {
            boolean hide = JSONHelper.getBoolean(data, "hideFromNEI", false);
            if (hide) {
                isAdded = false;
                JEISupport.helpers.getItemBlacklist().addItemToBlacklist(stack);
            }
        }
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
        JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        JSONHelper.setBoolean(data, "matchNBT", matchNBT, false);
        JSONHelper.setBoolean(data, "disableCrafting", crafting, true);
        JSONHelper.setBoolean(data, "disableUsage", usage, true);
        JSONHelper.setString(data, "modid", modid, "IGNORE");
    }

    private boolean isAdded = true;

    @Override
    public void reward(UUID uuid) {
        if (Progression.JEI_LOADED && !isAdded) {
        	JEISupport.itemRegistry.getItemList().add(stack);
            isAdded = true;
        }
    }

    @Override
    public void onAdded() {
        CraftingRegistry.addRequirement(type, modid, stack, orename, matchDamage, matchNBT, usage, crafting, criteria);
    }

    @Override
    public void onRemoved() {
        CraftingRegistry.remove(type, modid, stack, orename, matchDamage, matchNBT, usage, crafting, criteria);
    }

    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public void setItem(String fieldName, ItemStack stack) {
        if (fieldName.equals("stack")) {
            onRemoved();
            this.stack = stack;
            onAdded();
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Allow " + type.getDisplayName());
        list.add(stack.getDisplayName());
    }
}
