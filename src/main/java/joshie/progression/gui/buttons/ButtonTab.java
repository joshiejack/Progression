package joshie.progression.gui.buttons;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.api.gui.Position;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.editors.ITextEditable;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.handlers.APICache;
import joshie.progression.handlers.RuleHandler;
import joshie.progression.handlers.TemplateHandler;
import joshie.progression.helpers.AchievementHelper;
import joshie.progression.helpers.FileHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.lib.PInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static joshie.progression.Progression.translate;
import static joshie.progression.api.special.DisplayMode.EDIT;
import static joshie.progression.gui.core.GuiList.*;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.RED;

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
        mc.getTextureManager().bindTexture(PInfo.textures);

        if (isSideways) {
            int yTexture = isBottom ? 234: 206;
            RenderHelper.disableStandardItemLighting();
            int xTexture = TREE_EDITOR.currentTab == tab ? 206 : 231;;
            if (xPosition == 0) xTexture = 206;
            CORE.drawTexture(PInfo.textures, xPosition + CORE.getOffsetX(), yPosition, xTexture, yTexture, 25, 22);

            int stackY = isBottom ? -3: 0;
            if (xPosition == 0) {
                CORE.drawStack(tab.getIcon(), xPosition + CORE.getOffsetX(), yPosition + 5 + stackY, 1F);
            } else CORE.drawStack(tab.getIcon(), xPosition + 4 + CORE.getOffsetX(), yPosition + 5 + stackY, 1F);
        } else {
            int yTexture = TREE_EDITOR.currentTab == tab ? 25 : 0;
            RenderHelper.disableStandardItemLighting();
            int xTexture = 206;
            if (xPosition == 0) xTexture = 231;
            CORE.drawTexture(PInfo.textures, xPosition, yPosition, xTexture, yTexture, 25, 25);
            if (xPosition == 0) {
                CORE.drawStack(tab.getIcon(), xPosition + 2, yPosition + 5, 1F);
            } else CORE.drawStack(tab.getIcon(), xPosition + 7, yPosition + 6, 1F);
        }
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        int xtra = isSideways ? CORE.getOffsetX() : 0;
        boolean hovering = hovered = x >= xPosition + xtra && y >= yPosition && x < xPosition + xtra + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        drawTexture(mc);

        boolean displayTooltip = false;
        if (MODE == EDIT) {
            //displayTooltip = TextEditor.GROUP_EDITOR.getEditable() == this;
        }

        if (k == 2 || displayTooltip) {
            ArrayList<String> name = new ArrayList();
            String hidden = tab.isVisible() ? "" : "(" + translate("tab.hidden") + ")";
            name.add(TEXT_EDITOR_SIMPLE.getText(this) + hidden);
            if (MODE == EDIT) {
                name.add(GRAY + "(" + translate("tab.sort") + ") " + tab.getSortIndex());
                if (!Options.hideTooltips) {
                    name.add(GRAY + translate("tab.shift"));
                    name.add(GRAY + translate("tab.ctrl"));
                    name.add(GRAY + translate("tab.alt"));
                    name.add(GRAY + translate("tab.i"));
                    name.add(GRAY + translate("tab.s"));
                    name.add(GRAY + translate("tab.arrow"));
                    name.add(GRAY + translate("tab.delete"));
                    name.add(RED + "  " + translate("tab.warning"));
                }
            }

            TOOLTIP.clear();
            TOOLTIP.add(name);
        }
    }

    @Override
    public void onClicked() {
        CORE.clickedButton = true;
        //MCClientHelper.getPlayer().closeScreen(); //Close everything first
        //If the tab is already selected, then we should edit it instead        

        boolean donestuff = false;
        if (MODE == EDIT) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                ITab newTab = TREE_EDITOR.currentTab;
                if (tab == TREE_EDITOR.currentTab) {
                    newTab = TREE_EDITOR.previousTab;
                }

                if (newTab != null) {
                    if (!APICache.getClientCache().getTabIDs().contains(newTab.getUniqueID())) {
                        for (ITab tab : APICache.getClientCache().getTabSet()) {
                            newTab = tab;
                            break;
                        }
                    }
                }

                TREE_EDITOR.selected = null;
                TREE_EDITOR.previous = null;
                TREE_EDITOR.lastClicked = null;
                TREE_EDITOR.currentTab = newTab;
                for (ICriteria c : tab.getCriteria()) {
                    RuleHandler.removeCriteria(c.getUniqueID(), true);
                }

                APICache.getClientCache().removeTab(tab); //Reopen after removing#
                TREE_EDITOR.onClientSetup();
                CORE.setEditor(TREE_EDITOR);
                return;
            }

            if (GuiScreen.isShiftKeyDown()) {
                TEXT_EDITOR_SIMPLE.setEditable(this);
                donestuff = true;
            } else if (GuiScreen.isCtrlKeyDown() || ITEM_EDITOR.isVisible()) {
                ITEM_EDITOR.select(FilterTypeItem.INSTANCE, this, Position.TOP);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                boolean current = tab.isVisible();
                tab.setVisibility(!current);
                donestuff = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                if(TemplateHandler.registerTab(JSONLoader.getDataTabFromTab(tab))) {
                    JSONLoader.saveJSON(FileHelper.getTemplatesFolder("tab", tab.getLocalisedName() + "_" + tab.getUniqueID()), JSONLoader.getDataTabFromTab(tab), true, false);
                    AchievementHelper.display(tab.getIcon(), "Saved " + tab.getLocalisedName());
                }
            } else if (GuiScreen.isAltKeyDown()) {
                Options.settings.defaultTabID = tab.getUniqueID();
            }
        }

        if (!donestuff) {
            TREE_EDITOR.previousTab = TREE_EDITOR.currentTab;
            TREE_EDITOR.currentTab = tab;
            TREE_EDITOR.currentTabID = tab.getUniqueID(); //Reopen the gui
        }

        //Rebuild
        TREE_EDITOR.rebuildCriteria();
    }

    @Override
    public void onNotClicked() {

    }

    @Override
    public String getTextField() {
        return tab.getLocalisedName();
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
