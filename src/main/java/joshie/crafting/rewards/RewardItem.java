package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.helpers.SpawnItemHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketRewardItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardItem extends RewardBase implements IItemSelectable, ITextEditable {
    private ItemStack stack = new ItemStack(Items.diamond);

    public RewardItem() {
        super("item", 0xFFE599FF);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", stack);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            PacketHandler.sendToClient(new PacketRewardItem(stack.copy()), (EntityPlayerMP)player);
            SpawnItemHelper.addToPlayerInventory(player, stack.copy());
        }
    }

    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
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
    public void draw(int mouseX, int mouseY) {
        DrawHelper.drawStack(getIcon(), 27, 27, 2.5F);
        int typeColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) typeColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            DrawHelper.drawText("amount: " + SelectTextEdit.INSTANCE.getText(), 4, 18, typeColor);
        } else DrawHelper.drawText("amount: " + getTextField(), 4, 18, typeColor);
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
