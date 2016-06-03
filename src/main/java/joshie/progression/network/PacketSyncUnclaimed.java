package joshie.progression.network;

import com.google.common.collect.HashMultimap;
import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Packet(isSided = true, side = Side.CLIENT)
public class PacketSyncUnclaimed extends PenguinPacket {
    public static class UnclaimedPair {
        public UUID uuid;
        public UUID[] rewardUUIDs;

        public UnclaimedPair(){}
        public UnclaimedPair(UUID uuid, Collection<IRewardProvider> reward) {
            this.uuid = uuid;
            rewardUUIDs = new UUID[reward.size()];
            int i = 0;
            for (IRewardProvider provider: reward) {
                rewardUUIDs[i] = provider.getUniqueID();
                i++;
            }
        }

        public void toBytes(ByteBuf buf) {
            writeGzipString(buf, uuid.toString());
            buf.writeInt(rewardUUIDs.length);
            for (UUID rewardUUID: rewardUUIDs) {
                writeGzipString(buf, rewardUUID.toString());
            }
        }

        public void fromBytes(ByteBuf buf) {
            uuid = UUID.fromString(readGzipString(buf));
            int amount = buf.readInt();
            rewardUUIDs = new UUID[amount];
            for (int i = 0; i < amount; i++) {
                rewardUUIDs[i] = UUID.fromString(readGzipString(buf));
            }
        }
    }

    private boolean remove;
    private boolean overwrite;
    private UnclaimedPair[] data;

    public PacketSyncUnclaimed() {}
    public PacketSyncUnclaimed(HashMultimap<UUID, IRewardProvider> map) {
        this.overwrite = true;
        this.data = new UnclaimedPair[map.size()];
        int position = 0;
        for (UUID uuid: map.keySet()) {
            this.data[position] = new UnclaimedPair(uuid, map.get(uuid));
            position++;
        }
    }

    public PacketSyncUnclaimed(UUID uuid, List<IRewardProvider> rewards) {
        this.overwrite = false;
        this.data = new UnclaimedPair[1];
        this.data[0] = new UnclaimedPair(uuid, rewards);
    }

    public PacketSyncUnclaimed(UUID uuid, IRewardProvider reward) {
        this.overwrite = false;
        this.data = new UnclaimedPair[1];
        HashSet<IRewardProvider> set = new HashSet();
        set.add(reward);
        this.data[0] = new UnclaimedPair(uuid, set);
        this.remove = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(remove);
        buf.writeBoolean(overwrite);
        buf.writeInt(data.length);
        for (UnclaimedPair pair: data) {
            if (pair == null) buf.writeBoolean(false);
            else {
                buf.writeBoolean(true);
                pair.toBytes(buf);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        remove = buf.readBoolean();
        overwrite = buf.readBoolean();
        int size = buf.readInt();
        data = new UnclaimedPair[size];
        for (int i = 0; i < size; i++) {
            if (buf.readBoolean()) {
                data[i] = new UnclaimedPair();
                data[i].fromBytes(buf);
            }
        }
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        PlayerTracker.getClientPlayer().getMappings().setUnclaimedRewards(remove, overwrite, data);
    }
}
