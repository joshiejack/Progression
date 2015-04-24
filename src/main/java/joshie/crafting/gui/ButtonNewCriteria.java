package joshie.crafting.gui;

import java.util.ArrayList;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.Criteria;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITab;
import joshie.crafting.gui.ButtonBase.ButtonLeft;
import joshie.crafting.json.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

public class ButtonNewCriteria extends ButtonLeft {
    public ButtonNewCriteria(int y) {
        super(y);
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        boolean hovering = field_146123_n = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(textures);
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
            CraftingAPI.registry.newTab(CraftAPIRegistry.getNextUnique()).setDisplayName("New Tab").setStack(new ItemStack(Items.book)).setVisibility(true);
            GuiTreeEditor.INSTANCE.initGui();
        } else {
            GuiTreeEditor.INSTANCE.previous = null;
            GuiTreeEditor.INSTANCE.selected = null;
            GuiTreeEditor.INSTANCE.lastClicked = null;
            ITab currentTab = GuiTreeEditor.INSTANCE.currentTab;
            int mouseX = GuiTreeEditor.INSTANCE.mouseX;
            int mouseY = GuiTreeEditor.INSTANCE.mouseY;
            int offsetX = GuiTreeEditor.INSTANCE.offsetX;
            Criteria criteria = CraftingAPI.registry.newCriteria(currentTab, CraftAPIRegistry.getNextUnique()).setDisplayName("New Criteria").setVisibility(true).setIcon(new ItemStack(Blocks.stone));
            criteria.getTreeEditor().setCoordinates(mouseX - 50 - offsetX, mouseY - 10);
            criteria.getTreeEditor().draw(mouseX - 50, mouseY - 10, offsetX);
            criteria.getTreeEditor().click(mouseX - 50, mouseY - 10, false);
        }
    }
}
