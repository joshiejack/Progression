package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.handlers.APICache;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.CriteriaMappings;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class PacketSelectRewards extends PenguinPacket {
    private Set<IRewardProvider> rewards;

    public PacketSelectRewards() {}

    public PacketSelectRewards(Set<IRewardProvider> rewards) {
        this.rewards = rewards;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(rewards.size());
        for (IRewardProvider provider: rewards) {
            writeGzipString(buf, provider.getUniqueID().toString());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        rewards = new LinkedHashSet();
        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            rewards.add(APICache.getCache(false).getRewardFromUUID(UUID.fromString(readGzipString(buf))));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        for (IRewardProvider reward: rewards) {
            CriteriaMappings mappings = PlayerTracker.getServerPlayer(player).getMappings();
            if (mappings.claimReward((EntityPlayerMP) player, reward)) {
                mappings.remapAfterClaiming(reward.getCriteria());
            }
        }
    }
}
