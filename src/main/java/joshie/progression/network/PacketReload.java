package joshie.progression.network;

import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.DefaultSettings;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PacketReload extends PacketAction {
    @Override
    public void handlePacket(EntityPlayer player) {
        PacketReload.handle(JSONLoader.getTabs());
    }

    /** Borrowed from Blood Magic by WayofTime **/
    private static final int DELETEID = 2525277;
    private static final IChatComponent[] messages = new IChatComponent[] { 
            new ChatComponentText("Progression data was reloaded."), 
            new ChatComponentText("   Use " + EnumChatFormatting.BLUE + "/progression reset" + EnumChatFormatting.RESET + " if you wish to reset player data") 
    };

    private static int lastAdded;

    public static void handle(DefaultSettings settings) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            for (int i = DELETEID + 2 - 1; i <= lastAdded; i++) {
                chat.deleteChatLine(i);
            }

            for (int i = 0; i < 2; i++) {
                chat.printChatMessageWithOptionalDeletion(messages[i], DELETEID + i);
            }

            lastAdded = DELETEID + messages.length - 1;
        } else {
            if (Options.editor) {
                //Perform a reset of all the data serverside
                RemappingHandler.reloadServerData(settings);
                for (EntityPlayer player : PlayerHelper.getAllPlayers()) {
                    RemappingHandler.onPlayerConnect((EntityPlayerMP) player);
                }

                PacketHandler.sendToEveryone(new PacketReload());
            }
        }
    }
}
