package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Packet(isSided = true, side = Side.CLIENT)
public class PacketSyncUsernameCache extends PenguinPacket {
    public static HashMap<UUID, String> cache = new HashMap<UUID, String>();
    private Map<UUID, String> map;

    public PacketSyncUsernameCache() {}
    public PacketSyncUsernameCache(Map<UUID, String> map) {
        this.map = map;
    }


    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(map.size());
        for (UUID uuid: map.keySet()) {
            writeGzipString(buf, uuid.toString());
            writeGzipString(buf, map.get(uuid));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        cache = new HashMap<UUID, String>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            cache.put(UUID.fromString(readGzipString(buf)), readGzipString(buf));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {}
}
