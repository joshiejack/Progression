package joshie.progression.gui.editors;

import joshie.progression.gui.core.IGuiFeature;
import joshie.progression.helpers.SplitHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import static joshie.progression.gui.core.GuiList.CORE;
import static joshie.progression.gui.core.GuiList.TOOLTIP;

public abstract class GuiBaseEditor implements IEditorMode {
    protected ArrayList<IGuiFeature> features = new ArrayList<IGuiFeature>();

    @Override
    public void initData() {}

    @Override
    public boolean hasButtons() {
        return false;
    }

    @Override
    public ArrayList<IGuiFeature> getFeatures() {
        return features;
    }

    @Override
    public void keyTyped(char character, int key) {}
    
    @Override
    public void guiMouseReleased(int mouseX, int mouseY, int button) {}
    
    @Override
    public void handleMouseInput(int mouseX, int mouseY) {}

    //Convenience methods - Stacks
    public void drawStack(ItemStack stack, int x, int y, float scale) {
        CORE.drawStack(stack, x, y, scale);
    }

    //Convenience methods - Text
    public void drawText(String text, int left, int top, int color) {
        CORE.drawText(text, left, top, color);
    }

    //Convenience methods - Gradient
    public void drawGradientRectWithBorder(int left, int top, int right, int bottom, int startColor, int endColor, int border) {
        CORE.drawGradientRectWithBorder(left, top, right, bottom, startColor, endColor, border);
    }

    //Convenience methods - Line
    public void drawLine(int left, int top, int right, int bottom, int thickness, int color) {
        CORE.drawLine(left, top, right, bottom, thickness, color);
    }

    //Convenience methods - Tooltip
    public void addTooltip(String string) {
        TOOLTIP.add(string);
    }

    //Convenience methods - Tooltip Advanced
    public void addTooltip(String tooltip, int length) {
        String[] strings = SplitHelper.splitTooltip(tooltip, length);
        TOOLTIP.add(SplitHelper.splitTooltip(tooltip.replace("\r", ""), length));
    }
}
