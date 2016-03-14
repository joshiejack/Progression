package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketRewardItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class RewardItem extends RewardBaseItemFilter {
    public int stackSize = 1;
    
    public RewardItem() {
        super("item", 0xFFE599FF);
        list.add(new ItemFilterField("filters", this));
        list.add(new ItemFilterFieldPreview("filters", this, false, 25, 30, 26, 70, 25, 75, 2.8F));
        list.add(new TextField("stackSize", this));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        stackSize = JSONHelper.getInteger(data, "stackSize", 1);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setInteger(data, "stackSize", stackSize, 1);
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            ItemStack stack = ItemHelper.getRandomItem(filters).copy();
            stack.stackSize = stackSize;
            
            PacketHandler.sendToClient(new PacketRewardItem(stack.copy()), (EntityPlayerMP)player);
            SpawnItemHelper.addToPlayerInventory(player, stack.copy());
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Free Item");
        ItemStack stack = preview == null ? BROKEN: preview;;
        list.add(stack.getDisplayName() + " x" + stackSize);
    }
}
