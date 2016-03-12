package joshie.progression.network;

import joshie.progression.Progression;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PacketReset extends PacketAction {
    @Override
    public void handlePacket(EntityPlayer player) {
        PacketReset.handle();
    }

    public static void handle() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            MCClientHelper.getPlayer().addChatComponentMessage(new ChatComponentText("All player data for Progression was reset."));
        } else {
            if (Options.editor) {
                Progression.instance.createWorldData(); //Recreate the world data, Wiping out any saved information for players
                RemappingHandler.reloadServerData(JSONLoader.getTabs());
                for (EntityPlayerMP player : PlayerHelper.getAllPlayers()) {
                    //Reset all the data to default
                    RemappingHandler.onPlayerConnect(player);
                }

                PacketHandler.sendToEveryone(new PacketReset());
            }
        }
    }
}
