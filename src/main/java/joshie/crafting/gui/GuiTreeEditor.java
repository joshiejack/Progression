package joshie.crafting.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.Criteria;
import joshie.crafting.Tab;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Options;
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
        ArrayList<Tab> tabs = new ArrayList(CraftAPIRegistry.tabs.values());
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

        currentTab = CraftAPIRegistry.getTabFromName(currentTabName);
        if (currentTab == null) {
            for (Tab tab : CraftAPIRegistry.tabs.values()) {
                currentTab = tab;
                break;
            }
            
            if (currentTab == null) {
            	currentTab = CraftAPIRegistry.newTab(CraftAPIRegistry.getNextUnique()).setDisplayName("New Tab").setStack(new ItemStack(Items.book)).setVisibility(true);
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
            if (criteria.getTreeEditor().isCriteriaVisible() || ClientHelper.canEdit()) {
                EditorTree editor = criteria.getTreeEditor();
                List<Criteria> prereqs = criteria.getRequirements();
                for (Criteria p : prereqs) {
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
                    
                    drawLine(offsetX + width + x1, y + 12 + y1 - 1, offsetX + 5 + x2, y + 12 + y2 - 1, 1, theme.connectLineColor1);
                    drawLine(offsetX + width + x1, y + 12 + y1 + 1, offsetX + 5 + x2, y + 12 + y2 + 1, 1, theme.connectLineColor2); //#636C69
                    drawLine(offsetX + width + x1, y + 12 + y1, offsetX + 5 + x2, y + 12 + y2, 1, theme.connectLineColor3);
                }
            }
        }

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        for (Criteria criteria : currentTab.getCriteria()) {
            if (criteria.getTreeEditor().isCriteriaVisible() || ClientHelper.canEdit()) {
                criteria.getTreeEditor().draw(0, y, offsetX);
            }
        }

        TreeItemSelect.INSTANCE.draw();
    }

    public Tab previousTab = null;

    @Override
    protected void keyTyped(char character, int key) {
        Criteria toRemove = null;
        for (Criteria criteria : currentTab.getCriteria()) {
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
        for (Criteria criteria : currentTab.getCriteria()) {
            criteria.getTreeEditor().release(mouseX, mouseY);
        }
    }

    private long lastClick;
    private int lastType;
    Criteria lastClicked = null;

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
        for (Criteria criteria : currentTab.getCriteria()) {
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
        for (Criteria criteria : currentTab.getCriteria()) {
            criteria.getTreeEditor().follow(mouseX, mouseY);
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                criteria.getTreeEditor().scroll(wheel < 0);
            }
        }
    }
}
