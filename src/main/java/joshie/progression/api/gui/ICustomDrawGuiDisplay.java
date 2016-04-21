package joshie.progression.api.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Implement this on rewards, triggers, filters, conditions, 
 *  if you wish to draw something special on them, other than default fields. */
public interface ICustomDrawGuiDisplay {
    @SideOnly(Side.CLIENT)
    public void drawDisplay(IDrawHelper helper, int renderX, int renderY, int mouseX, int mouseY);
}
