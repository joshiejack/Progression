package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Reward;
import joshie.progression.criteria.rewards.RewardBaseAction;
import joshie.progression.handlers.APIHandler;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSyncCriteria extends PenguinPacket {
	private Criteria[] criteria;
	private Integer[] integers;
	private boolean overwrite;
    
    public PacketSyncCriteria() {}
    public PacketSyncCriteria(boolean overwrite, Integer[] values, Criteria[] criteria) {
    	this.criteria = criteria;
    	this.integers = values;
    	this.overwrite = overwrite;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	buf.writeBoolean(overwrite);
    	buf.writeInt(criteria.length);
    	for (Criteria tech: criteria) {
            ByteBufUtils.writeUTF8String(buf, tech.uniqueName);
    	}
    	
    	for (Integer i: integers) {
    		buf.writeInt(i);
    	}
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	overwrite = buf.readBoolean();
    	int size = buf.readInt();
    	criteria = new Criteria[size];
    	for (int i = 0; i < size; i++) {
    		criteria[i] = APIHandler.getCriteriaFromName(ByteBufUtils.readUTF8String(buf));
    	}
    	
    	integers = new Integer[size];
    	for (int i = 0; i < size; i++) {
    		integers[i] = buf.readInt();
    	}
    }
    
    @Override
	public void handlePacket(EntityPlayer player) {   
        PlayerTracker.getClientPlayer().getMappings().markCriteriaAsCompleted(overwrite, integers, criteria);
        if (overwrite) {
        	for (Criteria condition: APIHandler.criteria.values()) {
        		for (Criteria unlocked: criteria) {
        		    if (unlocked == null) continue;
        			for (Reward reward: unlocked.rewards) {
        				if (reward.getType() instanceof RewardBaseAction) {
        					reward.getType().reward(PlayerTracker.getClientPlayer().getUUID());
        				}
        			}
        		}
        	}
        }
    }
}
