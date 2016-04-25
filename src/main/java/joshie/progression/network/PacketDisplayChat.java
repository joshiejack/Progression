package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

public class PacketDisplayChat extends PenguinPacket {
    private String text;

    public PacketDisplayChat() {}
    public PacketDisplayChat(String text) {
        this.text = text;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeGzipString(buf, text);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = readGzipString(buf);
    }

    @Override
    public void handlePacket(EntityPlayer sender) {
        ChatHelper.displayChat(text);
    }
}
