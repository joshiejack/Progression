package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.IConditionProvider;
import joshie.progression.handlers.APICache;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

@Packet
public class PacketIsSatisfied extends PenguinPacket {
    private UUID uuid;
    private boolean isTrue;

    public PacketIsSatisfied() {}

    public PacketIsSatisfied(UUID uuid) {
        this.uuid = uuid;
    }

    public PacketIsSatisfied(UUID uuid, boolean isTrue) {
        this.uuid = uuid;
        this.isTrue = isTrue;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeGzipString(buf, uuid.toString());
        buf.writeBoolean(isTrue);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(readGzipString(buf));
        isTrue = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IConditionProvider provider = APICache.getCache(player.worldObj.isRemote).getConditionFromUUID(uuid);
        if (provider != null) {
            if (!player.worldObj.isRemote) {
                boolean isTrue = provider.getProvided().isSatisfied(PlayerTracker.getPlayerData(player).getTeam());
                PacketHandler.sendToClient(new PacketIsSatisfied(uuid, isTrue), player);
            } else provider.setSatisfied(isTrue);
        }
    }
}
