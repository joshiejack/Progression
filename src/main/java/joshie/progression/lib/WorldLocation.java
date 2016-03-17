package joshie.progression.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class WorldLocation {
    public EntityPlayer player;
    public int dimension;
    public BlockPos pos;

    public WorldLocation(int dimension, double x, double y, double z) {
        this.dimension = dimension;
        this.pos = new BlockPos(x, y, z);
    }
    
    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }
}
