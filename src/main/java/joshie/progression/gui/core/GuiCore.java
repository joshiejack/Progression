package joshie.progression.gui.core;

import joshie.progression.PClientProxy;
import joshie.progression.gui.editors.IEditorMode;
import joshie.progression.gui.editors.insert.FeatureNew;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.json.Theme;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketLockUnlockSaving;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static joshie.progression.api.special.DisplayMode.EDIT;
import static joshie.progression.gui.core.GuiList.*;

public class GuiCore extends GuiScreen {
    public HashMap<Object, Integer> offsetCache = new HashMap();
    public ScaledResolution res;
    public IEditorMode lastGui;
    public IEditorMode openGui;
    public IEditorMode nextGui;
    public int mouseX = 0;
    public int mouseY = 0;

    private int offsetX; // Offset position
    public int ySize = 240; // Height of the Gui :O
    public int screenTop; // Top of the screen :D
    public int screenWidth; //Width of the screen

    public boolean clickedButton = false;
    public boolean scrollingEnabled = true;
    public boolean markedForInit = false;

    public GuiCore setEditor(IEditorMode editor) {
        this.markedForInit = true;
        this.nextGui = editor;
        if (this.nextGui != GuiList.GROUP_EDITOR) {
            this.lastGui = this.nextGui;
        }
        
        return this;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void resetX() {
        offsetX = 0;
    }

    @Override
    public void initGui() {
        MODE = MCClientHelper.getMode(); //Update the mode
        if (openGui == null) openGui = TREE_EDITOR;
        mc = Minecraft.getMinecraft();
        res = new ScaledResolution(mc);
        screenWidth = res.getScaledWidth();
        Keyboard.enableRepeatEvents(true);
        if (offsetCache.containsKey(getKey())) {
            offsetX = offsetCache.get(getKey());
        } else offsetX = 0;

        scrollingEnabled = true;
        screenTop = (height - ySize) / 2;
        if (openGui != null) {
            openGui.initData();
            TOOLTIP.init();
            for (IGuiFeature feature : openGui.getFeatures()) {
                feature.init();
            }
        }
    }

    public void clearEditors() {
        ITEM_EDITOR.clearEditable();
        TEXT_EDITOR_FULL.clearEditable();
        TEXT_EDITOR_SIMPLE.clearEditable();
        NEW_TRIGGER.setVisibility(false);
        NEW_REWARD.setVisibility(false);
        NEW_FILTER.setVisibility(false);
        NEW_CONDITION.setVisibility(false);
    }

    public List<GuiButton> getButtonNewList() {
        buttonList = new ArrayList();
        return buttonList;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (MODE == EDIT) {
            //Don't allow this gui to be reopened until the server is ready
            PClientProxy.isSaver = true; //Mark us as being the one who saved
            PacketHandler.sendToServer(new PacketLockUnlockSaving(true));
        }
    }

    @Override
    public void drawScreen(int cursorX, int cursorY, float partialTicks) {
        //If we've changed the main gui, reset the data at the start
        if (markedForInit) {
            markedForInit = false;
            openGui = nextGui; //Replace these
            initGui();
        }

        TOOLTIP.clear();
        screenTop = (height - ySize) / 2;
        boolean overlayvisible = false;
        for (IGuiFeature feature : openGui.getFeatures()) {
            if (feature.isVisible() && !feature.isOverlay()) { //Only draw visible stuff
                feature.draw(cursorX, cursorY - screenTop);
            }

            if (feature.isVisible() && feature.isOverlay()) overlayvisible = true;
        }

        if (openGui != null) openGui.drawGuiForeground(overlayvisible, mouseX, mouseY);
        if (overlayvisible) TOOLTIP.clear();

        for (IGuiFeature feature : openGui.getFeatures()) {
            if (feature.isVisible() && feature.isOverlay()) { //Only new Stuff
                feature.draw(cursorX, cursorY - screenTop);
            }
        }

        //Draw the tooltip in the right place
        if (openGui.hasButtons()) super.drawScreen(cursorX, cursorY - screenTop, partialTicks);
        TOOLTIP.drawFeature(cursorX, cursorY);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        if (markedForInit) return; //Dont process clicks while active
        boolean overlayvisible = false;
        if (button == 0) {
            for (IGuiFeature feature : openGui.getFeatures()) {
                if (feature.isVisible()) { //Don't process hidden features
                    if (feature.mouseClicked(mouseX, mouseY, button)) {
                        return; // Don't continue if a mouse click was processed
                    }

                    if (feature.isOverlay()) overlayvisible = true;
                }
            }
        }

        if (openGui.hasButtons()) super.mouseClicked(mouseX, mouseY, button);
        if (clickedButton) {
            clickedButton = false;
            return;
        }
        
        if (openGui != null && openGui.guiMouseClicked(overlayvisible, mouseX, mouseY, button)) {
            return;
        }

        if (button == 1) {
            if (!FeatureNew.IS_OPEN) {
                if (openGui != null && openGui != GROUP_EDITOR) {
                    setEditor(openGui.getPreviousGui());
                }
            }
        }

        clearEditors();
    }

    @Override
    public void mouseReleased(int x, int y, int button) {
        if (openGui != null) openGui.guiMouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        Keyboard.enableRepeatEvents(true); //Because something is breaking it
        if (markedForInit) return; //Dont process keys while active
        int jump = 1;
        if (Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42)) {
            jump = 100; //Shift Jump
        }

        if (!TEXT_EDITOR_SIMPLE.isEditing()) {
            if (key == 203) {
                scroll(jump);
            } else if (key == 205) {
                scroll(-jump);
            }
        }

        if (openGui != null && MODE == EDIT) openGui.keyTyped(character, key);

        TEXT_EDITOR_SIMPLE.keyTyped(character, key);
        super.keyTyped(character, key);
    }

