package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.ICriteria;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiEditor extends GuiScreen {
    public static final GuiEditor INSTANCE = new GuiEditor();
    public int mouseX = 0;
    public int mouseY = 0;

    protected int leftX = 212;
    protected int rightX = 218;
    protected int xSize = 430;
    protected int ySize = 217;
    private ICriteria criteria = null; //The criteria we are currently editing, if this is null, then we're in the 'create/organise' criteria view'

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
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);
    }

    public void drawScreen(int i, int j, float f) {
        int x = (width - 430) / 2;
        int y = (height - ySize) / 2;
        drawRect(x, y, x + leftX + rightX, y + ySize, 0xFFCCCCCC);
        //Top Border
        drawRect(x, y, x + leftX + rightX, y + 1, 0xFF000000);
        drawRect(x, y + ySize - 1, x + leftX + rightX, y + ySize, 0xFF000000);
        drawRect(x, y, x + 1, y + ySize, 0xFF000000);
        drawRect(x + leftX + rightX - 1, y, x + leftX + rightX, y + ySize, 0xFF000000);
        if (criteria == null) {
            drawCriteria(x, y);
        }
        
        super.drawScreen(i, j, f);
    }
    
    private void drawRectWithBorder(int x, int y, int width, int height, int color, int border) {
        x += 5;
        y += 5;
        
        drawRect(x, y, x + width, y + height, color);
        drawRect(x, y, x + 1, y + height, border);
        drawRect(x + width - 1, y, x + width, y + height, border);
        drawRect(x, y, x + width, y + 1, border);
        drawRect(x, y + height - 1, x + width, y + height, border);
    }

    private void drawCriteria(int x, int y) {
        for (ICriteria criteria: CraftAPIRegistry.criteria.values()) {            
            drawRectWithBorder(x + criteria.getX(), y + criteria.getY(), 50, 25, 0xFFFFFFFF, 0xFF000000);
            mc.fontRenderer.drawString(criteria.getUniqueName(), x + 7 + criteria.getX(), y + 7 + criteria.getY(), 0xFF000000);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
    }

    @Override
    public void handleMouseInput() {
        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        mouseX = x - (width - xSize) / 2;
        mouseY = y - (height - ySize) / 2;

        super.handleMouseInput();
    }
}
