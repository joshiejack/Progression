package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IResearch;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncResearch implements IMessage, IMessageHandler<PacketSyncResearch, IMessage> {
	private IResearch[] researches;
	private boolean overwrite;
    
    public PacketSyncResearch() {}
    public PacketSyncResearch(boolean overwrite, IResearch... researches) {
    	this.researches = researches;
    	this.overwrite = overwrite;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	buf.writeBoolean(overwrite);
    	buf.writeInt(researches.length);
    	for (IResearch tech: researches) {
            ByteBufUtils.writeUTF8String(buf, tech.getName());
    	}
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	overwrite = buf.readBoolean();
    	int size = buf.readInt();
    	researches = new IResearch[size];
    	for (int i = 0; i < size; i++) {
    		researches[i] = CraftingAPI.registry.getResearchFromName(ByteBufUtils.readUTF8String(buf));
    	}
    }
    
    @Override
    public IMessage onMessage(PacketSyncResearch message, MessageContext ctx) {        
    	if (!message.overwrite) {
    		CraftingAPI.players.getClientPlayer().unlock(message.researches);
    	} else CraftingAPI.players.getClientPlayer().setResearch(message.researches);
        return null;
    }
}
