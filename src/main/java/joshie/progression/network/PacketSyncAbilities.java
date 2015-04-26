package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.player.DataStats;
import joshie.progression.player.PlayerTracker;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncAbilities implements IMessage, IMessageHandler<PacketSyncAbilities, IMessage> {
	private DataStats abilities;
    
    public PacketSyncAbilities() {}
    public PacketSyncAbilities(DataStats abilities) {
        this.abilities = abilities;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	abilities.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	abilities = new DataStats();
    	abilities.fromBytes(buf);
    }
    
    @Override
    public IMessage onMessage(PacketSyncAbilities message, MessageContext ctx) {        
    	PlayerTracker.getClientPlayer().setAbilities(message.abilities);
        return null;
    }
}
