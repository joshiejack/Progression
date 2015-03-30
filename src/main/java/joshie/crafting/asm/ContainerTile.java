package joshie.crafting.asm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class ContainerTile extends Container {	
    public TileEntity tile;
    
	public ContainerTile() {}
	public ContainerTile(TileEntity tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return false;
	}
}
