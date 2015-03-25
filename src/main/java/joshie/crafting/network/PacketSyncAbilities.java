package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.player.DataAbilities;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncAbilities implements IMessage, IMessageHandler<PacketSyncAbilities, IMessage> {
	private DataAbilities abilities;
    
    public PacketSyncAbilities() {}
    public PacketSyncAbilities(DataAbilities abilities) {
        this.abilities = abilities;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	abilities.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	abilities = new DataAbilities();
    	abilities.fromBytes(buf);
    }
    
    @Override
    public IMessage onMessage(PacketSyncAbilities message, MessageContext ctx) {        
    	CraftingAPI.players.getClientPlayer().setAbilities(message.abilities);
        return null;
    }
}
