package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.special.IRequestItem;
import joshie.progression.handlers.APIHandler;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class PacketRequestItem extends PenguinPacket {
    private UUID uuid;
    private ItemStack stack;

    public PacketRequestItem() {}

    public PacketRequestItem(UUID uuid, ItemStack stack) {
        this.uuid = uuid;
        this.stack = stack;
    }

    public PacketRequestItem(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
        buf.writeBoolean(stack != null);
        if (stack != null) ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        if (buf.readBoolean()) {
            stack = ByteBufUtils.readItemStack(buf);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IRewardProvider provider = APIHandler.getCache(player.worldObj.isRemote).getRewardFromUUID(uuid);
        if (provider.getProvided() instanceof IRequestItem) {
            IRequestItem request = ((IRequestItem)provider.getProvided());
            if (player.worldObj.isRemote) PacketHandler.sendToServer(new PacketRequestItem(uuid, request.getRequestedStack()));
            else request.reward(player, stack);
        }
    }
}
