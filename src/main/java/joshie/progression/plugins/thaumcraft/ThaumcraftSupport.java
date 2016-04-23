package joshie.progression.plugins.thaumcraft;

import joshie.progression.asm.helpers.TransferHelper;
import joshie.progression.asm.helpers.TransferHelper.Transferable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.container.SlotCraftingArcaneWorkbench;

public class ThaumcraftSupport {
    public static void init() {
        TransferHelper.list.add(new Transferable() {
            @Override
            public boolean isAcceptable(Object object, Slot slot, EntityPlayer player, ItemStack stack) {
                return slot instanceof SlotCraftingArcaneWorkbench;
            }
        });
    }
}
