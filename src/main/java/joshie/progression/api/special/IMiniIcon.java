package joshie.progression.api.special;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Implement this on objects that have a miniature icon **/
public interface IMiniIcon {
    @SideOnly(Side.CLIENT)
    public ResourceLocation getMiniIcon();
}
