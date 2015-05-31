package joshie.progression.network;

import joshie.progression.Progression;
import joshie.progression.helpers.ClientHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenEditor extends PacketAction implements IMessageHandler<PacketOpenEditor, IMessage> {
    @Override
    public IMessage onMessage(PacketOpenEditor message, MessageContext ctx) {
        ClientHelper.getPlayer().openGui(Progression.instance, 0, null, 0, 0, 0);
        return null;
    }
}
