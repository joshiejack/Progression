package joshie.crafting.gui;

import joshie.crafting.api.ICriteria;
import joshie.crafting.json.JSONLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiBase extends GuiScreen {
    public int mouseX = 0;
    public int mouseY = 0;

    protected int leftX = 212;
    protected int rightX = 218;
    protected int xSize = 430;
    protected int ySize = 217;
    public ICriteria selected = null;

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        JSONLoader.saveJSON();
    }

    public void drawScreen(int i, int j, float f) {
        int x = 0;
        int y = (height - ySize) / 2;
        drawRectWithBorder(-1, y, mc.displayWidth + 1, y + ySize, 0xFFCCCCCC, 0xCC000000);
        super.drawScreen(i, j, f);
    }

    public void drawRectWithBorder(int x, int y, int x2, int y2, int color, int border) {
        drawRect(x, y, x2, y2, color);
        drawRect(x, y, x + 1, y2, border);
        drawRect(x2 - 1, y, x2, y2, border);
        drawRect(x, y, x2, y + 1, border);
        drawRect(x, y2 - 1, x2, y2, border);
    }

    @Override
    public void handleMouseInput() {
        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        mouseX = x;
        mouseY = y - (height - ySize) / 2;
        super.handleMouseInput();
    }
}
