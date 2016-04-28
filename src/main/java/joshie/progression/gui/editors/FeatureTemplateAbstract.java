package joshie.progression.gui.editors;

import joshie.progression.gui.core.FeatureAbstract;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import static joshie.progression.gui.core.GuiList.*;

public abstract class FeatureTemplateAbstract extends FeatureAbstract {
    private String search = "";

    public FeatureTemplateAbstract() {
        setVisibility(false);
    }

    public void setVisible() {
        TEMPLATE_SELECTOR_TAB.setVisibility(false);
        TEMPLATE_SELECTOR_CRITERIA.setVisibility(false);
        setVisibility(true);
    }

    @Override
    public boolean isOverlay() {
        return true;
    }

    protected boolean isMouseOver(int mouseX, int mouseY, int j, int k) {
        if (mouseX >= 32 + (j * 16) && mouseX <= 32 + (j * 16) + 16) {
            if (mouseY >= 45 + (k * 16) && mouseY <= 45 + (k * 16) + 16) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int width = (int) ((double) (screenWidth - 75) / 16.133333334D);
        if (clickForeground(mouseX, mouseY, width)) return true;
        setVisibility(false);
        return false;
    }

    protected abstract boolean clickForeground(int mouseX, int mouseY, int width);

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        int offsetX = CORE.getOffsetX();
        ScaledResolution res = CORE.res;
        CORE.drawGradientRectWithBorder(30, 20, res.getScaledWidth() - 30, 40, THEME.blackBarGradient1, THEME.blackBarGradient2, THEME.blackBarBorder);
        CORE.drawRectWithBorder(30, 40, res.getScaledWidth() - 30, 210, THEME.blackBarUnderLine, THEME.blackBarUnderLineBorder);

        CORE.mc.fontRendererObj.drawString("Select a Template - Click Elsewhere to close", 35 - offsetX, CORE.screenTop + 27, THEME.blackBarFontColor);
        drawForeground(mouseX, mouseY, (int)((double) (screenWidth - 75) / 16.133333334D));
    }

    protected abstract void drawForeground(int mouseX, int mouseY, int width);
}