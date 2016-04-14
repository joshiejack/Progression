package joshie.progression.asm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerPlayer extends Container {
    public EntityPlayer player;

    public ContainerPlayer() {}
    public ContainerPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }
}
