package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncTriggers implements IMessage, IMessageHandler<PacketSyncTriggers, IMessage> {
	private ITrigger[] researches;
	private boolean overwrite;
    
    public PacketSyncTriggers() {}
    public PacketSyncTriggers(boolean overwrite, ITrigger... researches) {
    	this.researches = researches;
    	this.overwrite = overwrite;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	buf.writeBoolean(overwrite);
    	buf.writeInt(researches.length);
    	for (ITrigger tech: researches) {
            ByteBufUtils.writeUTF8String(buf, tech.getUniqueName());
    	}
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	overwrite = buf.readBoolean();
    	int size = buf.readInt();
    	researches = new ITrigger[size];
    	for (int i = 0; i < size; i++) {
    		researches[i] = CraftingAPI.registry.getTrigger(null, ByteBufUtils.readUTF8String(buf), null);
    	}
    }
    
    @Override
    public IMessage onMessage(PacketSyncTriggers message, MessageContext ctx) {        
    	CraftingAPI.players.getClientPlayer().getMappings().markTriggerAsCompleted(message.overwrite, message.researches);
        return null;
    }
}
