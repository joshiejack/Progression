package joshie.crafting.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITreeEditor;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.RenderItemHelper;
import joshie.crafting.json.Theme;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class EditorTree implements ITreeEditor {
    private static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
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
    protected int offsetX = 0;
    private int x;
    private int y;

    public EditorTree(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
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
        int textWidth = gui.mc.fontRenderer.getStringWidth(criteria.getDisplayName());
        int iconWidth = 9 + (criteria.getRewards().size() * 12);
        if (textWidth >= iconWidth) {
            width = textWidth + 9;
        } else width = iconWidth;

        left = this.x + x;
        right = (int) (this.x + width) + x;
        top = this.y;
        bottom = (int) (this.y + height);
        offsetX = x;
    }

    public void draw(int x, int y, int offsetX) {
        draw(x, y, offsetX, 0);
    }

    @Override
    public boolean isCriteriaVisible() {
        if (criteria.isVisible()) return true;
        else return CraftingAPI.players.getClientPlayer().getMappings().getCompletedCriteria().keySet().containsAll(criteria.getRequirements());
    }
    
    public boolean isCriteriaCompleteable(ICriteria criteria) {
        HashMap<ICriteria, Integer> completedMap = CraftingAPI.players.getClientPlayer().getMappings().getCompletedCriteria();
        boolean completeable = true;
        //Check the conflicts of this criteria
        for (ICriteria conflicts : criteria.getConflicts()) {
            if (completedMap.containsKey(conflicts)) return false;
        }

        //Check the requirements, if they aren't completable return false
        for (ICriteria requirements : criteria.getRequirements()) {
            if (requirements.equals(criteria)) return false;
            if (!isCriteriaCompleteable(requirements)) return false;
        }

        return true;
    }

    @Override
    public void draw(int x, int y, int offsetX, int highlight) {
        recalculate(offsetX);
        if (highlight != 0) {
            GuiTreeEditor.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, Theme.INSTANCE.invisible, highlight);
        } else {
            HashMap<ICriteria, Integer> completedMap = CraftingAPI.players.getClientPlayer().getMappings().getCompletedCriteria();
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
                for (ICriteria c : criteria.getRequirements()) {
                    if (completedMap.containsKey(c)) {
                        requires++;
                    }
                }

                allRequires = criteria.getRequirements().size() == requires;
            }

            boolean available = allRequires && !anyConflicts;

            int textureY = 0;
            int textureX = 0;
            if (isCompleted) textureY = 25;
            else if (available) textureY = 50;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (!criteria.isVisible()) {
                if (ClientHelper.canEdit()) {
                    textureX = 100;
                } else {
                    if (available || isCompleted) {
                        textureX = 0;
                    } else return; //If it's not completed, or available then hide it entirely
                }
            }

            if (isSelected) textureY = 100;
            ICriteria selected = GuiTreeEditor.INSTANCE.lastClicked;
            if (!isCompleted) {
                if (!isCriteriaCompleteable(criteria)) {
                    textureY = 75;
                }
            }
            
            if (selected != null) {
                if (selected.getConflicts().contains(criteria)) {
                    textureY = 75;
                }
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            gui.mc.getTextureManager().bindTexture(textures);
            gui.drawTexturedModalRect(x + left, y + top, textureX, textureY, 10, 25);
            for (int i = 0; i < width - 20; i++) {
                gui.drawTexturedModalRect(x + left + 10 + i, y + top, textureX + 10, textureY, 1, 25);
            }

            gui.drawTexturedModalRect(x + right - 10, y + top, textureX + 90, textureY, 10, 25);

            //gui.drawTexturedModalRect(x + left, y + top, textureX, textureY, 100, 25);
            gui.mc.fontRenderer.drawString(criteria.getDisplayName(), x + left + 4, y + top + 3, Theme.INSTANCE.criteriaDisplayNameColor);

            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            //Draw in the rewards
            int xOffset = 0;
            for (IReward reward : criteria.getRewards()) {
                ItemStack icon = reward.getIcon();
                if (icon == null || icon.getItem() == null) continue; //Protection against null icons
                RenderItemHelper.drawStack(icon, x + 4 + left + (xOffset * 12), y + top + 12, 0.75F);
                xOffset++;
            }

            int mouseX = GuiTreeEditor.INSTANCE.mouseX;
            int mouseY = GuiTreeEditor.INSTANCE.mouseY;
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            xOffset = 0;
            boolean hoveredReward = false;
            for (IReward reward : criteria.getRewards()) {
                int x1 = 3 + left + (xOffset * 12);
                int x2 = x1 + 11;
                int y1 = bottom - 13;
                int y2 = y1 + 12;
                if (isOver(mouseX, mouseY, x1, x2, y1, y2)) {
                    List list = new ArrayList();
                    reward.addTooltip(list);
                    GuiTreeEditor.INSTANCE.addTooltip(list);
                    hoveredReward = true;
                }

                xOffset++;
            }

            RenderHelper.disableStandardItemLighting();

            if (!hoveredReward) { //If we weren't hovering over the reward, display the requirements
                if (isOver(mouseX, mouseY)) {
                    List list = new ArrayList();
                    if (ClientHelper.canEdit()) {
                        list.add("Double Click to edit "/* + (Hold shift for display mode) */);
                        list.add("Shift + Click to make something a requirement");
                        list.add("Ctrl + Click to make something conflict");
                        list.add("I + Click to Hide/Unhide");
                    }

                    GuiTreeEditor.INSTANCE.addTooltip(list);
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

    @Override
    public boolean keyTyped(char character, int key) {
        if (isSelected && ClientHelper.canEdit()) {
            return key == 211 || key == 14;
        }

        return false;
    }

    @Override
    public boolean click(int x, int y, boolean isDouble) {
        if (isOver(x, y)) {
            if (noOtherSelected()) {
                ICriteria previous = getPrevious();
                if (previous != null && ClientHelper.canEdit()) {
                    List<ICriteria> list = null;
                    boolean isConflict = false;
                    if (GuiScreen.isShiftKeyDown()) {
                        list = previous.getRequirements();
                        if (previous.getConflicts().contains(criteria)) {
                            list = null;
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        list = previous.getConflicts();
                        isConflict = true;
                    }

                    if (previous == criteria) {
                        list = null;
                    }

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

                if (ClientHelper.canEdit()) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                        boolean invisible = criteria.isVisible();
                        criteria.setVisibility(!invisible);
                        return true;
                    }
                }

                if (isDouble && GuiTreeEditor.INSTANCE.previous == criteria) {
                    isHeld = false;
                    isSelected = false;

                   // if (GuiScreen.isShiftKeyDown()) {
                       // GuiCriteriaViewer.INSTANCE.selected = criteria;
                       // ClientHelper.getPlayer().openGui(CraftingMod.instance, 3, null, 0, 0, 0);
                    //} else {
                        GuiCriteriaEditor.INSTANCE.selected = criteria;
                        ClientHelper.getPlayer().openGui(CraftingMod.instance, 1, null, 0, 0, 0);
                   // }
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

    @Override
    public void release(int x, int y) {
        if (isHeld) {
            isHeld = false;
            clearSelected();
        }
    }

    @Override
    public void follow(int x, int y) {
        if (isHeld && ClientHelper.canEdit()) {
            this.x += x - prevX;
            this.y += y - prevY;
            prevX = x;
            prevY = y;
        }
    }

    @Override
    public void scroll(boolean scrolledDown) {
        return;
    }
}
