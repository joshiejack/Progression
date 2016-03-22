package joshie.progression.network;

import java.util.Collections;
import java.util.List;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketClaimReward extends PenguinPacket {
    private IProgressionCriteria criteria;
    private int rewardId;
    private boolean randomReward;

    public PacketClaimReward() {}

    public PacketClaimReward(IProgressionCriteria criteria, int rewardId, boolean randomReward) {
        this.criteria = criteria;
        this.rewardId = rewardId;
        this.randomReward = randomReward;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, criteria.getUniqueName());
        buf.writeInt(rewardId);
        buf.writeBoolean(randomReward);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        criteria = APIHandler.getCriteriaFromName(ByteBufUtils.readUTF8String(buf));
        rewardId = buf.readInt();
        randomReward = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        List<IProgressionReward> rewards = criteria.getRewards();
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

            rewards.get(selected).reward(PlayerHelper.getUUIDForPlayer(player));
        }
    }
}
