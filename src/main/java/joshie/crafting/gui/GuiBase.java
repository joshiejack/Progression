package joshie.crafting.gui;

import joshie.crafting.api.ICriteria;
import joshie.crafting.json.JSONLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiBase extends GuiScreen {
    public int mouseX = 0;
    public int mouseY = 0;

    protected int leftX = 212;
    protected int rightX = 218;
    protected int xSize = 430;
    protected int ySize = 217;
    public ICriteria selected = null;
    public ICriteria previous = null;

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        EditorTicker.LAST_TICK = 1000;
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        JSONLoader.saveCriteria();
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

    public void drawLine(int x, int y, int x2, int y2, int thickness, int color) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f, f1, f2, f3);
        
        int posX = 0;
        if (x2 > x) {
            posX = thickness;
        } else {
            posX = -thickness;
        }
        
        int posY = 0;
        if (y2 > y) {
            posY = thickness;
        } else {
            posY = -thickness;
        }
        
        tessellator.startDrawing(7);
        tessellator.addVertex((double) x, (double) y + posX, 0.0D);
        tessellator.addVertex((double) x2, (double) y2 + posX, 0.0D);
        tessellator.addVertex((double) x2 + posY, (double) y2, 0.0D);
        tessellator.addVertex((double) x + posY, (double) y, 0.0D);
        tessellator.draw();

        tessellator.startDrawing(7);
        colorize(0xFFBF00FF);
        tessellator.addVertex((double) x, (double) y, 0.0D);
        tessellator.addVertex((double) x + 5, (double) y, 0.0D);
        tessellator.addVertex((double) x + 5, (double) y + 5, 0.0D);
        tessellator.addVertex((double) x, (double) y + 5, 0.0D);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void colorize(int color) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        GL11.glColor4f(f, f1, f2, f3);
    }

    public int offsetX = 0;

    public void scroll(int amount) {
        offsetX += amount;
        if (offsetX >= 0) {
            offsetX = 0;
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (SelectTextEdit.getEditable() == null) {
            int jump = 1;
            if (Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42)) {
                jump = 50;
            }

            if (key == 203) {
                scroll(jump);
            } else if (key == 205) {
                scroll(-jump);
            }
        }

        super.keyTyped(character, key);
    }

    @Override
    public void handleMouseInput() {
        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        mouseX = x;
        mouseY = y - (height - ySize) / 2;

        if (!SelectItemOverlay.INSTANCE.isVisible()) {
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                if (wheel < 0) {
                    scroll(20);
                } else {
                    scroll(-20);
                }
            }
        }

        super.handleMouseInput();
    }
}