    @Override
    public void handleMouseInput() throws IOException {
        mouseX = Mouse.getEventX() * width / mc.displayWidth;
        mouseY = (height - Mouse.getEventY() * height / mc.displayHeight - 1) - screenTop;

        int wheel = Mouse.getDWheel();
        boolean down = wheel < 0;
        if (wheel != 0) {
            boolean scrolled = false;
            for (IGuiFeature feature : openGui.getFeatures()) {
                if (feature.isVisible()) { //Must be visible of course!
                    if (feature.scroll(mouseX, mouseY, down)) {
                        scrolled = true;
                        break;
                    }
                }
            }

            if (!scrolled) {
                if (!down) {
                    scroll(50);
                } else {
                    scroll(-50);
                }
            }
        }

        super.handleMouseInput();
        if (openGui != null) openGui.handleMouseInput(mouseX, mouseY);
    }

    public Object getKey() {
        return openGui == null ? this : openGui.getKey();
    }

    // Scrolling Helpers
    public void scroll(int amount) {
        if (scrollingEnabled) {
            offsetX += amount;
            if (offsetX >= 0) {
                offsetX = 0;
            }

            offsetCache.put(getKey(), offsetX);
        }
    }

    // Helper Drawing functions
    // Regular Rectangle
    public void drawRectWithBorder(int left, int top, int right, int bottom, int color, int border) {
        top = screenTop + top; //Adjust for the screenHeight
        bottom = screenTop + bottom; //Adjust the bottom too
        drawRect(left, top, right, bottom, color);
        drawRect(left, top, left + 1, bottom, border);
        drawRect(right - 1, top, right, bottom, border);
        drawRect(left, top, right, top + 1, border);
        drawRect(left, bottom - 1, right, bottom, border);
    }

    // Gradient Rectangle
    public void drawGradientRectWithBorder(int left, int top, int right, int bottom, int startColor, int endColor, int border) {
        top = screenTop + top; //Adjust for the screenHeight
        bottom = screenTop + bottom; //Adjust the bottom too
        drawGradientRect(left, top, right, bottom, startColor, endColor);
        drawRect(left, top, left + 1, bottom, border);
        drawRect(right - 1, top, right, bottom, border);
        drawRect(left, top, right, top + 1, border);
        drawRect(left, bottom - 1, right, bottom, border);
    }

    //Gradient rect, For tooltip usage so leave alone
    @Override
    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    public void drawStack(ItemStack stack, int x, int y, float scale) {
        RenderItemHelper.drawStack(stack, x, y + screenTop, scale);
    }

    public void drawText(String text, int left, int top, int color) {
        drawText(text, left, top, color, 1F);
    }

    public void drawText(String text, int left, int top, int color, float scale) {
        fontRendererObj.drawString(text, (int) (left / scale), (int) ((top + screenTop) / scale), color);
    }

    public void drawRightAlignedText(String text, int left, int top, int color, float scale) {
        fontRendererObj.setBidiFlag(true);
        fontRendererObj.drawString(text, (int) (left / scale), (int) ((top + screenTop) / scale), color);
        fontRendererObj.setBidiFlag(false);
    }

    public void drawSplitText(String text, int left, int top, int width, int color) {
        drawSplitText(text, left, top, width, color, 1F);
    }

    public void drawSplitText(String text, int left, int top, int width, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        fontRendererObj.drawSplitString(text, (int) (left / scale), (int) ((top + screenTop) / scale), width, color);
        GlStateManager.popMatrix();
    }

    public void drawTexture(ResourceLocation resource, int left, int top, int u, int v, int width, int height) {
        mc.getTextureManager().bindTexture(resource);
        top = screenTop + top; //Adjust for the screenHeight
        drawTexturedModalRect(left, top, u, v, width, height);
        GlStateManager.color(1F, 1F, 1F); //Fix Colours
    }

    public void drawLine(int left, int top, int right, int bottom, int thickness, int color) {
        top = screenTop + top; //Adjust
        bottom = screenTop + bottom; //Adjust

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

    public Theme getTheme() {
        return THEME;
    }

    public void setZLevel(float f) {
        zLevel = f;
        itemRender.zLevel = f;
    }
}
