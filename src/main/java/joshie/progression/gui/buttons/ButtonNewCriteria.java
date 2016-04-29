package joshie.progression.gui.buttons;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.handlers.RuleHandler;
import joshie.progression.json.Options;
import joshie.progression.lib.PInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.UUID;

import static joshie.progression.gui.core.GuiList.*;

public class ButtonNewCriteria extends ButtonBase {
    public ButtonNewCriteria(int x, int y) {
        super(0, x, y, 25, 25, "");
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        int xtra = isSideways ? CORE.getOffsetX() : 0;
        boolean hovering = hovered = x >= xPosition + xtra && y >= yPosition && x < xPosition + xtra + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);

        if (!isSideways) {
            int yTexture = k == 2 ? 25 : 0;
            CORE.drawTexture(PInfo.textures, xPosition, yPosition, 231, yTexture, 25, 25);
            CORE.drawText("New", xPosition + 2, yPosition + 9, THEME.newButtonFontColor);
        } else {
            int xTexture = k == 2 ? 206 : 231;;
            CORE.drawTexture(PInfo.textures, xPosition + CORE.getOffsetX(), yPosition, xTexture, 206, 25, 22);
            CORE.drawText("New", xPosition + 4 + CORE.getOffsetX(), yPosition + 8, THEME.newButtonFontColor);
        }

        if (k == 2 && !Options.hideTooltips) {
            ArrayList<String> name = new ArrayList();
            name.add(EnumChatFormatting.WHITE + "New");
            name.add(EnumChatFormatting.GRAY + "Click for a New Criteria");
            name.add(EnumChatFormatting.GRAY + "Shift click for a New Tab");
            name.add(EnumChatFormatting.GRAY + "Ctrl click to load Criteria Template");
            name.add(EnumChatFormatting.GRAY + "Ctrl + Shift to load Tab Template");
            name.add(EnumChatFormatting.GRAY + "");
            name.add(EnumChatFormatting.GRAY + "You can also double click empty");
            name.add(EnumChatFormatting.GRAY + "space to insert a new criteria");
            TOOLTIP.add(name);
        }
    }

    @Override
    public void onClicked() {
        CORE.clickedButton = true;

        if(GuiScreen.isCtrlKeyDown() && GuiScreen.isShiftKeyDown()) {
            TEMPLATE_SELECTOR_TAB.setVisible();
        } else if (GuiScreen.isCtrlKeyDown()) {
            TEMPLATE_SELECTOR_CRITERIA.setVisible();
        } else if (GuiScreen.isShiftKeyDown()) {
            RuleHandler.newTab(UUID.randomUUID(), true).setDisplayName("New Tab").setStack(new ItemStack(Items.book)).setVisibility(true);
            CORE.initGui();
        } else {
            TREE_EDITOR.previous = null;
            TREE_EDITOR.selected = null;
            TREE_EDITOR.lastClicked = null;
            TREE_EDITOR.isDragging = false;
            ITab currentTab = TREE_EDITOR.currentTab;
            int mouseX = CORE.mouseX;
            int mouseY = CORE.mouseY;
            int offsetX = CORE.getOffsetX();
            ICriteria criteria = RuleHandler.newCriteria(currentTab, UUID.randomUUID(), true);
            criteria.setCoordinates(mouseX - 50 - offsetX, mouseY - 10);
            TREE_EDITOR.addCriteria(criteria, mouseX - 50, mouseY - 10, offsetX);
        }
    }
}
