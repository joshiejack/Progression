package joshie.progression.gui;

import java.util.ArrayList;

import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Tab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

public class ButtonNewCriteria extends ButtonBase {
    public ButtonNewCriteria(int y) {
        super(0, 0, y, 25, 25, "");
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        boolean hovering = field_146123_n = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(ProgressionInfo.textures);
        int yTexture = k == 2 ? 25 : 0;
        drawTexturedModalRect(xPosition, yPosition, 231, yTexture, 25, 25);
        mc.fontRenderer.drawString("New", xPosition + 2, yPosition + 9, Theme.INSTANCE.newButtonFontColor);
        if (k == 2) {
            ArrayList<String> name = new ArrayList();
            name.add(EnumChatFormatting.WHITE + "New");
            name.add(EnumChatFormatting.GRAY + "Click and Drag for Criteria");
            name.add(EnumChatFormatting.GRAY + "Shift Click for a Tab");
            GuiTreeEditor.INSTANCE.addTooltip(name);
        }
    }

    @Override
    public void onClicked() {
        if (GuiScreen.isShiftKeyDown()) {
            APIHandler.newTab(APIHandler.getNextUnique()).setDisplayName("New Tab").setStack(new ItemStack(Items.book)).setVisibility(true);
            GuiTreeEditor.INSTANCE.initGui();
        } else {
            GuiTreeEditor.INSTANCE.previous = null;
            GuiTreeEditor.INSTANCE.selected = null;
            GuiTreeEditor.INSTANCE.lastClicked = null;
            GuiTreeEditor.INSTANCE.isDragging = false;
            Tab currentTab = GuiTreeEditor.INSTANCE.currentTab;
            int mouseX = GuiTreeEditor.INSTANCE.mouseX;
            int mouseY = GuiTreeEditor.INSTANCE.mouseY;
            int offsetX = GuiTreeEditor.INSTANCE.offsetX;
            Criteria criteria = APIHandler.newCriteria(currentTab, APIHandler.getNextUnique(), true);
            criteria.treeEditor.setCoordinates(mouseX - 50 - offsetX, mouseY - 10);
            criteria.treeEditor.draw(mouseX - 50, mouseY - 10, offsetX);
            criteria.treeEditor.click(mouseX - 50, mouseY - 10, false);
        }
    }
}
