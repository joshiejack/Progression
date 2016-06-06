package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.handlers.APICache;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Packet(isSided = true, side = Side.SERVER)
public class PacketClaimReward extends PenguinPacket {
    private ICriteria criteria;
    private int rewardId;
    private boolean randomReward;

    public PacketClaimReward() {}

    public PacketClaimReward(ICriteria criteria, int rewardId, boolean randomReward) {
        this.criteria = criteria;
        this.rewardId = rewardId;
        this.randomReward = randomReward;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, criteria.getUniqueID().toString());
        buf.writeInt(rewardId);
        buf.writeBoolean(randomReward);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        criteria = APICache.getServerCache().getCriteria(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
        rewardId = buf.readInt();
        randomReward = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        List<IRewardProvider> rewards = criteria.getRewards();
        if (rewards != null && rewards.size() > 0) {
            if (randomReward) Collections.shuffle(rewards);
            int selected = 0;
            //Looping around to avoid getting wrong values
            for (int i = 0; i < rewards.size(); i++) {
                if (i == rewardId) {
                    selected = i;
                    break;
                }
            }

            rewards.get(selected).getProvided().reward((EntityPlayerMP)player);
        }
    }
}
