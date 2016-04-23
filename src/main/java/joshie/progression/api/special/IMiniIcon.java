package joshie.progression.api.special;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Implement this on objects that have a miniature icon **/
public interface IMiniIcon {
    @SideOnly(Side.CLIENT)
    public ItemStack getMiniIcon();
}
