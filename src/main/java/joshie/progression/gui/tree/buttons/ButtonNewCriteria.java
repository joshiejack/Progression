package joshie.progression.gui.tree.buttons;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.GuiTreeEditor;
import joshie.progression.handlers.APIHandler;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.UUID;

public class ButtonNewCriteria extends ButtonBase {
    public ButtonNewCriteria(int y) {
        super(0, 0, y, 25, 25, "");
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        boolean hovering = hovered = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
        int yTexture = k == 2 ? 25 : 0;
        GuiCore.INSTANCE.drawTexture(ProgressionInfo.textures, xPosition, yPosition, 231, yTexture, 25, 25);
        GuiCore.INSTANCE.drawText("New", xPosition + 2, yPosition + 9, Theme.INSTANCE.newButtonFontColor);
        if (k == 2) {
            ArrayList<String> name = new ArrayList();
            name.add(EnumChatFormatting.WHITE + "New");
            name.add(EnumChatFormatting.GRAY + "Click for a New Criteria");
            name.add(EnumChatFormatting.GRAY + "Shift Click for a New Tab");
            FeatureTooltip.INSTANCE.addTooltip(name);
        }
    }

    @Override
    public void onClicked() {
        GuiCore.INSTANCE.clickedButton = true;
        
        if (GuiScreen.isShiftKeyDown()) {
            APIHandler.newTab(UUID.randomUUID()).setDisplayName("New Tab").setStack(new ItemStack(Items.book)).setVisibility(true);
            GuiCore.INSTANCE.initGui();
        } else {
            GuiTreeEditor.INSTANCE.previous = null;
            GuiTreeEditor.INSTANCE.selected = null;
            GuiTreeEditor.INSTANCE.lastClicked = null;
            GuiTreeEditor.INSTANCE.isDragging = false;
            ITab currentTab = GuiTreeEditor.INSTANCE.currentTab;
            int mouseX = GuiCore.INSTANCE.mouseX;
            int mouseY = GuiCore.INSTANCE.mouseY;
            int offsetX = GuiCore.INSTANCE.offsetX;
            ICriteria criteria = APIHandler.newCriteria(currentTab, UUID.randomUUID(), true);
            criteria.setCoordinates(mouseX - 50 - offsetX, mouseY - 10);
            GuiTreeEditor.INSTANCE.addCriteria(criteria, mouseX - 50, mouseY - 10, offsetX);
        }
    }
}
