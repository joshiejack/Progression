package joshie.progression.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import joshie.progression.api.ICriteria;
import joshie.progression.api.ITab;
import joshie.progression.api.event.TabVisibleEvent;
import joshie.progression.gui.base.GuiBase;
import joshie.progression.gui.buttons.ButtonNewCriteria;
import joshie.progression.gui.buttons.ButtonTab;
import joshie.progression.gui.editors.EditText;
import joshie.progression.gui.editors.SelectItem;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Options;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class GuiTreeEditor extends GuiBase {
    public static final GuiTreeEditor INSTANCE = new GuiTreeEditor();
    private HashMap<ICriteria, TreeEditorElement> elements;
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
        if (MCClientHelper.isInEditMode()) {
            buttonList.add(new ButtonNewCriteria(pos));
            pos += 28;
            number++;
        }

        //Sort tabs alphabetically or by sort index
        ArrayList<ITab> tabs = new ArrayList(APIHandler.getTabs().values());
        Collections.sort(tabs, new SortIndex());

        for (ITab tab : tabs) {
            if (isTabVisible(tab) || MCClientHelper.isInEditMode()) {
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
            currentTabName = Options.settings.defaultTabID;
        }

        currentTab = APIHandler.getTabFromName(currentTabName);
        if (currentTab == null) {
            for (ITab tab : APIHandler.getTabs().values()) {
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
        rebuildCriteria();
    }

    public void rebuildCriteria() {
        //Rebuild
        elements = new HashMap();
        for (ICriteria criteria : currentTab.getCriteria()) {
            elements.put(criteria, new TreeEditorElement(criteria));
        }
    }

    public TreeEditorElement getElement(ICriteria criteria) {
        return elements.get(criteria);
    }

    public void addCriteria(ICriteria criteria, int x, int y, int offsetX) {
        elements.put(criteria, new TreeEditorElement(criteria));
        getElement(criteria).draw(x, y, offsetX);
        getElement(criteria).click(x, y, false);
        lastClicked = criteria;
    }

    public static boolean isTabVisible(ITab tab) {
        TabVisibleEvent event = new TabVisibleEvent(MCClientHelper.getPlayer(), tab.getUniqueName());
        if (MinecraftForge.EVENT_BUS.post(event)) return false;
        return tab.isVisible();
    }

    @Override
    public void drawForeground() {
        if (currentTab == null) initGui();
        if (!MCClientHelper.isInEditMode() && !isTabVisible(currentTab)) return;
        for (ICriteria criteria : currentTab.getCriteria()) {
            if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                TreeEditorElement editor = getElement(criteria);
                List<ICriteria> prereqs = criteria.getPreReqs();
                for (ICriteria c : prereqs) {
                    int y1 = getElement(c).getY();
                    int y2 = editor.getY();
                    int x1 = getElement(c).getX();
                    int x2 = editor.getX();

                    int width = 0;
                    int textWidth = mc.fontRendererObj.getStringWidth(c.getDisplayName());
                    int iconWidth = 9 + (c.getRewards().size() * 12);
                    if (textWidth >= iconWidth) {
                        width = textWidth + 9;
                    } else width = iconWidth;

                    width -= 3;

                    if (c.getTab() == criteria.getTab()) {
                        drawLine(offsetX + width + x1, y + 12 + y1 - 1, offsetX + 5 + x2, y + 12 + y2 - 1, 1, theme.connectLineColor1);
                        drawLine(offsetX + width + x1, y + 12 + y1 + 1, offsetX + 5 + x2, y + 12 + y2 + 1, 1, theme.connectLineColor2); //#636C69
                        drawLine(offsetX + width + x1, y + 12 + y1, offsetX + 5 + x2, y + 12 + y2, 1, theme.connectLineColor3);
                    }
                }
            }
        }

        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        for (ICriteria criteria : currentTab.getCriteria()) {
            if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                getElement(criteria).draw(0, y, offsetX);
            }
        }

        TreeEditorSelection.INSTANCE.draw();
    }

    public ITab previousTab = null;

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        ICriteria toRemove = null;
        for (ICriteria criteria : currentTab.getCriteria()) {
            if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                if (getElement(criteria).keyTyped(character, key)) {
                    toRemove = criteria;
                    break;
                }
            }
        }

        if (toRemove != null) {
            APIHandler.removeCriteria(toRemove.getUniqueName(), false);
        }

        if (MCClientHelper.isInEditMode()) {
            EditText.INSTANCE.keyTyped(character, key);

            if (SelectItem.INSTANCE.getEditable() != null) {
                TreeEditorSelection.INSTANCE.keyTyped(character, key);
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
    public void mouseReleased(int x, int y, int button) {
        for (ICriteria criteria : currentTab.getCriteria()) {
            getElement(criteria).release(mouseX, mouseY);
        }

        isDragging = false;
    }

    public long lastClick;
    public int lastType;
    public ICriteria lastClicked = null;
    public int drag = 0;
    public boolean isDragging = false;

    @Override
    protected void mouseClicked(int par1, int par2, int button) throws IOException {
        if (currentTab == null) return;
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = button == 0 && lastType == 0 && difference <= 500;
        lastClick = System.currentTimeMillis();
        lastType = button;

        lastClicked = null;
        if (SelectItem.INSTANCE.getEditable() != null) {
            if (!TreeEditorSelection.INSTANCE.mouseClicked(mouseX, mouseY)) {
                SelectItem.INSTANCE.clear();
            }
        }

        super.mouseClicked(par1, par2, button);
        if (button == 0) {
            for (ICriteria criteria : currentTab.getCriteria()) {
                if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                    if (getElement(criteria).click(mouseX, mouseY, isDoubleClick)) {
                        lastClicked = criteria;
                    }
                }
            }
        } else {
            if (lastClicked == null && selected == null) {
                isDragging = true;
                drag = mouseX;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        if (isDragging) {
            int difference = mouseX - drag;
            drag = mouseX;

            if (difference != 0) {
                scroll(difference);
            }
        }

        if (lastClicked != null) {
            for (ICriteria criteria : currentTab.getCriteria()) {
                getElement(criteria).follow(mouseX, mouseY);
                int wheel = Mouse.getDWheel();
                if (wheel != 0) {
                    getElement(criteria).scroll(wheel < 0);
                }
            }
        }
    }
}
