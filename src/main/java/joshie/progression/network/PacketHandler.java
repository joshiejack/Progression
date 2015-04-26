package joshie.progression.network;

import java.util.UUID;

import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ProgressionInfo.MODID);
    private static int id;
    
    public static void registerPacket(Class clazz) {
    	registerPacket(clazz, Side.CLIENT);
    	registerPacket(clazz, Side.SERVER);
    }

    public static void registerPacket(Class clazz, Side side) {
        INSTANCE.registerMessage(clazz, clazz, id++, side);
    }

    public static void sendToEveryone(IMessage packet) {
        INSTANCE.sendToAll(packet);
    }

    public static void sendToClient(IMessage packet, UUID uuid) {
    	EntityPlayerMP player = (EntityPlayerMP) PlayerHelper.getPlayerFromUUID(uuid);
		if (player != null) {
			sendToClient(packet, player);
		}
    }
    
    public static void sendToClient(IMessage packet, EntityPlayerMP player) {
        INSTANCE.sendTo(packet, player);
    }

    public static void sendToServer(IMessage packet) {
        INSTANCE.sendToServer(packet);
    }
}
