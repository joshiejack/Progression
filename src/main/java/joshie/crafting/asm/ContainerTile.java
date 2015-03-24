package joshie.crafting.asm;

import joshie.crafting.api.CraftingAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerTile extends ContainerPlayer {	
	public ContainerTile() {}
	public ContainerTile(Object tile) {
		TileEntity tileEntity = (TileEntity)tile;
		uuid = CraftingAPI.players.getTileOwner(tileEntity).getUUID();
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return false;
	}
}
