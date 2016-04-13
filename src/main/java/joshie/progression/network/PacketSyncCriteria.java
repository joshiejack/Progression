package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PacketSyncCriteria extends PenguinPacket {
    private ICriteria[] criteria;
    private Integer[] integers;
    private boolean overwrite;

    public PacketSyncCriteria() {}

    public PacketSyncCriteria(boolean overwrite, Integer[] values, ICriteria[] criteria) {
        this.criteria = criteria;
        this.integers = values;
        this.overwrite = overwrite;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(overwrite);
        buf.writeInt(criteria.length);
        for (ICriteria tech : criteria) {
            writeGzipString(buf, tech.getUniqueID().toString());
        }

        for (Integer i : integers) {
            buf.writeInt(i);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        overwrite = buf.readBoolean();
        int size = buf.readInt();
        criteria = new ICriteria[size];
        for (int i = 0; i < size; i++) {
            criteria[i] = APIHandler.getCache(true).getCriteria().get(UUID.fromString(readGzipString(buf)));
        }

        integers = new Integer[size];
        for (int i = 0; i < size; i++) {
            integers[i] = buf.readInt();
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        PlayerTracker.getClientPlayer().getMappings().markCriteriaAsCompleted(overwrite, integers, criteria);
    }
}
