package joshie.progression.asm.helpers;

import joshie.progression.api.ProgressionAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TransferHelper {
    public static List<Transferable> list = new ArrayList<Transferable>();
    static {
        list.add(new Transferable());
    }

    public static void onPickup(Object object, Slot slot, EntityPlayer player, ItemStack stack) {
        if (!player.worldObj.isRemote) {
            for (Transferable t: list) {
                if (t.isAcceptable(object, slot, player, stack)) {
                    ProgressionAPI.registry.fireTrigger(player, "trigger.crafting", stack);
                    break;
                }
            }
        }
    }

    public static class Transferable {
        public boolean isAcceptable(Object object, Slot slot, EntityPlayer player, ItemStack stack) {
            return (slot.getSlotIndex() == 0 && slot instanceof SlotCrafting);
        }

        @Override
        public int hashCode() {
            return this.getClass().getSimpleName().hashCode();
        }

        @Override
        public boolean equals(Object object) {
            return object == null ? false : object.getClass().equals(this.getClass());
        }
    }
}
