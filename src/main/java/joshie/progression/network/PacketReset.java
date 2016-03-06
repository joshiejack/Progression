package joshie.progression.network;

import joshie.progression.Progression;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.Options;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PacketReset extends PacketAction implements IMessageHandler<PacketReset, IMessage> {
    @Override
    public IMessage onMessage(PacketReset message, MessageContext ctx) {
        PacketReset.handle();
        return null;
    }

    public static void handle() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            ClientHelper.getPlayer().addChatComponentMessage(new ChatComponentText("All player data for Progression was reset."));
        } else {
            if (Options.editor) {
                Progression.instance.createWorldData(); //Recreate the world data, Wiping out any saved information for players
                RemappingHandler.reloadServerData();
                for (EntityPlayer player : PlayerHelper.getAllPlayers()) {
                    RemappingHandler.onPlayerConnect((EntityPlayerMP) player);
                }

                PacketHandler.sendToEveryone(new PacketReset());
            }
        }
    }
}
