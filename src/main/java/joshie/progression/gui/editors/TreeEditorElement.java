package joshie.progression.gui.editors;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import joshie.progression.player.PlayerTracker;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TreeEditorElement {
    private static final GuiTreeEditor gui = GuiTreeEditor.INSTANCE;
    private final ICriteria criteria;
    private boolean isSelected;
    private boolean isHeld;
    private int prevX;
    private int prevY;
    protected int left;
    protected int right;
    protected int top;
    protected int bottom;
    protected int width = 200;
    protected int height = 25;

    public TreeEditorElement(ICriteria criteria) {
        this.criteria = criteria;
    }

    public int getX() {
        return criteria.getX();
    }

    public int getY() {
        return criteria.getY();
    }

    private int getWidthFromRewards() {
        int size = criteria.getRewards().size();
        if (size == 1) {
            return 21; //12 Bigger 1 * 12 + 9
        } else if (size == 2) {
            return 33; // 12 Bigger 2 * 12
        } else if (size == 3) {
            return 45; //12 Bigger
        } else if (size == 4) {
            return 57; //12 Bigger
        }

        return 0;
    }

    private void recalculate(int x) {
        int textWidth = gui.mc.fontRendererObj.getStringWidth(criteria.getLocalisedName());
        int iconWidth = 9 + (criteria.getRewards().size() * 12);
        if (textWidth >= iconWidth) {
            width = textWidth + 9;
        } else width = iconWidth;

        left = criteria.getX() + x;
        right = (int) (criteria.getX() + width) + x;
        top = criteria.getY();
        bottom = (int) (criteria.getY() + height);
    }

    public void draw(int x, int y, int offsetX) {
        draw(x, y, offsetX, 0);
    }

    public boolean isCriteriaVisible() {
        if (criteria.isVisible()) return true;
        else return PlayerTracker.getClientPlayer().getMappings().getCompletedCriteria().keySet().containsAll(criteria.getPreReqs());
    }

    public static boolean isCriteriaCompleteable(ICriteria criteria) {
        HashMap<ICriteria, Integer> completedMap = PlayerTracker.getClientPlayer().getMappings().getCompletedCriteria();
        boolean completeable = true;
        //Check the conflicts of this criteria
        for (ICriteria conflicts : criteria.getConflicts()) {
            if (completedMap.containsKey(conflicts)) return false;
        }

        //Check the requirements, if they aren't completable return false
        for (ICriteria requirements : criteria.getPreReqs()) {
            if (requirements.equals(criteria)) return false;
            if (!isCriteriaCompleteable(requirements)) return false;
        }

        return true;
    }

    public static enum ColorMode {
        DEFAULT(0), COMPLETED(25), AVAILABLE(50), ERROR(75), SELECTED(100), INVISIBLE(0);

        public final int y;

        private ColorMode(int y) {
            this.y = y;
        }
    }

    public static ColorMode getModeForCriteria(ICriteria criteria, boolean isSelected) {
        ColorMode mode = ColorMode.DEFAULT;
        HashMap<ICriteria, Integer> completedMap = PlayerTracker.getClientPlayer().getMappings().getCompletedCriteria();
        boolean isCompleted = completedMap.containsKey(criteria);
        boolean anyConflicts = false;
        boolean allRequires = false;
        int requires = 0;
        for (ICriteria c : criteria.getConflicts()) {
            if (completedMap.containsKey(c)) {
                anyConflicts = true;
                break;
            }
        }

        if (!anyConflicts) {
            for (ICriteria c : criteria.getPreReqs()) {
                if (completedMap.containsKey(c)) {
                    requires++;
                }
            }

            allRequires = criteria.getPreReqs().size() == requires;
        }

        boolean available = allRequires && !anyConflicts;
        if (isCompleted) mode = ColorMode.COMPLETED;
        else if (available) mode = ColorMode.AVAILABLE;

        if (!criteria.isVisible()) {
            if (!MCClientHelper.isInEditMode()) {
                if (available || isCompleted) {

                } else return ColorMode.INVISIBLE;
            }
        }

        if (isSelected) mode = ColorMode.SELECTED;
        ICriteria selected = GuiTreeEditor.INSTANCE.lastClicked;
        if (!isCompleted) {
            if (!isCriteriaCompleteable(criteria)) {
                mode = ColorMode.ERROR;
            }
        }

        if (selected != null) {
            if (selected.getConflicts().contains(criteria)) {
                mode = ColorMode.ERROR;
            }
        }

        if (PlayerTracker.getClientPlayer().getMappings().isImpossible(criteria)) {
            mode = ColorMode.ERROR;
        }

        return mode;
    }

    public void draw(int x, int y, int offsetX, int highlight) {
        recalculate(offsetX);
        if (highlight != 0) {
            GuiCore.INSTANCE.drawRectWithBorder(x + left, top, x + right, bottom, Theme.INSTANCE.invisible, highlight);
        } else {
            ColorMode mode = getModeForCriteria(criteria, isSelected);
            if (mode == ColorMode.INVISIBLE) return; //If we returned invisible, don't show it
            int textureX = 0;
            int textureY = mode.y;
            if (!criteria.isVisible()) { //Make the texture transparent if this is edit mode
                if (MCClientHelper.isInEditMode()) {
                    textureX = 100;
                }
            }

            GlStateManager.enableBlend();
            GlStateManager.color(1F, 1F, 1F, 1F);
            gui.mc.getTextureManager().bindTexture(ProgressionInfo.textures);
            GuiCore.INSTANCE.drawTexture(ProgressionInfo.textures, x + left, top, textureX, textureY, 10, 25);
            for (int i = 0; i < width - 20; i++) {
                GuiCore.INSTANCE.drawTexture(ProgressionInfo.textures, x + left + 10 + i, top, textureX + 10, textureY, 1, 25);
            }

            GuiCore.INSTANCE.drawTexture(ProgressionInfo.textures, x + right - 10, top, textureX + 90, textureY, 10, 25);

            //gui.drawTexturedModalRect(x + left, y + top, textureX, textureY, 100, 25);
            GuiCore.INSTANCE.drawText(criteria.getLocalisedName(), x + left + 4, top + 3, Theme.INSTANCE.criteriaDisplayNameColor);

            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            //Draw in the rewards
            int xOffset = 0;
            for (IRewardProvider reward : criteria.getRewards()) {
                ItemStack icon = reward.getIcon().copy();
                if (icon == null || icon.getItem() == null) continue; //Protection against null icons
                icon.stackSize = 1; //Force it to 1
                GuiCore.INSTANCE.drawStack(icon, x + 4 + left + (xOffset * 12), top + 12, 0.75F);
                xOffset++;
            }

            int mouseX = GuiCore.INSTANCE.mouseX;
            int mouseY = GuiCore.INSTANCE.mouseY;
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            xOffset = 0;
            boolean hoveredReward = false;
            for (IRewardProvider reward : criteria.getRewards()) {
                int x1 = 3 + left + (xOffset * 12);
                int x2 = x1 + 11;
                int y1 = bottom - 13;
                int y2 = y1 + 12;
                if (isOver(mouseX, mouseY, x1, x2, y1, y2)) {
                    List list = new ArrayList();
                    reward.addTooltip(list);
                    FeatureTooltip.INSTANCE.addTooltip(list);
                    hoveredReward = true;
                }

                xOffset++;
            }

            RenderHelper.disableStandardItemLighting();

            if (!hoveredReward) { //If we weren't hovering over the reward, display the requirements
                if (isOver(mouseX, mouseY)) {
                    List list = new ArrayList();
                    if (MCClientHelper.isInEditMode()) {
                        list.add("Double Click to edit "/* + (Hold shift for display mode) */);
                        list.add("Shift + Click to make something a requirement");
                        list.add("Ctrl + Click to make something conflict");
                        list.add("I + Click to Hide/Unhide");
                    }

                    for (ICriteria c : criteria.getPreReqs()) {
                        if (c.getTab() != criteria.getTab()) {
                            list.add(EnumChatFormatting.RED + "Requires: " + c.getLocalisedName() + " from the \"" + c.getTab().getDisplayName() + "\" tab");
                        }
                    }

                    FeatureTooltip.INSTANCE.addTooltip(list);
                    RenderHelper.disableStandardItemLighting();
                }
            }
        }
    }

    private boolean isOver(int mouseX, int mouseY, int x1, int x2, int y1, int y2) {
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }

    private boolean noOtherSelected() {
        return GuiTreeEditor.INSTANCE.selected == null;
    }

    private void clearSelected() {
        GuiTreeEditor.INSTANCE.selected = null;
    }

    private void setSelected() {
        GuiTreeEditor.INSTANCE.selected = criteria;
        GuiTreeEditor.INSTANCE.previous = criteria;
    }

    private ICriteria getPrevious() {
        return GuiTreeEditor.INSTANCE.previous;
    }

    private boolean isOver(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    private void remove(List<ICriteria> list, ICriteria criteria) {
        Iterator<ICriteria> it = list.iterator();
        while (it.hasNext()) {
            ICriteria c = it.next();
            if (c.equals(criteria)) {
                it.remove();
                break;
            }
        }
    }

    public boolean keyTyped(char character, int key) {
        if (isSelected && MCClientHelper.isInEditMode()) {
            return key == 211 || key == 14;
        }

        return false;
    }

    public boolean isAssignableTo(ICriteria to, ICriteria from) {
        if (to.equals(from)) return false; //If they are the same criteria we can't assign them
        if (from.getPreReqs().contains(to)) return false; //If the criteria we are trying to assign has this one in it, we can't
        //We need to check down the chain to stop the assignments

        return true;
    }

    public boolean click(int x, int y, boolean isDouble) {
        if (isOver(x, y)) {
            if (noOtherSelected()) {
                ICriteria previous = getPrevious();
                if (previous != null && MCClientHelper.isInEditMode()) {
                    List<ICriteria> list = null;
                    boolean isConflict = false;
                    if (GuiScreen.isShiftKeyDown()) {
                        list = previous.getPreReqs();
                        if (previous.getConflicts().contains(criteria)) {
                            list = null;
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        list = previous.getConflicts();
                        isConflict = true;
                    }

                    if (!isAssignableTo(previous, criteria)) list = null;

                    if (list != null) {
                        if (list.contains(criteria)) {
                            remove(list, criteria);
                            if (isConflict) {
                                remove(criteria.getConflicts(), previous);
                            }
                        } else {
                            //Now that it's been added, move everything
                            list.add(criteria);
                            if (isConflict) {
                                criteria.getConflicts().add(previous);
                            }
                        }

                        return true;
                    }
                }

                if (MCClientHelper.isInEditMode()) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                        criteria.setVisiblity(!criteria.isVisible());
                        return true;
                    }
                }

                if (isDouble && GuiTreeEditor.INSTANCE.previous == criteria) {
                    isHeld = false;
                    isSelected = false;

                    GuiCriteriaEditor.INSTANCE.setCriteria(criteria);
                    GuiCore.INSTANCE.setEditor(GuiCriteriaEditor.INSTANCE);
                    return true;
                }

                isHeld = true;
                isSelected = true;

                prevX = x;
                prevY = y;
                setSelected();
                return true;
            }

            return false;
        } else {
            if (isSelected && x >= 0) {
                clearSelected();
                isSelected = false;
            }

            return false;
        }
    }

    public void release(int x, int y) {
        if (isHeld) {
            isHeld = false;
            clearSelected();
        }
    }

    public void follow(int x, int y) {
        if (isHeld && MCClientHelper.isInEditMode()) {
            criteria.setCoordinates(criteria.getX() + x - prevX, criteria.getY() + y - prevY);
            prevX = x;
            prevY = y;
        }
    }

    public void scroll(boolean scrolledDown) {
        return;
    }
}
