package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.IReward;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.helpers.SpawnItemHelper;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardItem extends RewardBase implements IItemSelectable, ITextEditable {
    private ItemStack stack = new ItemStack(Items.diamond);

    public RewardItem() {
        super("Give Item", 0xFFE599FF, "item");
    }

    @Override
    public IReward newInstance() {
        return new RewardItem();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardItem reward = new RewardItem();
        reward.stack = StackHelper.getStackFromString(data.get("item").getAsString());
        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("item", StackHelper.getStringFromStack(stack));
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            SpawnItemHelper.addToPlayerInventory(player, stack.copy());
        }
    }

    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public Result clicked() {
        if (mouseX >= 10 && mouseX <= 100) {
            if (mouseY >= 30 && mouseY <= 100) {
                SelectItemOverlay.INSTANCE.select(this, Type.REWARD);
                return Result.ALLOW;
            }
        }

        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        drawStack(getIcon(), 27, 27, 2.5F);
        int typeColor = 0xFFFFFFFF;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) typeColor = 0xFFBBBBBB;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            drawText("amount: " + SelectTextEdit.INSTANCE.getText(), 4, 18, typeColor);
        } else drawText("amount: " + getTextField(), 4, 18, typeColor);
    }

    @Override
    public void setItemStack(ItemStack stack) {
        int amount = this.stack.stackSize;
        this.stack = stack;
        this.stack.stackSize = amount;
    }

    private String textField;

    @Override
    public String getTextField() {
        if (textField == null) {
            textField = "" + stack.stackSize;
        }

        return textField;
    }

    @Override
    public void setTextField(String text) {
        String fixed = text.replaceAll("[^0-9]", "");
        this.textField = fixed;
        int size = 0;

        try {
            size = Integer.parseInt(textField);
        } catch (Exception e) {
            size = 1;
        }

        size = Math.max(1, size);
        stack.stackSize = size;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Free Item");
        list.add(stack.getDisplayName() + " x" + stack.stackSize);
    }
}
