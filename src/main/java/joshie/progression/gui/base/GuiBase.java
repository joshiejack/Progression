package joshie.progression.gui.base;

import static joshie.progression.network.core.PacketPart.SEND_SIZE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import joshie.progression.Progression;
import joshie.progression.api.ICriteria;
import joshie.progression.gui.editors.EditText;
import joshie.progression.gui.editors.SelectItem;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.json.Theme;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncJSONToServer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class GuiBase extends GuiScreen {
    public Set<IRenderOverlay> overlays = new HashSet<IRenderOverlay>();
    public int mouseX = 0;
    public int mouseY = 0;

    public ScaledResolution res;
    public ArrayList<String> tooltip;
    public int ySize = 240;
    public Theme theme = null;
    public ICriteria selected = null;
    public ICriteria previous = null;
    public boolean switching = false;
    public int y;

    @Override
    public void initGui() {
        super.initGui();
        switching = false;
        y = (height - ySize) / 2;
        Keyboard.enableRepeatEvents(true);
        res = new ScaledResolution(mc);
        theme = Theme.INSTANCE;
        SelectItem.INSTANCE.clear();
    }

    @Override
    protected void actionPerformed(GuiButton button) {}
    
    private static int GUI_CLOSED;

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (MCClientHelper.isInEditMode() && !switching) {
            if (Options.debugMode) {
                Progression.logger.log(Level.INFO, "Saving JSON Data");
            }

            JSONLoader.saveData(); //Save the data clientside
            String json = JSONLoader.getClientTabJsonData();
            int length = SplitHelper.splitStringEvery(json, 5000).length;
            PacketHandler.sendToServer(new PacketSyncJSONToServer(SEND_SIZE, "", length, System.currentTimeMillis()));
            //Send the packet to the server about the new json
            GuiCriteriaEditor.INSTANCE.clearEditors();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        tooltip = new ArrayList<String>();
        drawBackground();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        drawForeground();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        drawTooltip(tooltip, mouseX, mouseY);
    }

    public void drawBackground() {
        drawRectWithBorder(-1, y, mc.displayWidth + 1, y + ySize, theme.backgroundColor, theme.backgroundBorder);
    }

    public abstract void drawForeground();

    public void drawRectWithBorder(int left, int top, int right, int bottom, int color, int border) {
        drawRect(left, top, right, bottom, color);
        drawRect(left, top, left + 1, bottom, border);
        drawRect(right - 1, top, right, bottom, border);
        drawRect(left, top, right, top + 1, border);
        drawRect(left, bottom - 1, right, bottom, border);
    }

    public void drawGradientRectWithBorder(int left, int top, int right, int bottom, int startColor, int endColor, int border) {
        drawGradientRect(left, top, right, bottom, startColor, endColor);
        drawRect(left, top, left + 1, bottom, border);
        drawRect(right - 1, top, right, bottom, border);
        drawRect(left, top, right, top + 1, border);
        drawRect(left, bottom - 1, right, bottom, border);
    }

    public void drawLine(int left, int top, int right, int bottom, int thickness, int color) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);

        int posX;
        if (right > left) {
            posX = thickness;
        } else {
            posX = -thickness;
        }

        int posY;
        if (bottom > top) {
            posY = thickness;
        } else {
            posY = -thickness;
        }

        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double) left, (double) top + posX, 0.0D).endVertex();
        worldrenderer.pos((double) right, (double) bottom + posX, 0.0D).endVertex();
        worldrenderer.pos((double) right + posY, (double) bottom, 0.0D).endVertex();
        worldrenderer.pos((double) left + posY, (double) top, 0.0D).endVertex();
        tessellator.draw();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) left, (double) top, 0.0D).color(f, f1, f2, f3).endVertex();
        worldrenderer.pos((double) left + 5, (double) top, 0.0D).color(f, f1, f2, f3).endVertex();
        worldrenderer.pos((double) left + 5, (double) top + 5, 0.0D).color(f, f1, f2, f3).endVertex();
        worldrenderer.pos((double) left, (double) top + 5, 0.0D).color(f, f1, f2, f3).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void addTooltip(List<String> list) {
        tooltip.addAll(list);
    }

    private void drawTooltip(List<String> list, int x, int y) {
        if (!list.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int k = 0;
            Iterator<String> iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = iterator.next();
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
                String s1 = list.get(i2);
                fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
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
    protected void keyTyped(char character, int key) throws IOException {
        if (EditText.getEditable() == null) {
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
    public void handleMouseInput() throws IOException {
        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        mouseX = x;
        mouseY = y - (height - ySize) / 2;
        int wheel = Mouse.getDWheel();
        boolean down = wheel < 0;
        if (wheel != 0) {
            if (!SelectItem.INSTANCE.isVisible()) {
                if (!down) {
                    scroll(20);
                } else {
                    scroll(-20);
                }
            } else {
                SelectItem.INSTANCE.scroll(down);
            }
        }
        super.handleMouseInput();
    }
}