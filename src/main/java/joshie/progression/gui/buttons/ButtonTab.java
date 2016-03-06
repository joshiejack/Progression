package joshie.progression.gui.buttons;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Tab;
import joshie.progression.gui.GuiTreeEditor;
import joshie.progression.gui.editors.EditText;
import joshie.progression.gui.editors.EditText.ITextEditable;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.editors.SelectItem;
import joshie.progression.gui.editors.SelectItem.Type;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ButtonTab extends ButtonBase implements ITextEditable, IItemSelectable {
    private Tab tab;

    public ButtonTab(Tab tab, int x, int y) {
        super(0, x, y, 25, 25, "");
        this.tab = tab;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        boolean hovering = hovered = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(ProgressionInfo.textures);
        int yTexture = GuiTreeEditor.INSTANCE.currentTab == tab ? 25 : 0;
        RenderHelper.disableStandardItemLighting();
        int xTexture = 206;
        if (xPosition == 0) xTexture = 231;
        drawTexturedModalRect(xPosition, yPosition, xTexture, yTexture, 25, 25);
        if (xPosition == 0) {
            RenderItemHelper.drawStack(tab.getStack(), xPosition + 2, yPosition + 5, 1F);
        } else RenderItemHelper.drawStack(tab.getStack(), xPosition + 7, yPosition + 5, 1F);

        boolean displayTooltip = false;
        if (MCClientHelper.canEdit()) {
            displayTooltip = EditText.INSTANCE.getEditable() == this;
        }

        if (k == 2 || displayTooltip) {
            ArrayList<String> name = new ArrayList();
            String hidden = tab.isVisible() ? "" : "(Hidden)";
            name.add(EditText.INSTANCE.getText(this) + hidden);
            if (MCClientHelper.canEdit()) {
                name.add(EnumChatFormatting.GRAY + "(Sort Index) " + tab.getSortIndex());
                name.add(EnumChatFormatting.GRAY + "Shift + Click to rename");
                name.add(EnumChatFormatting.GRAY + "Ctrl + Click to select item icon");
                name.add(EnumChatFormatting.GRAY + "I + Click to hide/unhide");
                name.add(EnumChatFormatting.GRAY + "Arrow keys to move up/down");
                name.add(EnumChatFormatting.GRAY + "Delete + Click to delete");
                name.add(EnumChatFormatting.RED + "  Deleting a tab, deletes all criteria in it");
            }

            GuiTreeEditor.INSTANCE.tooltip.clear();
            GuiTreeEditor.INSTANCE.addTooltip(name);
        }
    }

    @Override
    public void onClicked() {
        //If the tab is already selected, then we should edit it instead        
        if (MCClientHelper.canEdit()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                Tab newTab = GuiTreeEditor.INSTANCE.currentTab;
                if (tab == GuiTreeEditor.INSTANCE.currentTab) {
                    newTab = GuiTreeEditor.INSTANCE.previousTab;
                }

                if (newTab != null) {
                    if (!APIHandler.tabs.containsKey(newTab.getUniqueName())) {
                        for (Tab tab : APIHandler.tabs.values()) {
                            newTab = tab;
                            break;
                        }
                    }
                }

                GuiTreeEditor.INSTANCE.selected = null;
                GuiTreeEditor.INSTANCE.previous = null;
                GuiTreeEditor.INSTANCE.lastClicked = null;
                GuiTreeEditor.INSTANCE.currentTab = newTab;
                for (Criteria c : tab.getCriteria()) {
                    APIHandler.removeCriteria(c.uniqueName, true);
                }

                APIHandler.tabs.remove(tab.getUniqueName());
                GuiTreeEditor.INSTANCE.initGui();
                return;
            }

            if (GuiScreen.isShiftKeyDown()) {
                EditText.INSTANCE.select(this);
            } else if (GuiScreen.isCtrlKeyDown()) {
                SelectItem.INSTANCE.select(this, Type.TREE);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                boolean current = tab.isVisible();
                tab.setVisibility(!current);
            }
        }

        GuiTreeEditor.INSTANCE.previousTab = GuiTreeEditor.INSTANCE.currentTab;
        GuiTreeEditor.INSTANCE.currentTab = tab;
        GuiTreeEditor.INSTANCE.currentTabName = tab.getUniqueName();
    }

    @Override
    public void onNotClicked() {
        if (MCClientHelper.canEdit()) {
            if (EditText.INSTANCE.getEditable() == this) {
                EditText.INSTANCE.clear();
            }

            if (SelectItem.INSTANCE.getEditable() == this) {
                SelectItem.INSTANCE.clear();
            }
        }
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
    public void setItemStack(ItemStack stack) {
        tab.setStack(stack);
    }
}
