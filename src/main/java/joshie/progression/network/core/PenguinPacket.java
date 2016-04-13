package joshie.progression.network.core;

import com.google.common.io.CharStreams;
import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class PenguinPacket implements IMessage {
    public abstract void handlePacket(EntityPlayer player);
    
    @Override
    public void toBytes(ByteBuf to) {}

    @Override
    public void fromBytes(ByteBuf from) {}

    public void handleQueuedClient(NetHandlerPlayClient handler) {
        handlePacket(MCClientHelper.getPlayer());
    }

    public void handleQueuedServer(NetHandlerPlayServer serverHandler) {
        handlePacket(serverHandler.playerEntity);
    }

    public static void writeGzipString(ByteBuf buf, String string) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(baos);
            gzip.write(string.getBytes("UTF-8"));
            gzip.close();
            byte[] data = baos.toByteArray();
            buf.writeInt(data.length);
            buf.writeBytes(data);
        } catch (Exception e) {}
    }

    public static String readGzipString(ByteBuf buf) {
        try {
            int length = buf.readInt();
            byte[] data = buf.readBytes(length).array();
            GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data));
            BufferedReader bf = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
            return CharStreams.toString(bf);
        } catch (Exception e) {
            return "";
        }
    }
}
