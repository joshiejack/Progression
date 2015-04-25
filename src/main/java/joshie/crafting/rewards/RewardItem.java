package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.TextList.ItemAmountField;
import joshie.crafting.gui.TextList.ItemField;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.helpers.SpawnItemHelper;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketRewardItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

public class RewardItem extends RewardBase {
    public ItemStack stack = new ItemStack(Items.diamond);

    public RewardItem() {
        super("item", 0xFFE599FF);
        ItemField field = new ItemField("stack", this, 27, 27, 2.5F, 10, 100, 30, 100, Type.REWARD);
        list.add(new ItemAmountField("stack", "stack", field));
        list.add(field);
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
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Free Item");
        list.add(stack.getDisplayName() + " x" + stack.stackSize);
    }
}
