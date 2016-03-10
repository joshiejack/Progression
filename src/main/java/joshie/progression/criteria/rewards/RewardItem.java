package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.fields.ItemAmountField;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketRewardItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

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
