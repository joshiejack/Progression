package joshie.progression.gui.newversion.overlays;

import joshie.progression.gui.newversion.GuiCore;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DrawFeatureHelper {
	protected GuiCore guiDraw;
	protected int screenTop; // Used for the helper methods in this class
	protected int offsetX; //OffsetX on the scroll position
	
	public DrawFeatureHelper(GuiCore core) {
		guiDraw = core;
	}
	
	public GuiCore getGui() {
		return guiDraw;
	}
	
    public Theme getTheme() {
        return guiDraw.getTheme();
    }
	
	public void configure() {
		screenTop = guiDraw.top; // Mark the top of the screen
		offsetX = guiDraw.offsetX; //Scroll position guiDraw.ySize; // Set that gui height
	}
	
	public void drawText(String text, int left, int top, int color) {
        guiDraw.mc.fontRendererObj.drawString(text, left, screenTop + top, color);
    }
	
	public void drawSplitText(String text, int left, int top, int width, int color) {
	    guiDraw.mc.fontRendererObj.drawSplitString(text, left, screenTop + top, width, color);
    }

	public void drawRectangle(int left, int top, int width, int height, int color, int border) {
		guiDraw.drawRectWithBorder(left, screenTop + top, left + width, screenTop + top + height, color, border);
	}
	
	public void drawGradient(int left, int top, int width, int height, int color, int color2, int border) {
        guiDraw.drawGradientRectWithBorder(left, screenTop + top, left + width, screenTop + top + height, color, color2, border);
    }
	
	public void drawStack(ItemStack stack, int left, int top, float scale) {
        guiDraw.drawStack(stack, left, screenTop + top, scale);
    }
	
	public void drawText(int renderX, int renderY, String text, int left, int top, int color) {
        guiDraw.mc.fontRendererObj.drawString(text, offsetX + left + renderX, screenTop + top + renderY, color);
    }
	
	public void drawSplitText(int renderX, int renderY, String text, int left, int top, int width, int color) {
        guiDraw.mc.fontRendererObj.drawSplitString(text, offsetX + left + renderX, screenTop + top + renderY, width, color);
    }

    public void drawRectangle(int renderX, int renderY, int left, int top, int width, int height, int color, int border) {
        guiDraw.drawRectWithBorder(offsetX + left + renderX, screenTop + top + renderY, offsetX + left + renderX + width, screenTop + top + renderY + height, color, border);
    }
    
    public void drawGradient(int renderX, int renderY, int left, int top, int width, int height, int color, int color2, int border) {
        guiDraw.drawGradientRectWithBorder(offsetX + left + renderX, screenTop + top + renderY, offsetX + left + renderX + width, screenTop + top + renderY + height, color, color2, border);
    }

    public void drawStack(int renderX, int renderY, ItemStack stack, int left, int top, float scale) {
        guiDraw.drawStack(stack, offsetX + left + renderX, screenTop + top + renderY, scale);
    }

    public void drawTexture(int renderX, int renderY, ResourceLocation resource, int left, int top, int u, int v, int width, int height) {
        guiDraw.mc.getTextureManager().bindTexture(resource);
        guiDraw.drawTexturedModalRect(offsetX + left + renderX, screenTop + top + renderY, u, v, width, height);
    }
}
