package joshie.progression.network;

import joshie.progression.Progression;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;

public class PacketOpenEditor extends PacketAction {
	@Override
	public void handlePacket(EntityPlayer player) {
		MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.EDITOR, null, 0, 0, 0);
	}
}
