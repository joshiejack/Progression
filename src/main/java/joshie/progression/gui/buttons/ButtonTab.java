package joshie.progression.gui.buttons;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import joshie.progression.Progression;
import joshie.progression.api.ICriteria;
import joshie.progression.api.ITab;
import joshie.progression.gui.GuiTreeEditor;
import joshie.progression.gui.editors.EditText;
import joshie.progression.gui.editors.EditText.ITextEditable;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.editors.SelectItem;
import joshie.progression.gui.editors.SelectItem.Type;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.lib.GuiIDs;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ButtonTab extends ButtonBase implements ITextEditable, IItemSelectable {
    private ITab tab;

    public ButtonTab(ITab tab, int x, int y) {
        super(0, x, y, 25, 25, "");
        this.tab = tab;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        boolean hovering = hovered = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
        int k = getHoverState(hovering);
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
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
        GuiTreeEditor.INSTANCE.switching = true; ///Don't save yet
        MCClientHelper.getPlayer().closeScreen(); //Close everything first
        //If the tab is already selected, then we should edit it instead        
        int x = Mouse.getX();
        int y = Mouse.getY();

        if (MCClientHelper.canEdit()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                ITab newTab = GuiTreeEditor.INSTANCE.currentTab;
                if (tab == GuiTreeEditor.INSTANCE.currentTab) {
                    newTab = GuiTreeEditor.INSTANCE.previousTab;
                }

                if (newTab != null) {
                    if (!APIHandler.getTabs().containsKey(newTab.getUniqueName())) {
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
                    APIHandler.removeCriteria(c.getUniqueName(), true);
                }

                APIHandler.getTabs().remove(tab.getUniqueName()); //Reopen after removing
                MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.TREE, MCClientHelper.getWorld(), 0, 0, 0);
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
        GuiTreeEditor.INSTANCE.currentTabName = tab.getUniqueName(); //Reopen the gui
        MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.TREE, MCClientHelper.getWorld(), 0, 0, 0);

        //Woo
        Mouse.setCursorPosition(x, y);
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
    public void setObject(Object stack) {
        if (stack instanceof ItemStack) {
            tab.setStack(((ItemStack) stack).copy());
        }
    }
}
