package joshie.progression.network;

import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.DefaultSettings;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

@Packet
public class PacketReload extends PenguinPacket {
    @Override
    public void handlePacket(EntityPlayer player) {
        String hostname = player.worldObj.isRemote ? JSONLoader.serverName : RemappingHandler.getHostName();
        PacketReload.handle(JSONLoader.getServerTabData(hostname), player.worldObj.isRemote);
    }

    public static void handle(DefaultSettings settings, boolean isClient) {
        if (isClient) {
            ChatHelper.displayChat("Progression data was reloaded", "   Use " + TextFormatting.BLUE + "/progression reset" + TextFormatting.RESET + " if you wish to reset player data");
        } else {
            if (Options.editor) {
                //Perform a reset of all the data serverside
                RemappingHandler.reloadServerData(settings, false);
                for (EntityPlayer player : PlayerHelper.getAllPlayers()) {
                    RemappingHandler.onPlayerConnect((EntityPlayerMP) player);
                }

                PacketHandler.sendToEveryone(new PacketReload());
            }
        }
    }
}
