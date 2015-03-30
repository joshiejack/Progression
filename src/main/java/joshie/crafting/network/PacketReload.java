package joshie.crafting.network;

import joshie.crafting.CraftingMod;
import joshie.crafting.CraftingRemapper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketReload extends PacketAction implements IMessageHandler<PacketReload, IMessage> {
	@Override
	public IMessage onMessage(PacketReload message, MessageContext ctx) {
	    if (CraftingMod.options.editor) {
    		//Perform a reset of all the data serverside
    		CraftingRemapper.reloadServerData();
	    }
	    
		return null;
	}
}
