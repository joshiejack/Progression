package joshie.progression.gui;

import java.io.IOException;

import joshie.progression.gui.base.GuiOffset;
import joshie.progression.gui.base.IRenderOverlay;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class GuiCriteriaEditor extends GuiOffset {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();

    public static void registerOverlay(IRenderOverlay overlay) {
        INSTANCE.overlays.add(overlay);
    }

    @Override
    public void drawForeground() {
        
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        
    }

    private static long lastClick;

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        
    }
}
