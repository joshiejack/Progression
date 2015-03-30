package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.crafting.CraftingRegistry;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardCrafting extends RewardBase implements IItemSelectable {
    private ItemStack stack = new ItemStack(Blocks.furnace);
    private CraftingType type = CraftingType.CRAFTING;
    private boolean matchDamage = true;
    private boolean matchNBT = false;
    private boolean usage = true;
    private boolean crafting = true;

    public RewardCrafting() {
        super("Crafting", 0xFF0085B2, "crafting");
    }

    @Override
    public IReward newInstance() {
        return new RewardCrafting();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardCrafting reward = new RewardCrafting();
        reward.stack = StackHelper.getStackFromString(data.get("item").getAsString());
        if (data.get("craftingType") != null) {
            String craftingtype = data.get("craftingType").getAsString();
            for (CraftingType type : CraftingType.craftingTypes) {
                if (type.name.equalsIgnoreCase(craftingtype)) {
                    reward.type = type;
                    break;
                }
            }
        }

        if (data.get("matchDamage") != null) {
            reward.matchDamage = data.get("matchDamage").getAsBoolean();
        }

        if (data.get("matchNBT") != null) {
            reward.matchNBT = data.get("matchNBT").getAsBoolean();
        }

        if (data.get("disableCrafting") != null) {
            reward.crafting = data.get("disableCrafting").getAsBoolean();
        }

        if (data.get("disableUsage") != null) {
            reward.usage = data.get("disableUsage").getAsBoolean();
        }

        if (CraftingMod.NEI_LOADED) {
            if (data.get("hideFromNEI") != null) {
                if (data.get("hideFromNEI").getAsBoolean() == false) {
                    reward.isAdded = false;
                    API.hideItem(stack);
                }
            }
        }

        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("item", StackHelper.getStringFromStack(stack));
        if (type != CraftingType.CRAFTING) {
            elements.addProperty("craftingType", type.name.toLowerCase());
        }

        if (matchDamage != true) {
            elements.addProperty("matchDamage", matchDamage);
        }

        if (matchNBT != false) {
            elements.addProperty("matchNBT", matchNBT);
        }

        if (crafting != true) {
            elements.addProperty("disableCrafting", false);
        }

        if (usage != true) {
            elements.addProperty("disableUsage", false);
        }
    }

    private boolean isAdded = true;

    @Override
    public void reward(UUID uuid) {
        if (CraftingMod.NEI_LOADED && !isAdded) {
            API.addItemListEntry(stack);
            isAdded = true;
        }
    }

    @Override
    public void onAdded() {
        CraftingAPI.crafting.addRequirement(type, stack, matchDamage, matchNBT, usage, crafting, criteria);
    }

    @Override
    public void onRemoved() {
        CraftingRegistry.remove(type, stack, matchDamage, matchNBT, usage, crafting, criteria);
    }

    //TODO: Replace this with an item which overlay the item
    //With some of crafting representation
    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public Result clicked() {
        if (mouseX >= 77 && mouseX <= 100) {
            if (mouseY >= 43 && mouseY <= 68) {
                SelectItemOverlay.INSTANCE.select(this, Type.REWARD);
                return Result.ALLOW;
            }
        }

        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) type = type == CraftingType.FURNACE ? CraftingType.CRAFTING : CraftingType.FURNACE;
            if (mouseY > 25 && mouseY <= 33) matchDamage = !matchDamage;
            if (mouseY > 34 && mouseY <= 41) matchNBT = !matchNBT;
            if (mouseY > 42 && mouseY <= 50) usage = !usage;
            if (mouseY > 50 && mouseY <= 57) crafting = !crafting;
            if (mouseY >= 17 && mouseY <= 57) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        drawStack(getIcon(), 76, 44, 1.4F);
        int typeColor = 0xFF000000;
        int matchColor = 0xFF000000;
        int match2Color = 0xFF000000;
        int usageColor = 0xFF000000;
        int craftColor = 0xFF000000;
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) typeColor = 0xFFBBBBBB;
            if (mouseY > 25 && mouseY <= 33) matchColor = 0xFFBBBBBB;
            if (mouseY > 34 && mouseY <= 41) match2Color = 0xFFBBBBBB;
            if (mouseY > 42 && mouseY <= 50) usageColor = 0xFFBBBBBB;
            if (mouseY > 50 && mouseY <= 57) craftColor = 0xFFBBBBBB;
        }

        drawText("type: " + type.name.toLowerCase(), 4, 18, typeColor);
        drawText("matchDamage: " + matchDamage, 4, 26, matchColor);
        drawText("matchNBT: " + matchNBT, 4, 34, match2Color);
        drawText("usage: " + usage, 4, 42, usageColor);
        drawText("crafting: " + crafting, 4, 50, craftColor);
    }

    @Override
    public void setItemStack(ItemStack stack) {
        onRemoved();
        this.stack = stack;
        onAdded();
    }
}
