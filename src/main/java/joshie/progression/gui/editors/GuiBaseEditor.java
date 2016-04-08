package joshie.progression.gui.editors;

import joshie.progression.api.special.DisplayMode;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IGuiFeature;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.json.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public abstract class GuiBaseEditor implements IEditorMode {
    public ArrayList<IGuiFeature> features;
    public ScaledResolution res;
    public DisplayMode mode;
    public Minecraft mc;
    public GuiCore core;
    public Theme theme;

    @Override
    public void initData(GuiCore core) {
        this.core = core;
        this.res = core.res;
        this.mc = core.mc;
        this.mode = MCClientHelper.getMode();
        this.features = core.features;
        this.theme = core.theme;
    }

    @Override
    public void keyTyped(char character, int key) {}
    
    @Override
    public void guiMouseReleased(int mouseX, int mouseY, int button) {}
    
    @Override
    public void handleMouseInput(int mouseX, int mouseY) {}

    //Convenience methods - Stacks
    public void drawStack(ItemStack stack, int x, int y, float scale) {
        core.drawStack(stack, x, y, scale);
    }

    //Convenience methods - Text
    public void drawText(String text, int left, int top, int color) {
        core.drawText(text, left, top, color);
    }

    //Convenience methods - Gradient
    public void drawGradientRectWithBorder(int left, int top, int right, int bottom, int startColor, int endColor, int border) {
        core.drawGradientRectWithBorder(left, top, right, bottom, startColor, endColor, border);
    }

    //Convenience methods - Line
    public void drawLine(int left, int top, int right, int bottom, int thickness, int color) {
        core.drawLine(left, top, right, bottom, thickness, color);
    }

    //Convenience methods - Tooltip
    public void addTooltip(String string) {
        FeatureTooltip.INSTANCE.addTooltip(string);
    }

    //Convenience methods - Tooltip Advanced
    public void addTooltip(String tooltip, int length) {
        String[] strings = SplitHelper.splitTooltip(tooltip, length);
        for (String s: strings) FeatureTooltip.INSTANCE.addTooltip("  " + s);
    }
}
