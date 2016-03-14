package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.api.IItemFilter;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.ItemHelper;
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

//TODO!!!!!!!!!!!
public class RewardPotion extends RewardBaseItemFilter {
    private static final ItemStack BROKEN = new ItemStack(Items.baked_potato);
    public List<IItemFilter> filters = new ArrayList();
    public int stackSize = 1;
    private ItemStack preview;
    private int ticker;

    public RewardPotion() {
        super("item", 0xFFE599FF);
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
            ItemStack stack = ItemHelper.getRandomItem(filters, false).copy();
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
