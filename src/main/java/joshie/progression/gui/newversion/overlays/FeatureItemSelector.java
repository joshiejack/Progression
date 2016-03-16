package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import joshie.progression.Progression;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.gui.GuiTreeEditor;
import joshie.progression.gui.editors.EditText.ITextEditable;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.item.ItemStack;

public class FeatureItemSelector extends FeatureAbstract implements ITextEditable {
    public static FeatureItemSelector INSTANCE = new FeatureItemSelector();
    private IItemSelectable selectable = null;
    private IItemSelectorFilter[] filters = null;
    private ArrayList<ItemStack> sorted;
    private String search = "";
    private Type type;
    private int position;

    public FeatureItemSelector() {}

    public IItemSelectable getEditable() {
        return selectable;
    }

    public static enum Type {
        REWARD(0), TRIGGER(95), TREE(0);

        public int yOffset;

        private Type(int offset) {
            this.yOffset = offset;
        }
    }

    public void select(IItemSelectorFilter[] filters, IItemSelectable selectable, Type type) {
        ItemHelper.addInventory();
        TextEditor.INSTANCE.setEditable(this);
        this.filters = filters;
        this.selectable = selectable;
        this.type = type;
        updateSearch();
    }

    public void clearEditable() {
        this.selectable = null;
    }

    @Override
    public boolean scroll(int mouseX, int mouseY, boolean scrolledDown) {
        mouseY -= type.yOffset;
        if (mouseY >= 40 && mouseY <= 110) {
            if (selectable != null) {
                int width = (int) ((double) (screenWidth - 10) / 16.133333334D) * 4;
                if (scrolledDown) position = Math.min(sorted.size() - 200, position + width);
                else position = Math.max(0, position - width);
                return true;
            }
        }

        return false;
    }

    public boolean passesFilters(ItemStack stack) {
        if (filters != null) {
            for (IItemSelectorFilter filter : filters) {
                if (filter.isAcceptable(stack)) return true;
            }

            return false;
        }

        return true;
    }

    private void attemptToAdd(ItemStack stack) {
        if (passesFilters(stack)) {
            if (!sorted.contains(stack)) {
                sorted.add(stack);
            }
        }
    }

    public ArrayList<ItemStack> getAllItems() {
        ArrayList<ItemStack> list = ItemHelper.getAllItems();
        if (filters != null) {
            for (IItemSelectorFilter filter : filters) {
                filter.addExtraItems(list);
            }
        }

        return list;
    }

    public void updateSearch() {
        if (search == null || search.equals("")) {
            sorted = new ArrayList();
            for (ItemStack stack : getAllItems()) {
                attemptToAdd(stack);
            }
        } else {
            position = 0;
            sorted = new ArrayList();
            for (ItemStack stack : getAllItems()) {
                if (stack != null && stack.getItem() != null) {
                    try {
                        if (stack.getDisplayName().toLowerCase().contains(search.toLowerCase())) {
                            attemptToAdd(stack);
                        }
                    } catch (Exception e) {}
                }
            }
        }
    }

    @Override
    public void setTextField(String text) {
        this.search = text;
        this.updateSearch();
    }

    @Override
    public String getTextField() {
        return this.search;
    }

    @Override
    public boolean isVisible() {
        return selectable != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (sorted == null) {
            updateSearch();
        }

        mouseY -= type.yOffset;
        int width = (int) ((double) (screenWidth - 10) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = position; i < position + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                if (mouseX >= 8 + (j * 16) && mouseX <= 8 + (j * 16) + 16) {
                    if (mouseY >= 45 + (k * 16) && mouseY <= 45 + (k * 16) + 16) {
                        selectable.setItemStack(sorted.get(i).copy());
                        selectable = null; //Clear out the selectable
                        return true;
                    }
                }

                j++;

                if (j >= width) {
                    j = 0;
                    k++;
                }
            }
        }

        return false;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        if (sorted == null) {
            updateSearch();
        }

        int offsetX = GuiCriteriaEditor.INSTANCE.offsetX;
        mouseY -= type.yOffset;
        offset.drawGradient(-1, 25 + type.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 15, theme.blackBarGradient1, theme.blackBarGradient2, theme.blackBarBorder);
        offset.drawRectangle(-1, 40 + type.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 73, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);
        
        offset.drawText(Progression.translate("selector.items"), 5, 29 + type.yOffset, theme.blackBarFontColor);
        offset.drawRectangle(285 - offsetX, 27 + type.yOffset, 200, 12, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);
        offset.drawText(TextEditor.INSTANCE.getText(this), 290, 29 + type.yOffset, theme.blackBarFontColor);

        int width = (int) ((double) (screenWidth - 10) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = position; i < position + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                ItemStack stack = sorted.get(i);
                offset.drawStack(stack, -offsetX + 8 + (j * 16), type.yOffset + 45 + (k * 16), 1F);
                if (mouseX >= 8 + (j * 16) && mouseX < 8 + (j * 16) + 16) {
                    if (mouseY >= 45 + (k * 16) && mouseY < 45 + (k * 16) + 16) {
                        FeatureTooltip.INSTANCE.addTooltip(stack.getTooltip(MCClientHelper.getPlayer(), false));
                    }
                }

                j++;

                if (j >= width) {
                    j = 0;
                    k++;
                }
            }
        }
    }
}