package joshie.progression.api.special;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Implement this on objects that have a special icon **/
public interface ICustomIcon {
    @SideOnly(Side.CLIENT)
    public ItemStack getIcon();
}
