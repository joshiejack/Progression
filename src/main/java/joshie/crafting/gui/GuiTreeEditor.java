package joshie.crafting.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITab;
import joshie.crafting.api.ITreeEditor;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Options;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiTreeEditor extends GuiBase {
    public static final GuiTreeEditor INSTANCE = new GuiTreeEditor();
    public String currentTabName;
    public ITab currentTab;

    private static class SortIndex implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            ITab tab1 = ((ITab) o1);
            ITab tab2 = ((ITab) o2);
            if (tab1.getSortIndex() == tab2.getSortIndex()) {
                return tab1.getDisplayName().compareTo(tab2.getDisplayName());
            }

            return tab1.getSortIndex() < tab2.getSortIndex() ? 1 : -1;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList = new ArrayList(); //Recreate the button list, in order to reposition it
        int number = 0;
        int pos = y - 5;
        if (ClientHelper.canEdit()) {
            buttonList.add(new ButtonNewCriteria(pos));
            pos += 28;
            number++;
        }

        //Sort tabs alphabetically or by sort index
        ArrayList<ITab> tabs = new ArrayList(CraftAPIRegistry.tabs.values());
        Collections.sort(tabs, new SortIndex());

        for (ITab tab : tabs) {
            if (tab.isVisible() || ClientHelper.canEdit()) {
                if (number <= 8) {
                    buttonList.add(new ButtonTab(tab, 0, pos));
                } else buttonList.add(new ButtonTab(tab, res.getScaledWidth() - 25, pos));

                pos += 28;

                if (number == 8) {
                    pos = y - 5;
                }

                number++;
            }
        }

        if (currentTabName == null) {
            currentTabName = Options.defaultTab;
        }

        currentTab = CraftingAPI.registry.getTabFromName(currentTabName);
        if (currentTab == null) {
            for (ITab tab : CraftAPIRegistry.tabs.values()) {
                currentTab = tab;
                break;
            }

            currentTabName = currentTab.getUniqueName();
        }
    }

    @Override
    public void drawForeground() {
        for (ICriteria criteria : currentTab.getCriteria()) {
            if (criteria.getTreeEditor().isCriteriaVisible() || ClientHelper.canEdit()) {
                ITreeEditor editor = criteria.getTreeEditor();
                List<ICriteria> prereqs = criteria.getRequirements();
                for (ICriteria p : prereqs) {
                    int y1 = p.getTreeEditor().getY();
                    int y2 = editor.getY();
                    int x1 = p.getTreeEditor().getX();
                    int x2 = editor.getX();
                    
                    int width = 0;
                    int textWidth = mc.fontRenderer.getStringWidth(p.getDisplayName());
                    int iconWidth = 9 + (p.getRewards().size() * 12);
                    if (textWidth >= iconWidth) {
                        width = textWidth + 9;
                    } else width = iconWidth;     
                    
                    width -= 3;
                    
                    drawLine(offsetX + width + x1, y + 12 + y1 - 1, offsetX + 5 + x2, y + 12 + y2 - 1, 1, 0xDDB9B9AD);
                    drawLine(offsetX + width + x1, y + 12 + y1 + 1, offsetX + 5 + x2, y + 12 + y2 + 1, 1, 0xFF636C69); //#636C69
                    drawLine(offsetX + width + x1, y + 12 + y1, offsetX + 5 + x2, y + 12 + y2, 1, 0xFFE8EFE7);
                }
            }
        }

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        for (ICriteria criteria : currentTab.getCriteria()) {
            if (criteria.getTreeEditor().isCriteriaVisible() || ClientHelper.canEdit()) {
                ITreeEditor editor = criteria.getTreeEditor();
                editor.draw(0, y, offsetX);
            }
        }

        TreeItemSelect.INSTANCE.draw();
    }

    public ITab previousTab = null;

    @Override
    protected void keyTyped(char character, int key) {
        ICriteria toRemove = null;
        for (ICriteria criteria : currentTab.getCriteria()) {
            if (criteria.getTreeEditor().isCriteriaVisible() || ClientHelper.canEdit()) {
                if (criteria.getTreeEditor().keyTyped(character, key)) {
                    toRemove = criteria;
                    break;
                }
            }
        }

        if (toRemove != null) {
            CraftAPIRegistry.removeCriteria(toRemove.getUniqueName());
        }

        if (ClientHelper.canEdit()) {
            SelectTextEdit.INSTANCE.keyTyped(character, key);

            if (SelectItemOverlay.INSTANCE.getEditable() != null) {
                TreeItemSelect.INSTANCE.keyTyped(character, key);
            }
        }

        if (key == Keyboard.KEY_UP) {
            currentTab.setSortIndex(currentTab.getSortIndex() + 1);
            this.initGui();
        } else if (key == Keyboard.KEY_DOWN) {
            currentTab.setSortIndex(currentTab.getSortIndex() - 1);
            this.initGui();
        }

        super.keyTyped(character, key);
    }

    @Override
    public void mouseMovedOrUp(int x, int y, int button) {
        for (ICriteria criteria : currentTab.getCriteria()) {
            criteria.getTreeEditor().release(mouseX, mouseY);
        }
    }

    private long lastClick;
    private int lastType;
    ICriteria lastClicked = null;

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        if (currentTab == null) return;
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = par3 == 0 && lastType == 0 && difference <= 500;
        lastClick = System.currentTimeMillis();
        lastType = par3;

        lastClicked = null;
        if (SelectItemOverlay.INSTANCE.getEditable() != null) {
            TreeItemSelect.INSTANCE.mouseClicked(mouseX, mouseY);
        }

        super.mouseClicked(par1, par2, par3);
        boolean clicked = false;
        for (ICriteria criteria : currentTab.getCriteria()) {
            if (criteria.getTreeEditor().isCriteriaVisible() || ClientHelper.canEdit()) {
                if (criteria.getTreeEditor().click(mouseX, mouseY, isDoubleClick)) {
                    if (!clicked) {
                        lastClicked = criteria;
                    }
                }
            }
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        for (ICriteria criteria : currentTab.getCriteria()) {
            criteria.getTreeEditor().follow(mouseX, mouseY);
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                criteria.getTreeEditor().scroll(wheel < 0);
            }
        }
    }
}
