package joshie.crafting.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Mouse;

public class RelativeScreen extends GuiScreen {    
    public ScaledResolution resolution;
    private int left; //The left
    private int top; //The top
    private int width; //The width, Relative scaling to this
    protected float scale; //The scale of items
    
    private int mouseRelX; //The mouses relative left/right position
    private int mouseRelY; //The mouses relative up/down position
    
    @Override
    public void initGui() {
        resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        
        if (mc.displayWidth >= 1024) {
            set((int) ((mc.displayWidth / 2D) - 512), 60, 1024);
        } else set((int) 0, 60, mc.displayWidth);
    }
    
    /** Sets up the data we need to know, with regard to left, top and width of screen **/
    public void set(int left, int top, int width) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.scale = (width / 1024F);
    }
    
    /** Draws a rectangle, adjusted to resolution **/
    public void drawRectangle(int x, int y, int x2, int y2, int color) {
        if (width < 1024) { //Scale the screen if our resolution is less than 1024 pixels.
            drawRect((int)((x / 1024D) * width), (int)(((top + y) / 1024D) * width), (int)((x2 / 1024D) * width), (int)(((top + y2) / 1024D) * width), color);
        } else drawRect(left + x, top + y, left + x2, top + y2, color);
    }
    
    /** Calculates mouse coordinates adjusted to resolution **/
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (width < 1024) { //Relative MouseX based on ScreenSize
            int x = Mouse.getEventX() * super.width / mc.displayWidth; //Half of the width
            mouseRelX = (int) (((x * resolution.getScaleFactor()) / (double)mc.displayWidth) * 1024D);
            int y = super.height - Mouse.getEventY() * super.height / mc.displayHeight - 1;
            mouseRelY = (int) (((y * resolution.getScaleFactor() / (double)mc.displayWidth) * 1024D)) - top + 2;
        } else {
            mouseRelX = ((Mouse.getEventX() * super.width / mc.displayWidth) * resolution.getScaleFactor()) - left;
            mouseRelY = ((mc.displayHeight - Mouse.getEventY())) - top;
        }
    }
}
