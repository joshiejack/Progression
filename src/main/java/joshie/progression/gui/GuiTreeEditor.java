package joshie.progression.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Tab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.json.Options;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiTreeEditor extends GuiBase {
    public static final GuiTreeEditor INSTANCE = new GuiTreeEditor();
    public String currentTabName;
    public Tab currentTab;

    private static class SortIndex implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Tab tab1 = ((Tab) o1);
            Tab tab2 = ((Tab) o2);
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
        ArrayList<Tab> tabs = new ArrayList(APIHandler.tabs.values());
        Collections.sort(tabs, new SortIndex());

        for (Tab tab : tabs) {
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

        currentTab = APIHandler.getTabFromName(currentTabName);
        if (currentTab == null) {
            for (Tab tab : APIHandler.tabs.values()) {
                currentTab = tab;
                break;
            }

            if (currentTab == null) {
                currentTab = APIHandler.newTab(APIHandler.getNextUnique()).setDisplayName("New Tab").setStack(new ItemStack(Items.book)).setVisibility(true);
                currentTabName = currentTab.getUniqueName();
                initGui();
            }
        }

        currentTabName = currentTab.getUniqueName();
    }

    @Override
    public void drawForeground() {
        if (currentTab == null) initGui();
        for (Criteria criteria : currentTab.getCriteria()) {
            if (criteria.treeEditor.isCriteriaVisible() || ClientHelper.canEdit()) {
                EditorTree editor = criteria.treeEditor;
                List<Criteria> prereqs = criteria.prereqs;
                for (Criteria c : prereqs) {
                    int y1 = c.treeEditor.getY();
                    int y2 = editor.getY();
                    int x1 = c.treeEditor.getX();
                    int x2 = editor.getX();

                    int width = 0;
                    int textWidth = mc.fontRenderer.getStringWidth(c.displayName);
                    int iconWidth = 9 + (c.rewards.size() * 12);
                    if (textWidth >= iconWidth) {
                        width = textWidth + 9;
                    } else width = iconWidth;

                    width -= 3;

                    if (c.tab == criteria.tab) {
                        drawLine(offsetX + width + x1, y + 12 + y1 - 1, offsetX + 5 + x2, y + 12 + y2 - 1, 1, theme.connectLineColor1);
                        drawLine(offsetX + width + x1, y + 12 + y1 + 1, offsetX + 5 + x2, y + 12 + y2 + 1, 1, theme.connectLineColor2); //#636C69
                        drawLine(offsetX + width + x1, y + 12 + y1, offsetX + 5 + x2, y + 12 + y2, 1, theme.connectLineColor3);
                    }
                }
            }
        }

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        for (Criteria criteria : currentTab.getCriteria()) {
            if (criteria.treeEditor.isCriteriaVisible() || ClientHelper.canEdit()) {
                criteria.treeEditor.draw(0, y, offsetX);
            }
        }

        TreeItemSelect.INSTANCE.draw();
    }

    public Tab previousTab = null;

    @Override
    protected void keyTyped(char character, int key) {
        Criteria toRemove = null;
        for (Criteria criteria : currentTab.getCriteria()) {
            if (criteria.treeEditor.isCriteriaVisible() || ClientHelper.canEdit()) {
                if (criteria.treeEditor.keyTyped(character, key)) {
                    toRemove = criteria;
                    break;
                }
            }
        }

        if (toRemove != null) {
            APIHandler.removeCriteria(toRemove.uniqueName, false);
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
        for (Criteria criteria : currentTab.getCriteria()) {
            criteria.treeEditor.release(mouseX, mouseY);
        }

        if (button == 0) {
            isDragging = false;
        }
    }

    private long lastClick;
    private int lastType;
    Criteria lastClicked = null;
    private int drag = 0;
    boolean isDragging = false;

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
        for (Criteria criteria : currentTab.getCriteria()) {
            if (criteria.treeEditor.isCriteriaVisible() || ClientHelper.canEdit()) {
                if (criteria.treeEditor.click(mouseX, mouseY, isDoubleClick)) {
                    lastClicked = criteria;
                }
            }
        }

        if (lastClicked == null) {
            isDragging = true;
            drag = mouseX;
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        if (isDragging) {
            int difference = mouseX - drag;
            drag = mouseX;

            if (difference != 0) {
                scroll(difference);
            }
        }

        for (Criteria criteria : currentTab.getCriteria()) {
            criteria.treeEditor.follow(mouseX, mouseY);
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                criteria.treeEditor.scroll(wheel < 0);
            }
        }
    }
}
