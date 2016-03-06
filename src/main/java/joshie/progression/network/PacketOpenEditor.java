package joshie.progression.network;

import joshie.progression.Progression;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;

public class PacketOpenEditor extends PacketAction {
	@Override
	public void handlePacket(EntityPlayer player) {
		MCClientHelper.getPlayer().openGui(Progression.instance, 0, null, 0, 0, 0);
	}
}
