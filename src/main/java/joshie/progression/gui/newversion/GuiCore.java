package joshie.progression.gui.newversion;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import joshie.progression.Progression;
import joshie.progression.gui.base.SaveTicker;
import joshie.progression.gui.newversion.overlays.FeatureBackground;
import joshie.progression.gui.newversion.overlays.FeatureFooter;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.FeatureTooltip;
import joshie.progression.gui.newversion.overlays.IGuiFeature;
import joshie.progression.gui.newversion.overlays.TextEditor;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.json.Theme;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

public class GuiCore extends GuiScreen {
    protected ArrayList<IGuiFeature> features = new ArrayList<IGuiFeature>();
    private int mouseX = 0;
    private int mouseY = 0;
    protected Theme theme;

    public int offsetX; // Offset position
    public int ySize = 240; // Height of the Gui :O
    public int top; // Top of the screen :D

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        theme = Theme.INSTANCE;
        features.clear(); // Clear out the features
        features.add(new FeatureBackground()); // Readd the background
        addFeatures(); // Add extra features
        features.add(new FeatureFooter()); // Add the footer

        // Init all the features
        FeatureTooltip.INSTANCE.init(this); //Damn you tooltip feature!
        for (IGuiFeature feature : features) {
            feature.init(this);
        }
    }
    
    public void clearEditors() {
        FeatureItemSelector.INSTANCE.clearEditable();
        TextEditor.INSTANCE.clearEditable();
    }

    public void addFeatures() {}

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (MCClientHelper.isInEditMode()) {
            if (Options.debugMode) {
                Progression.logger.log(Level.INFO, "Saving JSON Data");
            }

            JSONLoader.saveData();
            SaveTicker.LAST_TICK = 500;
            clearEditors();
        }
    }

    @Override
    public void drawScreen(int cursorX, int cursorY, float partialTicks) {
        FeatureTooltip.INSTANCE.clear();
        top = (height - ySize) / 2;
        for (IGuiFeature feature : features) {
            if (feature.isVisible()) { //Only draw visible stuff
                feature.draw(cursorX, cursorY - top);
            }
        }
        
        //Draw the tooltip in the right place
        FeatureTooltip.INSTANCE.drawFeature(cursorX, cursorY);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        for (IGuiFeature feature : features) {
            if (feature.isVisible()) { //Don't process hidden features
                if (feature.mouseClicked(mouseX, mouseY - top, button)) {
                    return; // Don't continue if a mouse click was processed
                }
            }
        }
        
       clearEditors();
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        int jump = 1;
        if (Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42)) {
            jump = 50; //Shift Jump
        }

        if (key == 203) {
            scroll(jump);
        } else if (key == 205) {
            scroll(-jump);
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
                    if (feature.scroll(mouseX, mouseY - top, down)) {
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

    // Scrolling Helpers
    public void scroll(int amount) {
        offsetX += amount;
        if (offsetX >= 0) {
            offsetX = 0;
        }
    }

    // Helper Drawing functions
    // Regular Rectangle
    public void drawRectWithBorder(int left, int top, int right, int bottom, int color, int border) {
        drawRect(left, top, right, bottom, color);
        drawRect(left, top, left + 1, bottom, border);
        drawRect(right - 1, top, right, bottom, border);
        drawRect(left, top, right, top + 1, border);
        drawRect(left, bottom - 1, right, bottom, border);
    }

    // Gradient Rectangle
    public void drawGradientRectWithBorder(int left, int top, int right, int bottom, int startColor, int endColor, int border) {
        drawGradientRect(left, top, right, bottom, startColor, endColor);
        drawRect(left, top, left + 1, bottom, border);
        drawRect(right - 1, top, right, bottom, border);
        drawRect(left, top, right, top + 1, border);
        drawRect(left, bottom - 1, right, bottom, border);
    }
    
    //Gradient rect
    @Override
    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    public void drawStack(ItemStack stack, int x, int y, float scale) {
        RenderItemHelper.drawStack(stack, x, y, scale);
    }

    public Theme getTheme() {
        return theme;
    }

    public void setZLevel(float f) {
        zLevel = f;
        itemRender.zLevel = f;
    }
}
