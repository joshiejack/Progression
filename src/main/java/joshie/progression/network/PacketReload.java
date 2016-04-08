package joshie.progression.network;

import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.DefaultSettings;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

public class PacketReload extends PacketAction {
    @Override
    public void handlePacket(EntityPlayer player) {
        PacketReload.handle(JSONLoader.getServerTabData(), player.worldObj.isRemote);
    }

    public static void handle(DefaultSettings settings, boolean isClient) {
        if (isClient) {
            ChatHelper.displayChat("Progression data was reloaded", "   Use " + EnumChatFormatting.BLUE + "/progression reset" + EnumChatFormatting.RESET + " if you wish to reset player data");
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
