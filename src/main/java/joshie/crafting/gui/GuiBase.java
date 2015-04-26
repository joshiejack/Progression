package joshie.crafting.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import joshie.crafting.Criteria;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.JSONLoader;
import joshie.crafting.json.Theme;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiBase extends GuiScreen {
    protected static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
    protected Set<IRenderOverlay> overlays = new HashSet();
    public int mouseX = 0;
    public int mouseY = 0;

    protected ScaledResolution res;
    protected ArrayList<String> tooltip;
    protected boolean blockTooltips;
    protected int leftX = 212;
    protected int rightX = 218;
    protected int xSize = 430;
    protected int ySize = 240;
    public Theme theme = null;
    public Criteria selected = null;
    public Criteria previous = null;
    public int y;

    @Override
    public void initGui() {
        super.initGui();
        y = (height - ySize) / 2;
        Keyboard.enableRepeatEvents(true);
        res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        theme = Theme.INSTANCE;
        SelectItemOverlay.INSTANCE.clear();
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (ClientHelper.getPlayer().capabilities.isCreativeMode) {
            JSONLoader.saveData();
            EditorTicker.LAST_TICK = 60;
        }
    }

    public void drawScreen(int x, int y, float f) {
        tooltip = new ArrayList();
        drawBackground();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        drawForeground();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        super.drawScreen(x, y, f);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        drawTooltip(tooltip, x, y);
    }

    public void drawBackground() {
        drawRectWithBorder(-1, y, mc.displayWidth + 1, y + ySize, theme.backgroundColor, theme.backgroundBorder);
    }

    public abstract void drawForeground();

    public void drawRectWithBorder(int x, int y, int x2, int y2, int color, int border) {
        drawRect(x, y, x2, y2, color);
        drawRect(x, y, x + 1, y2, border);
        drawRect(x2 - 1, y, x2, y2, border);
        drawRect(x, y, x2, y + 1, border);
        drawRect(x, y2 - 1, x2, y2, border);
    }

    public void drawGradientRectWithBorder(int x, int y, int x2, int y2, int color1, int color2, int border) {
        drawGradientRect(x, y, x2, y2, color1, color2);
        drawRect(x, y, x + 1, y2, border);
        drawRect(x2 - 1, y, x2, y2, border);
        drawRect(x, y, x2, y + 1, border);
        drawRect(x, y2 - 1, x2, y2, border);
    }

    public void drawString(String text, int x, int y, int color) {
        mc.fontRenderer.drawString(text, x, y, color);
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
        colorize(theme.connectLineColorize);
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

    public void addTooltip(List list) {
        tooltip.addAll(list);
    }

    private void drawTooltip(List list, int x, int y) {
        if (!list.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                int l = fontRendererObj.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (list.size() > 1) {
                i1 += 2 + (list.size() - 1) * 10;
            }

            if (j2 + k > this.width) {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > this.height) {
                k2 = this.height - i1 - 6;
            }

            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = -267386864;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = theme.toolTipWhite;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < list.size(); ++i2) {
                String s1 = (String) list.get(i2);
                fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
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
        int wheel = Mouse.getDWheel();
        boolean down = wheel < 0;
        if (wheel != 0) {
            if (!SelectItemOverlay.INSTANCE.isVisible()) {
                if (!down) {
                    scroll(20);
                } else {
                    scroll(-20);
                }
            } else {
                SelectItemOverlay.INSTANCE.scroll(down);
            }
        }

        super.handleMouseInput();
    }
}
