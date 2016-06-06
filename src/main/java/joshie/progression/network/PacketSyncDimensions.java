package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.DimensionHelper;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;

@Packet(isSided = true, side = Side.CLIENT)
public class PacketSyncDimensions extends PenguinPacket {
    private HashMap<Integer, String> map;

    public PacketSyncDimensions() {}
    public PacketSyncDimensions(HashMap<Integer, String> map) {
        this.map = map;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(map.size());
        for (Integer i: map.keySet()) {
            buf.writeInt(i);
            writeGzipString(buf, map.get(i));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        map = new HashMap<Integer, String>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            map.put(buf.readInt(), readGzipString(buf));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        DimensionHelper.setData(map);
    }
}
