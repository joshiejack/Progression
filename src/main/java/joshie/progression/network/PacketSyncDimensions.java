package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.DimensionHelper;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class PacketSyncDimensions extends PenguinPacket {
    private int[] ids;
    private String[] dimensions;

    public PacketSyncDimensions() {}
    public PacketSyncDimensions(HashMap<Integer, String> map) {
        this.ids = new int[map.size()];
        this.dimensions = new String[map.size()];
        int i = 0;
        for (Integer j: map.keySet()) {
            this.ids[i] = j;
            this.dimensions[i] = map.get(i);
            i++;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ids.length);
        for (int i = 0; i < ids.length; i++) {
            buf.writeInt(ids[i]);
            writeGzipString(buf, dimensions[i]);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        ids = new int[size];
        dimensions = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = buf.readInt();
            dimensions[i] = readGzipString(buf);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        DimensionHelper.setData(ids, dimensions);
    }
}
