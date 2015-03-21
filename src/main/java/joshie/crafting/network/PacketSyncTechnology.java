package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.tech.ITechnology;
import joshie.crafting.data.DataClient;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncTechnology implements IMessage, IMessageHandler<PacketSyncTechnology, IMessage> {
	private ITechnology[] techs;
    
    public PacketSyncTechnology() {}
    public PacketSyncTechnology(ITechnology... techs) {
        this.techs = techs;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	buf.writeInt(techs.length);
    	for (ITechnology tech: techs) {
            ByteBufUtils.writeUTF8String(buf, tech.getName());
    	}
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	int size = buf.readInt();
    	techs = new ITechnology[size];
    	for (int i = 0; i < size; i++) {
    		techs[i] = CraftingAPI.tech.getTechnologyFromName(ByteBufUtils.readUTF8String(buf));
    	}
    }
    
    @Override
    public IMessage onMessage(PacketSyncTechnology message, MessageContext ctx) {        
        DataClient.INSTANCE.setUnlocked(message.techs);
        return null;
    }
}
