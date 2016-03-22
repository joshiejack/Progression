package joshie.progression.gui.editors;

import java.util.ArrayList;
import java.util.HashMap;

import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IGuiFeature;
import joshie.progression.json.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

public abstract class GuiBaseEditor implements IEditorMode {
    public ArrayList<IGuiFeature> features;
    public HashMap<String, IProgressionField> fields;
    public ScaledResolution res;
    public Minecraft mc;
    public GuiCore core;
    public Theme theme;

    public int offsetX;
    public int screenTop;
    public int screenWidth;

    @Override
    public void initData(GuiCore core) {
        this.core = core;
        this.res = core.res;
        this.mc = core.mc;
        this.offsetX = core.offsetX;
        this.features = core.features;
        this.fields = core.fields;
        this.theme = core.theme;
        this.screenTop = core.screenTop;
        this.screenWidth = core.screenWidth;
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
}
