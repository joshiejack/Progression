package joshie.crafting.asm;

import java.util.UUID;

import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerPlayer extends Container {
	public UUID uuid;
	
	public ContainerPlayer() {}
	public ContainerPlayer(Object player) {
		this.uuid = PlayerHelper.getUUIDForPlayer((EntityPlayer)player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return false;
	}
}
