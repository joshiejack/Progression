package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.rewards.RewardCrafting;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncCriteria implements IMessage, IMessageHandler<PacketSyncCriteria, IMessage> {
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
    	for (ICriteria tech: criteria) {
            ByteBufUtils.writeUTF8String(buf, tech.getUniqueName());
    	}
    	
    	for (Integer i: integers) {
    		buf.writeInt(i);
    	}
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	overwrite = buf.readBoolean();
    	int size = buf.readInt();
    	criteria = new ICriteria[size];
    	for (int i = 0; i < size; i++) {
    		criteria[i] = CraftingAPI.registry.getCriteriaFromName(ByteBufUtils.readUTF8String(buf));
    	}
    	
    	integers = new Integer[size];
    	for (int i = 0; i < size; i++) {
    		integers[i] = buf.readInt();
    	}
    }
    
    @Override
    public IMessage onMessage(PacketSyncCriteria message, MessageContext ctx) {    
    	CraftingAPI.players.getClientPlayer().getMappings().markCriteriaAsCompleted(message.overwrite, message.integers, message.criteria);
        if (message.overwrite) {
        	for (ICriteria condition: CraftAPIRegistry.criteria.values()) {
        		for (ICriteria unlocked: message.criteria) {
        		    if (unlocked == null) continue;
        			for (IReward reward: unlocked.getRewards()) {
        				if (reward instanceof RewardCrafting) {
        					reward.reward(CraftingAPI.players.getClientPlayer().getUUID());
        				}
        			}
        		}
        	}
        }
    	
    	return null;
    }
}
