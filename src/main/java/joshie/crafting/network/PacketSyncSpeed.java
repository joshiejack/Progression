package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.api.CraftingAPI;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncSpeed implements IMessage, IMessageHandler<PacketSyncSpeed, IMessage> {
	private float speed;
    
    public PacketSyncSpeed() {}
    public PacketSyncSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	buf.writeFloat(speed);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	speed = buf.readFloat();
    }
    
    @Override
    public IMessage onMessage(PacketSyncSpeed message, MessageContext ctx) {        
    	CraftingAPI.players.getClientPlayer().setSpeed(message.speed);
        return null;
    }
}
