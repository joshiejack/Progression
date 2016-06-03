package joshie.progression.network;

import joshie.progression.Progression;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.Options;
import joshie.progression.lib.PInfo;
import joshie.progression.network.core.PenguinNetwork;
import joshie.progression.player.PlayerTeam;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PacketHandler {
    private static final PenguinNetwork INSTANCE = new PenguinNetwork(PInfo.MODID);
    public static void registerPacket(Class clazz) {
        registerPacket(clazz, Side.CLIENT);
        registerPacket(clazz, Side.SERVER);

        if (Options.debugMode) {
            Progression.logger.log(Level.INFO, "Registered the packet on both sides: " + clazz);
        }
    }

    public static void registerPacket(Class clazz, Side side) {
        INSTANCE.registerPacket(clazz, side);

        if (Options.debugMode) {
            Progression.logger.log(Level.INFO, "Registered the packet on " + side + " side: " + clazz);
        }
    }

    public static void sendToClient(IMessage message, EntityPlayer player) {
        INSTANCE.sendToClient(message, (EntityPlayerMP) player);
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }

    public static void sendToEveryone(IMessage message) {
        INSTANCE.sendToEveryone(message);
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

    public static void registerPackets(@Nonnull ASMDataTable asmDataTable) {
        String annotationClassName = Packet.class.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<ASMData>(asmDataTable.getAll(annotationClassName));

        topLoop:
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Map<String, Object> data = asmData.getAnnotationInfo();
                boolean isSided = data.get("isSided") != null ? (Boolean) data.get("isSided") : false;
                if (isSided) {
                    String s = ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) data.get("side"), "value");
                    Side side = s.equals("CLIENT") ? Side.CLIENT : Side.SERVER;
                    registerPacket(asmClass, side);
                } else registerPacket(asmClass);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
