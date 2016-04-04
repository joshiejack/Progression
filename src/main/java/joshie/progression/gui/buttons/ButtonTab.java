package joshie.progression.gui.buttons;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.*;
import joshie.progression.api.gui.Position;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Options;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ButtonTab extends ButtonBase implements ITextEditable, IItemSelectable {
    private ITab tab;
    private boolean isBottom;

    public ButtonTab(ITab tab, int x, int y) {
        super(0, x, y, 25, 25, "");
        this.tab = tab;
    }

    public ButtonTab setBottom() {
        this.isBottom = true;
        return this;
    }

    public void drawTexture(Minecraft mc) {
        mc.getTextureManager().bindTexture(ProgressionInfo.textures);

        if (isSideways) {
            int yTexture = isBottom ? 234: 206;
            RenderHelper.disableStandardItemLighting();
            int xTexture = GuiTreeEditor.INSTANCE.currentTab == tab ? 206 : 231;;
            if (xPosition == 0) xTexture = 206;
            GuiCore.INSTANCE.drawTexture(ProgressionInfo.textures, xPosition + GuiCore.INSTANCE.getOffsetX(), yPosition, xTexture, yTexture, 25, 22);

            int stackY = isBottom ? -3: 0;
            if (xPosition == 0) {
                GuiCore.INSTANCE.drawStack(tab.getStack(), xPosition + GuiCore.INSTANCE.getOffsetX(), yPosition + 5 + stackY, 1F);
            } else GuiCore.INSTANCE.drawStack(tab.getStack(), xPosition + 4 + GuiCore.INSTANCE.getOffsetX(), yPosition + 5 + stackY, 1F);
        } else {
            int yTexture = GuiTreeEditor.INSTANCE.currentTab == tab ? 25 : 0;
            RenderHelper.disableStandardItemLighting();
            int xTexture = 206;
            if (xPosition == 0) xTexture = 231;
            GuiCore.INSTANCE.drawTexture(ProgressionInfo.textures, xPosition, yPosition, xTexture, yTexture, 25, 25);
            if (xPosition == 0) {
                GuiCore.INSTANCE.drawStack(tab.getStack(), xPosition + 2, yPosition + 5, 1F);
            } else GuiCore.INSTANCE.drawStack(tab.getStack(), xPosition + 7, yPosition + 6, 1F);
        }
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        int xtra = isSideways ? GuiCore.INSTANCE.getOffsetX() : 0;
        boolean hovering = hovered = x >= xPosition + xtra && y >= yPosition && x < xPosition + xtra + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        drawTexture(mc);

        boolean displayTooltip = false;
        if (MCClientHelper.isInEditMode()) {
            //displayTooltip = TextEditor.INSTANCE.getEditable() == this;
        }

        if (k == 2 || displayTooltip) {
            ArrayList<String> name = new ArrayList();
            String hidden = tab.isVisible() ? "" : "(" + Progression.translate("tab.hidden") + ")";
            name.add(TextEditor.INSTANCE.getText(this) + hidden);
            if (MCClientHelper.isInEditMode()) {
                name.add(EnumChatFormatting.GRAY + "(" + Progression.translate("tab.sort") + ") " + tab.getSortIndex());
                name.add(EnumChatFormatting.GRAY + Progression.translate("tab.shift"));
                name.add(EnumChatFormatting.GRAY + Progression.translate("tab.ctrl"));
                name.add(EnumChatFormatting.GRAY + Progression.translate("tab.alt"));
                name.add(EnumChatFormatting.GRAY + Progression.translate("tab.i"));
                name.add(EnumChatFormatting.GRAY + Progression.translate("tab.arrow"));
                name.add(EnumChatFormatting.GRAY + Progression.translate("tab.delete"));
                name.add(EnumChatFormatting.RED + "  " + Progression.translate("tab.warning"));
            }

            FeatureTooltip.INSTANCE.clear();
            FeatureTooltip.INSTANCE.addTooltip(name);
        }
    }

    @Override
    public void onClicked() {
        GuiCore.INSTANCE.clickedButton = true;
        //MCClientHelper.getPlayer().closeScreen(); //Close everything first
        //If the tab is already selected, then we should edit it instead        

        boolean donestuff = false;
        if (MCClientHelper.isInEditMode()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                ITab newTab = GuiTreeEditor.INSTANCE.currentTab;
                if (tab == GuiTreeEditor.INSTANCE.currentTab) {
                    newTab = GuiTreeEditor.INSTANCE.previousTab;
                }

                if (newTab != null) {
                    if (!APIHandler.getTabs().containsKey(newTab.getUniqueID())) {
                        for (ITab tab : APIHandler.getTabs().values()) {
                            newTab = tab;
                            break;
                        }
                    }
                }

                GuiTreeEditor.INSTANCE.selected = null;
                GuiTreeEditor.INSTANCE.previous = null;
                GuiTreeEditor.INSTANCE.lastClicked = null;
                GuiTreeEditor.INSTANCE.currentTab = newTab;
                for (ICriteria c : tab.getCriteria()) {
                    APIHandler.removeCriteria(c.getUniqueID(), true);
                }

                APIHandler.getTabs().remove(tab.getUniqueID()); //Reopen after removing
                GuiCore.INSTANCE.setEditor(GuiTreeEditor.INSTANCE);
                return;
            }

            if (GuiScreen.isShiftKeyDown()) {
                TextEditor.INSTANCE.setEditable(this);
                donestuff = true;
            } else if (GuiScreen.isCtrlKeyDown() || FeatureItemSelector.INSTANCE.isVisible()) {
                FeatureItemSelector.INSTANCE.select(FilterTypeItem.INSTANCE, this, Position.TOP);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                boolean current = tab.isVisible();
                tab.setVisibility(!current);
                donestuff = true;
            } else if (GuiScreen.isAltKeyDown()) {
                Options.settings.defaultTabID = tab.getUniqueID();
            }
        }

        if (!donestuff) {
            GuiTreeEditor.INSTANCE.previousTab = GuiTreeEditor.INSTANCE.currentTab;
            GuiTreeEditor.INSTANCE.currentTab = tab;
            GuiTreeEditor.INSTANCE.currentTabID = tab.getUniqueID(); //Reopen the gui
        }

        //Rebuild
        GuiTreeEditor.INSTANCE.rebuildCriteria();
    }

    @Override
    public void onNotClicked() {

    }

    @Override
    public String getTextField() {
        return tab.getDisplayName();
    }

    @Override
    public void setTextField(String str) {
        tab.setDisplayName(str);
    }

    @Override
    public void setObject(Object stack) {
        if (stack instanceof ItemStack) {
            tab.setStack(((ItemStack) stack).copy());
        }
    }
}
