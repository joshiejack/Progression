package joshie.progression.asm;

import joshie.progression.asm.helpers.ThaumcraftHelper;
import net.minecraft.entity.player.InventoryPlayer;
import thaumcraft.common.tiles.TileArcaneWorkbench;

public class ASMHelper {
    private TileArcaneWorkbench tileEntity;
    private InventoryPlayer ip;
    
    public void test() {
        ThaumcraftHelper.onContainerChanged(tileEntity, ip);
    }
}
