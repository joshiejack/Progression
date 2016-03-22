package joshie.progression.gui.tree.buttons;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionTab;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.FeatureItemSelector;
import joshie.progression.gui.editors.FeatureItemSelector.Type;
import joshie.progression.gui.editors.GuiTreeEditor;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.editors.ITextEditable;
import joshie.progression.gui.editors.TextEditor;
import joshie.progression.gui.filters.FilterSelectorItem;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ButtonTab extends ButtonBase implements ITextEditable, IItemSelectable {
    private IProgressionTab tab;

    public ButtonTab(IProgressionTab tab, int x, int y) {
        super(0, x, y, 25, 25, "");
        this.tab = tab;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        boolean hovering = hovered = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(ProgressionInfo.textures);
        int yTexture = GuiTreeEditor.INSTANCE.currentTab == tab ? 25 : 0;
        RenderHelper.disableStandardItemLighting();
        int xTexture = 206;
        if (xPosition == 0) xTexture = 231;
        GuiCore.INSTANCE.drawTexture(ProgressionInfo.textures, xPosition, yPosition, xTexture, yTexture, 25, 25);
        if (xPosition == 0) {
            GuiCore.INSTANCE.drawStack(tab.getStack(), xPosition + 2, yPosition + 5, 1F);
        } else GuiCore.INSTANCE.drawStack(tab.getStack(), xPosition + 7, yPosition + 5, 1F);

        boolean displayTooltip = false;
        if (MCClientHelper.isInEditMode()) {
            displayTooltip = TextEditor.INSTANCE.getEditable() == this;
        }

        if (k == 2 || displayTooltip) {
            ArrayList<String> name = new ArrayList();
            String hidden = tab.isVisible() ? "" : "(Hidden)";
            name.add(TextEditor.INSTANCE.getText(this) + hidden);
            if (MCClientHelper.isInEditMode()) {
                name.add(EnumChatFormatting.GRAY + "(Sort Index) " + tab.getSortIndex());
                name.add(EnumChatFormatting.GRAY + "Shift + Click to rename");
                name.add(EnumChatFormatting.GRAY + "Ctrl + Click to select item icon");
                name.add(EnumChatFormatting.GRAY + "I + Click to hide/unhide");
                name.add(EnumChatFormatting.GRAY + "Arrow keys to move up/down");
                name.add(EnumChatFormatting.GRAY + "Delete + Click to delete");
                name.add(EnumChatFormatting.RED + "  Deleting a tab, deletes all criteria in it");
            }

            FeatureTooltip.INSTANCE.clear();
            FeatureTooltip.INSTANCE.addTooltip(name);
        }
    }

    @Override
    public void onClicked() {
        //MCClientHelper.getPlayer().closeScreen(); //Close everything first
        //If the tab is already selected, then we should edit it instead        

        if (MCClientHelper.isInEditMode()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                IProgressionTab newTab = GuiTreeEditor.INSTANCE.currentTab;
                if (tab == GuiTreeEditor.INSTANCE.currentTab) {
                    newTab = GuiTreeEditor.INSTANCE.previousTab;
                }

                if (newTab != null) {
                    if (!APIHandler.getTabs().containsKey(newTab.getUniqueName())) {
                        for (IProgressionTab tab : APIHandler.getTabs().values()) {
                            newTab = tab;
                            break;
                        }
                    }
                }

                GuiTreeEditor.INSTANCE.selected = null;
                GuiTreeEditor.INSTANCE.previous = null;
                GuiTreeEditor.INSTANCE.lastClicked = null;
                GuiTreeEditor.INSTANCE.currentTab = newTab;
                for (IProgressionCriteria c : tab.getCriteria()) {
                    APIHandler.removeCriteria(c.getUniqueName(), true);
                }

                APIHandler.getTabs().remove(tab.getUniqueName()); //Reopen after removing
                GuiCore.INSTANCE.setEditor(GuiTreeEditor.INSTANCE);
                return;
            }

            if (GuiScreen.isShiftKeyDown()) {
                TextEditor.INSTANCE.setEditable(this);
            } else if (GuiScreen.isCtrlKeyDown()) {
                FeatureItemSelector.INSTANCE.select(FilterSelectorItem.INSTANCE, this, Type.TREE);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                boolean current = tab.isVisible();
                tab.setVisibility(!current);
            }
        }

        GuiTreeEditor.INSTANCE.previousTab = GuiTreeEditor.INSTANCE.currentTab;
        GuiTreeEditor.INSTANCE.currentTab = tab;
        GuiTreeEditor.INSTANCE.currentTabName = tab.getUniqueName(); //Reopen the gui
        //MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.TREE, MCClientHelper.getWorld(), 0, 0, 0);
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
