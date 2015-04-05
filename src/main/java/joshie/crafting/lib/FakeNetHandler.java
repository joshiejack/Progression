package joshie.crafting.lib;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetAddress;
import java.net.SocketAddress;

import javax.crypto.SecretKey;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** FakeNet from CoFh Core **/

public class FakeNetHandler extends NetHandlerPlayServer {
    public static class NetworkManagerFake extends NetworkManager {
        public NetworkManagerFake() {
            super(false);
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

        public void closeChannel(IChatComponent paramIChatComponent) {}

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

        public IChatComponent getExitMessage() {
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

    public void onNetworkTick() {}

    public void kickPlayerFromServer(String paramString) {}

    public void processInput(C0CPacketInput paramC0CPacketInput) {}

    public void processPlayer(C03PacketPlayer paramC03PacketPlayer) {}

    public void setPlayerLocation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {}

    public void processPlayerDigging(C07PacketPlayerDigging paramC07PacketPlayerDigging) {}

    public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement paramC08PacketPlayerBlockPlacement) {}

    public void onDisconnect(IChatComponent paramIChatComponent) {}

    public void sendPacket(Packet paramPacket) {}

    public void processHeldItemChange(C09PacketHeldItemChange paramC09PacketHeldItemChange) {}

    public void processChatMessage(C01PacketChatMessage paramC01PacketChatMessage) {}

    public void processAnimation(C0APacketAnimation paramC0APacketAnimation) {}

    public void processEntityAction(C0BPacketEntityAction paramC0BPacketEntityAction) {}

    public void processUseEntity(C02PacketUseEntity paramC02PacketUseEntity) {}

    public void processClientStatus(C16PacketClientStatus paramC16PacketClientStatus) {}

    public void processCloseWindow(C0DPacketCloseWindow paramC0DPacketCloseWindow) {}

    public void processClickWindow(C0EPacketClickWindow paramC0EPacketClickWindow) {}

    public void processEnchantItem(C11PacketEnchantItem paramC11PacketEnchantItem) {}

    public void processCreativeInventoryAction(C10PacketCreativeInventoryAction paramC10PacketCreativeInventoryAction) {}

    public void processConfirmTransaction(C0FPacketConfirmTransaction paramC0FPacketConfirmTransaction) {}

    public void processUpdateSign(C12PacketUpdateSign paramC12PacketUpdateSign) {}

    public void processKeepAlive(C00PacketKeepAlive paramC00PacketKeepAlive) {}

    public void processPlayerAbilities(C13PacketPlayerAbilities paramC13PacketPlayerAbilities) {}

    public void processTabComplete(C14PacketTabComplete paramC14PacketTabComplete) {}

    public void processClientSettings(C15PacketClientSettings paramC15PacketClientSettings) {}

    public void processVanilla250Packet(C17PacketCustomPayload paramC17PacketCustomPayload) {}

    public void onConnectionStateTransition(EnumConnectionState paramEnumConnectionState1, EnumConnectionState paramEnumConnectionState2) {}
}
