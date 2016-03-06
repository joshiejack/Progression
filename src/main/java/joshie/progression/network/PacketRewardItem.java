package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketRewardItem extends PenguinPacket {
    private ItemStack stack;

    public PacketRewardItem() {}

    public PacketRewardItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
	public void handlePacket(EntityPlayer player) {
        SpawnItemHelper.addToPlayerInventory(MCClientHelper.getPlayer(), stack);
    }
}
