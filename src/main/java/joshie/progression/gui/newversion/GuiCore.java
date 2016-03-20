package joshie.progression.gui.newversion;

import static joshie.progression.network.core.PacketPart.SEND_SIZE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import joshie.progression.Progression;
import joshie.progression.api.fields.IField;
import joshie.progression.gui.newversion.overlays.FeatureBackground;
import joshie.progression.gui.newversion.overlays.FeatureFooter;
import joshie.progression.gui.newversion.overlays.FeatureFullTextEditor;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.FeatureNew;
import joshie.progression.gui.newversion.overlays.FeatureNewCondition;
import joshie.progression.gui.newversion.overlays.FeatureNewItemFilter;
import joshie.progression.gui.newversion.overlays.FeatureNewReward;
import joshie.progression.gui.newversion.overlays.FeatureNewTrigger;
import joshie.progression.gui.newversion.overlays.FeatureTooltip;
import joshie.progression.gui.newversion.overlays.IGuiFeature;
import joshie.progression.gui.newversion.overlays.TextEditor;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.json.Theme;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncJSONToServer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class GuiCore extends GuiScreen {
    protected ArrayList<IGuiFeature> features = new ArrayList<IGuiFeature>();
    protected HashMap<String, IField> fields = new HashMap();
    private HashMap<Object, Integer> offsetCache = new HashMap();
    private ResourceLocation lastResource;
    private int mouseX = 0;
    private int mouseY = 0;
    protected Theme theme;

    public int offsetX; // Offset position
    public int ySize = 240; // Height of the Gui :O
    public int screenTop; // Top of the screen :D
    public int screenWidth; //Width of the screen
    public boolean switching = false;
    public boolean scrollingEnabled = true;

    public abstract int getPreviousGuiID();

    @Override
    public void initGui() {
        switching = false;
        screenWidth = new ScaledResolution(mc).getScaledWidth();
        Keyboard.enableRepeatEvents(true);
        theme = Theme.INSTANCE;
        if (offsetCache.containsKey(getKey())) {
            offsetX = offsetCache.get(getKey());
        } else offsetX = 0;

        features.clear(); // Clear out the features
        features.add(new FeatureBackground()); // Readd the background
        initGuiData(); // Add extra features
        features.add(new FeatureFooter()); // Add the footer

        // Init all the features
        FeatureTooltip.INSTANCE.init(this); //Damn you tooltip feature!
        for (IGuiFeature feature : features) {
            feature.init(this);
        }
    }

    public void clearEditors() {
        FeatureItemSelector.INSTANCE.clearEditable();
        FeatureFullTextEditor.INSTANCE.clearEditable();
        TextEditor.INSTANCE.clearEditable();
        FeatureNewTrigger.INSTANCE.setVisibility(false);
        FeatureNewReward.INSTANCE.setVisibility(false);
        FeatureNewItemFilter.INSTANCE.setVisibility(false);
        FeatureNewCondition.INSTANCE.setVisibility(false);
    }

    public void initGuiData() {}

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
            clearEditors();
        }
    }

    @Override
    public void drawScreen(int cursorX, int cursorY, float partialTicks) {
        lastResource = null; //Reset the lastResource
        FeatureTooltip.INSTANCE.clear();
        screenTop = (height - ySize) / 2;
        boolean overlayvisible = false;
        for (IGuiFeature feature : features) {
            if (feature.isVisible() && !feature.isOverlay()) { //Only draw visible stuff
                feature.draw(cursorX, cursorY - screenTop);
            }

            if (feature.isVisible() && feature.isOverlay()) overlayvisible = true;
        }

        drawGuiForeground(overlayvisible, mouseX, mouseY - screenTop);

        for (IGuiFeature feature : features) {
            if (feature.isVisible() && feature.isOverlay()) { //Only new Stuff
                feature.draw(cursorX, cursorY - screenTop);
            }
        }

        //Draw the tooltip in the right place
        FeatureTooltip.INSTANCE.drawFeature(cursorX, cursorY);
    }

    public abstract void drawGuiForeground(boolean overlayvisible, int cursorX, int cursorY);

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        boolean overlayvisible = false;
        if (button == 0) {
            for (IGuiFeature feature : features) {
                if (feature.isVisible()) { //Don't process hidden features
                    if (feature.mouseClicked(mouseX, mouseY - screenTop, button)) {
                        return; // Don't continue if a mouse click was processed
                    }

                    if (feature.isOverlay()) overlayvisible = true;
                }
            }
        }

        if (guiMouseClicked(overlayvisible, mouseX, mouseY - screenTop, button)) {
            return;
        }

        if (button == 1) {
            if (!FeatureNew.IS_OPEN) {
                switching = true;
                mc.thePlayer.openGui(Progression.instance, getPreviousGuiID(), mc.theWorld, 0, 0, 0);
            }
        }

        clearEditors();
    }

    public abstract boolean guiMouseClicked(boolean overlayvisible, int mouseX, int mouseY, int button);

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        int jump = 1;
        if (Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42)) {
            jump = 50; //Shift Jump
        }

        if (!TextEditor.INSTANCE.isEditing()) {
            if (key == 203) {
                scroll(jump);
            } else if (key == 205) {
                scroll(-jump);
            }
        }

        TextEditor.INSTANCE.keyTyped(character, key);
        super.keyTyped(character, key);
    }

    @Override
    public void handleMouseInput() throws IOException {
        mouseX = Mouse.getEventX() * width / mc.displayWidth;
        mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        int wheel = Mouse.getDWheel();
        boolean down = wheel < 0;
        if (wheel != 0) {
            boolean scrolled = false;
            for (IGuiFeature feature : features) {
                if (feature.isVisible()) { //Must be visible of course!
                    if (feature.scroll(mouseX, mouseY - screenTop, down)) {
                        scrolled = true;
                        break;
                    }
                }
            }

            if (!scrolled) {
                if (!down) {
                    scroll(20);
                } else {
                    scroll(-20);
                }
            }
        }
        super.handleMouseInput();
    }

    public Object getKey() {
        return this;
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
    
    public void drawRightAlignedText(String text, int left, int top, int color) {
        drawRightAlignedText(text, left, top, color, 1F);
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
        GlStateManager.color(1F, 1F, 1F); //Fix Colours
        mc.getTextureManager().bindTexture(resource);
        top = screenTop + top; //Adjust for the screenHeight
        drawTexturedModalRect(left, top, u, v, width, height);
    }

    public Theme getTheme() {
        return theme;
    }

    public void setZLevel(float f) {
        zLevel = f;
        itemRender.zLevel = f;
    }
}
