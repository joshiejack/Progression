package joshie.progression.network;

import joshie.progression.criteria.triggers.TriggerChangeGui;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class PacketDebugGUI extends PenguinPacket {
    @Override
    public void handlePacket(EntityPlayer player) {
        ChatHelper.displayChat(EnumChatFormatting.BLUE + "Displaying class names for guis was set to " + TriggerChangeGui.toggleDebug());
    }
}
