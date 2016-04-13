package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PacketSyncImpossible extends PenguinPacket {
    private ICriteria[] criteria;

    public PacketSyncImpossible() {}

    public PacketSyncImpossible(ICriteria[] criteria) {
        this.criteria = criteria;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(criteria.length);
        for (ICriteria tech : criteria) {
            writeGzipString(buf, tech.getUniqueID().toString());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        criteria = new ICriteria[size];
        for (int i = 0; i < size; i++) {
            criteria[i] = APIHandler.getCache(true).getCriteria().get(UUID.fromString(readGzipString(buf)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        PlayerTracker.getClientPlayer().getMappings().setImpossibles(criteria);
    }
}
