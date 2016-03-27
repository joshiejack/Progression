package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionTab;
import joshie.progression.api.event.TabVisibleEvent;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.tree.buttons.ButtonNewCriteria;
import joshie.progression.gui.tree.buttons.ButtonTab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Options;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiTreeEditor extends GuiBaseEditor implements IEditorMode {
    public static final GuiTreeEditor INSTANCE = new GuiTreeEditor();
    private HashMap<IProgressionCriteria, TreeEditorElement> elements;
    public IProgressionCriteria selected = null;
    public IProgressionCriteria previous = null;
    public UUID currentTabID;
    public IProgressionTab currentTab;

    private static class SortIndex implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            IProgressionTab tab1 = ((IProgressionTab) o1);
            IProgressionTab tab2 = ((IProgressionTab) o2);
            if (tab1.getSortIndex() == tab2.getSortIndex()) {
                return tab1.getDisplayName().compareTo(tab2.getDisplayName());
            }

            return tab1.getSortIndex() < tab2.getSortIndex() ? 1 : -1;
        }
    }
    
    @Override
    public Object getKey() {
        return currentTab;
    }

    @Override
    public void initData(GuiCore core) {
        super.initData(core);
        List<GuiButton> buttonList = core.getButtonNewList(); //Recreate the button list, in order to reposition it
        int number = 0;
        int pos = -5;
        if (MCClientHelper.isInEditMode()) {
            buttonList.add(new ButtonNewCriteria(pos));
            pos += 28;
            number++;
        }

        //Sort tabs alphabetically or by sort index
        ArrayList<IProgressionTab> tabs = new ArrayList(APIHandler.getTabs().values());
        Collections.sort(tabs, new SortIndex());

        for (IProgressionTab tab : tabs) {
            if (isTabVisible(tab) || MCClientHelper.isInEditMode()) {
                if (number <= 8) {
                    buttonList.add(new ButtonTab(tab, 0, pos));
                } else buttonList.add(new ButtonTab(tab, res.getScaledWidth() - 25, pos));

                pos += 28;

                if (number == 8) {
                    pos = -5;
                }

                number++;
            }
        }

        if (currentTabID == null) {
            currentTabID = Options.settings.defaultTabID;
        }

        currentTab = APIHandler.getTabFromName(currentTabID);
        if (currentTab == null) {
            for (IProgressionTab tab : APIHandler.getTabs().values()) {
                currentTab = tab;
                break;
            }

            if (currentTab == null) {
                currentTab = APIHandler.newTab(UUID.randomUUID()).setDisplayName("New Tab").setStack(new ItemStack(Items.book)).setVisibility(true);
                currentTabID = currentTab.getUniqueID();
                core.initGui(); //Reset things
            }
        }

        currentTabID = currentTab.getUniqueID();
        features.add(FeatureItemSelectorTree.INSTANCE);
        rebuildCriteria();
    }

    public void rebuildCriteria() {
        //Rebuild
        elements = new HashMap();
        for (IProgressionCriteria criteria : currentTab.getCriteria()) {
            elements.put(criteria, new TreeEditorElement(criteria));
        }
    }

    public TreeEditorElement getElement(IProgressionCriteria criteria) {
        return elements.get(criteria);
    }

    public void addCriteria(IProgressionCriteria criteria, int x, int y, int offsetX) {
        elements.put(criteria, new TreeEditorElement(criteria));
        getElement(criteria).draw(x, y, offsetX);
        getElement(criteria).click(x, y, false);
        lastClicked = criteria;
    }

    public static boolean isTabVisible(IProgressionTab tab) {
        TabVisibleEvent event = new TabVisibleEvent(MCClientHelper.getPlayer(), tab.getUniqueID());
        if (MinecraftForge.EVENT_BUS.post(event)) return false;
        return tab.isVisible();
    }
    
    @Override
    public IEditorMode getPreviousGui() {
        return this;
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {
        if (!MCClientHelper.isInEditMode() && !isTabVisible(currentTab)) return;
        for (IProgressionCriteria criteria : currentTab.getCriteria()) {
            if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                TreeEditorElement editor = getElement(criteria);
                List<IProgressionCriteria> prereqs = criteria.getPreReqs();
                for (IProgressionCriteria c : prereqs) {
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
                        drawLine(offsetX + width + x1, 12 + y1 - 1, offsetX + 5 + x2, 12 + y2 - 1, 1, theme.connectLineColor1);
                        drawLine(offsetX + width + x1, 12 + y1 + 1, offsetX + 5 + x2, 12 + y2 + 1, 1, theme.connectLineColor2); //#636C69
                        drawLine(offsetX + width + x1, 12 + y1, offsetX + 5 + x2, 12 + y2, 1, theme.connectLineColor3);
                    }
                }
            }
        }

        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        for (IProgressionCriteria criteria : currentTab.getCriteria()) {
            if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                getElement(criteria).draw(0, screenTop, offsetX);
            }
        }
    }

    public IProgressionTab previousTab = null;

    @Override
    public void keyTyped(char character, int key) {
        IProgressionCriteria toRemove = null;
        for (IProgressionCriteria criteria : currentTab.getCriteria()) {
            if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                if (getElement(criteria).keyTyped(character, key)) {
                    toRemove = criteria;
                    break;
                }
            }
        }

        if (toRemove != null) {
            APIHandler.removeCriteria(toRemove.getUniqueID(), false);
        }

        if (key == Keyboard.KEY_UP) {
            currentTab.setSortIndex(currentTab.getSortIndex() + 1);
            core.initGui();
        } else if (key == Keyboard.KEY_DOWN) {
            currentTab.setSortIndex(currentTab.getSortIndex() - 1);
            core.initGui();
        }
    }

    @Override
    public void guiMouseReleased(int mouseX, int mouseY, int button) {
        for (IProgressionCriteria criteria : currentTab.getCriteria()) {
            getElement(criteria).release(mouseX, mouseY);
        }

        isDragging = false;
    }

    public long lastClick;
    public int lastType;
    public IProgressionCriteria lastClicked = null;
    public int drag = 0;
    public boolean isDragging = false;

    @Override
    public boolean guiMouseClicked(boolean overlayvisible, int mouseX, int mouseY, int button) {
        if (currentTab == null) return false;
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = button == 0 && lastType == 0 && difference <= 500;
        lastClick = System.currentTimeMillis();
        lastType = button;
        
        lastClicked = null;
        if (button == 0) {
            for (IProgressionCriteria criteria : currentTab.getCriteria()) {
                if (getElement(criteria).isCriteriaVisible() || MCClientHelper.isInEditMode()) {
                    if (getElement(criteria).click(mouseX, mouseY, isDoubleClick)) {
                        lastClicked = criteria;
                        return true;
                    }
                }
            }
            
            return false;
        } else {
            if (lastClicked == null && selected == null) {
                isDragging = true;
                drag = mouseX;
                return true;
            }
        }
        
        return true;
    }

    @Override
    public void handleMouseInput(int mouseX, int mouseY) {
        if (isDragging) {
            int difference = mouseX - drag;
            drag = mouseX;

            if (difference != 0) {
                core.scroll(difference);
            }
        }

        if (lastClicked != null) {
            for (IProgressionCriteria criteria : currentTab.getCriteria()) {
                getElement(criteria).follow(mouseX, mouseY);
                int wheel = Mouse.getDWheel();
                if (wheel != 0) {
                    getElement(criteria).scroll(wheel < 0);
                }
            }
        }
    }
}
