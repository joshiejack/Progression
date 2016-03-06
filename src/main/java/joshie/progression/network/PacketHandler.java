package joshie.progression.network;

import java.util.UUID;

import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.ProgressionInfo;
import joshie.progression.player.PlayerTeam;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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

    public static void sendToClient(IMessage packet, ICommandSender sender) {
        if (sender instanceof EntityPlayerMP) {
            sendToClient(packet, (EntityPlayerMP)sender);
        }
    }

    public static void sendToClient(IMessage packet, UUID uuid) {
    	EntityPlayerMP player = (EntityPlayerMP) PlayerHelper.getPlayerFromUUID(uuid);
		if (player != null) {
			sendToClient(packet, player);
		}
    }
    
    public static void sendToTeam(IMessage packet, PlayerTeam team) {
        /** Send the stuff to the captain first **/
        EntityPlayerMP owner = (EntityPlayerMP) PlayerHelper.getPlayerFromUUID(team.getOwner());
        if (owner != null) {
            sendToClient(packet, owner);
        }
        
        /** Then send it to all team members **/
        for (UUID uuid: team.getMembers()) {
            EntityPlayerMP member = (EntityPlayerMP) PlayerHelper.getPlayerFromUUID(uuid);
            if (member != null) {
                sendToClient(packet, member);
            }
        }
    }
    
    public static void sendToClient(IMessage packet, EntityPlayerMP player) {
        INSTANCE.sendTo(packet, player);
    }

    public static void sendToServer(IMessage packet) {
        INSTANCE.sendToServer(packet);
    }
}
