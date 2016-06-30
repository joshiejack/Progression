package joshie.progression.network;

import com.google.common.collect.Lists;
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
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.*;

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
        EntityPlayerMP player = (EntityPlayerMP) PlayerHelper.getPlayerFromUUID(false, uuid);
        if (player != null) {
            sendToClient(packet, player);
        }
    }

    public static void sendToTeam(IMessage packet, PlayerTeam team) {
        /** Send the stuff to the captain first **/
        EntityPlayerMP owner = (EntityPlayerMP) PlayerHelper.getPlayerFromUUID(false, team.getOwner());
        if (owner != null) {
            sendToClient(packet, owner);
        }
        
        /** Then send it to all team members **/
        for (UUID uuid: team.getMembers()) {
            EntityPlayerMP member = (EntityPlayerMP) PlayerHelper.getPlayerFromUUID(false, uuid);
            if (member != null) {
                sendToClient(packet, member);
            }
        }
    }

    public static void registerPackets(@Nonnull ASMDataTable asmDataTable) {
        String annotationClassName = Packet.class.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<ASMData>(asmDataTable.getAll(annotationClassName));

        HashMap<String, Pair<Side, Class>> sidedPackets = new HashMap();
        HashMap<String, Class> unsidedPackets = new HashMap();

        topLoop:
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Map<String, Object> data = asmData.getAnnotationInfo();
                boolean isSided = data.get("isSided") != null ? (Boolean) data.get("isSided") : false;
                if (isSided) {
                    String s = ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) data.get("side"), "value");
                    Side side = s.equals("CLIENT") ? Side.CLIENT : Side.SERVER;
                    sidedPackets.put(asmClass.getSimpleName(), Pair.of(side, (Class)asmClass));
                } else unsidedPackets.put(asmClass.getSimpleName(), asmClass);
            } catch (Exception e) { e.printStackTrace(); }
        }


        //Sort the packet alphabetically so they get registered the same on server and client side
        Comparator<String> alphabetical = new Comparator<String>() {
            public int compare(String str1, String str2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }

                return res;
            }
        };


        //Sort the sided and unsided packets
        List<String> namesSided = Lists.newArrayList(sidedPackets.keySet());
        Collections.sort(namesSided, alphabetical);
        List<String> namesUnsided = Lists.newArrayList(unsidedPackets.keySet());
        Collections.sort(namesUnsided, alphabetical);

        //Register sided packets
        for (String sided: namesSided) {
            Pair<Side, Class> result = sidedPackets.get(sided);
            try {
                registerPacket(result.getRight(), result.getLeft());
            } catch (Exception e) { e.printStackTrace(); }
        }

        //Register unsided packets
        for (String unsided: namesUnsided) {
            try {
                registerPacket(unsidedPackets.get(unsided));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
