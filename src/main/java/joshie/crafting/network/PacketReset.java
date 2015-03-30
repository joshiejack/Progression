package joshie.crafting.network;

import joshie.crafting.CraftingMod;
import joshie.crafting.CraftingRemapper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketReset extends PacketAction implements IMessageHandler<PacketReset, IMessage> {
    @Override
    public IMessage onMessage(PacketReset message, MessageContext ctx) {
        if (CraftingMod.options.editor) {
            CraftingMod.instance.createWorldData(); //Recreate the world data, Wiping out any saved information for players
            CraftingRemapper.reloadServerData();
        }
        
        return null;
    }
}
