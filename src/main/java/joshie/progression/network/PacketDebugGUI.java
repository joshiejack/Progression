package joshie.progression.network;

import joshie.progression.criteria.triggers.TriggerChangeGui;
import joshie.progression.helpers.ChatHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class PacketDebugGUI extends PacketAction {
	@Override
	public void handlePacket(EntityPlayer player) {
		ChatHelper.displayChat(EnumChatFormatting.BLUE + "Displaying class names for guis was set to " + TriggerChangeGui.toggleDebug());
	}
}
