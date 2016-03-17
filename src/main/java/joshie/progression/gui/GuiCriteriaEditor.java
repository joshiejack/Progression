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
        ScaledResolution res = GuiCriteriaEditor.INSTANCE.res;
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        //Title and Repeatability Box
        //Draw Tabs
        mc.getTextureManager().bindTexture(ProgressionInfo.textures);
        drawTexturedModalRect(fullWidth - 30, 15, 80, 0, 20, 25);
        for (int i = 0; i < 40; i++) {
            //drawTexturedModalRect(10 + i, 10, 0, 0, 1, 25);
        }

        drawTexturedModalRect(fullWidth - 50, 15, 0, 0, 20, 25);

        if (MCClientHelper.canEdit()) {
            int crossY = 64;

            GlStateManager.color(1F, 1F, 1F, 1F);
            MCClientHelper.getMinecraft().getTextureManager().bindTexture(ProgressionInfo.textures);
           
        }


        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.draw(0, y);
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        super.keyTyped(character, key);

        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.keyTyped(character, key);
            }
        }
    }

    private static long lastClick;

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = difference <= 150;
        lastClick = System.currentTimeMillis();

        boolean clicked = false;
        int visible = 0;
        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                if (overlay.mouseClicked(mouseX, mouseY, button)) {
                    clicked = true;
                    break;
                }

                visible++;
            }
        }

        super.mouseClicked(x, y, button);
    }
}
