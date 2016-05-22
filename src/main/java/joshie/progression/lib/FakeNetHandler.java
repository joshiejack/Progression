package joshie.progression.lib;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.crypto.SecretKey;
import java.net.InetAddress;
import java.net.SocketAddress;

/** FakeNet from CoFh Core **/

public class FakeNetHandler extends NetHandlerPlayServer {
    public static class NetworkManagerFake extends NetworkManager {
        public NetworkManagerFake() {
            super(EnumPacketDirection.SERVERBOUND);
        }

        public void channelActive(ChannelHandlerContext paramChannelHandlerContext) throws Exception {}

        public void setConnectionState(EnumConnectionState paramEnumConnectionState) {}

        public void channelInactive(ChannelHandlerContext paramChannelHandlerContext) {}

        public void exceptionCaught(ChannelHandlerContext paramChannelHandlerContext, Throwable paramThrowable) {}

        public void setNetHandler(INetHandler paramINetHandler) {}

        public void scheduleOutboundPacket(Packet paramPacket, GenericFutureListener... paramVarArgs) {}

        public void processReceivedPackets() {}

        public SocketAddress getSocketAddress() {
            return null;
        }

        public void closeChannel(ITextComponent paramIChatComponent) {}

        public boolean isLocalChannel() {
            return false;
        }

        @SideOnly(Side.CLIENT)
        public static NetworkManager provideLanClient(InetAddress paramInetAddress, int paramInt) {
            return null;
        }

        @SideOnly(Side.CLIENT)
        public static NetworkManager provideLocalClient(SocketAddress paramSocketAddress) {
            return null;
        }

        public void enableEncryption(SecretKey paramSecretKey) {}

        public boolean isChannelOpen() {
            return false;
        }

        public INetHandler getNetHandler() {
            return null;
        }

        public ITextComponent getExitMessage() {
            return null;
        }

        public void disableAutoRead() {}

        public Channel channel() {
            return null;
        }
    }

    public FakeNetHandler(MinecraftServer paramMinecraftServer, EntityPlayerMP paramEntityPlayerMP) {
        super(paramMinecraftServer, new NetworkManagerFake(), paramEntityPlayerMP);
    }

    public void update() {}

    public void kickPlayerFromServer(String paramString) {}

    public void processInput(CPacketInput paramC0CPacketInput) {}

    public void processPlayer(CPacketPlayer paramC03PacketPlayer) {}

    public void setPlayerLocation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {}

    public void processPlayerDigging(CPacketPlayerDigging paramC07PacketPlayerDigging) {}

    public void processPlayerBlockPlacement(CPacketPlayerTryUseItem p_processPlayerBlockPlacement_1_) {}

    public void onDisconnect(ITextComponent paramIChatComponent) {}

    public void sendPacket(Packet paramPacket) {}

    public void processHeldItemChange(CPacketHeldItemChange paramC09PacketHeldItemChange) {}

    public void processChatMessage(CPacketChatMessage paramC01PacketChatMessage) {}

    public void handleAnimation(CPacketAnimation paramC0APacketAnimation) {}

    public void processEntityAction(CPacketEntityAction paramC0BPacketEntityAction) {}

    public void processUseEntity(CPacketUseEntity paramC02PacketUseEntity) {}

    public void processClientStatus(CPacketClientStatus paramC16PacketClientStatus) {}

    public void processCloseWindow(CPacketCloseWindow paramC0DPacketCloseWindow) {}

    public void processClickWindow(CPacketClickWindow paramC0EPacketClickWindow) {}

    public void processEnchantItem(CPacketEnchantItem paramC11PacketEnchantItem) {}

    public void processCreativeInventoryAction(CPacketCreativeInventoryAction paramC10PacketCreativeInventoryAction) {}

    public void processConfirmTransaction(CPacketConfirmTransaction paramC0FPacketConfirmTransaction) {}

    public void processUpdateSign(CPacketUpdateSign paramC12PacketUpdateSign) {}

    public void processKeepAlive(CPacketKeepAlive paramC00PacketKeepAlive) {}

    public void processPlayerAbilities(CPacketPlayerAbilities paramC13PacketPlayerAbilities) {}

    public void processTabComplete(CPacketTabComplete paramC14PacketTabComplete) {}

    public void processClientSettings(CPacketClientSettings paramC15PacketClientSettings) {}

    public void processVanilla250Packet(CPacketCustomPayload paramC17PacketCustomPayload) {}

    public void onConnectionStateTransition(EnumConnectionState paramEnumConnectionState1, EnumConnectionState paramEnumConnectionState2) {}
}
