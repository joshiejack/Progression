package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;

@Packet(isSided = true, side = Side.CLIENT)
public class PacketClaimed extends PenguinPacket {
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
    public void handlePacket(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            MCClientHelper.getPlayer().addChatComponentMessage(new ChatComponentText("You have claimed the Tile Entity at " + x + " " + y + " " + z));
        }
    }
}
