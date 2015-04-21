package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PacketClaimed implements IMessage, IMessageHandler<PacketClaimed, IMessage> {
    private int x, y, z;
    
    public PacketClaimed() {}
    public PacketClaimed(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }
    
    @Override
    public IMessage onMessage(PacketClaimed message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            ClientHelper.getPlayer().addChatComponentMessage(new ChatComponentText("You have claimed the Tile Entity at " + message.x + " " + message.y + " " + message.z));
        }

        return null;
    }
}
