package joshie.progression.gui;

import java.util.ArrayList;

import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class SelectItemOverlay extends TextEditable implements IRenderOverlay {
    public static SelectItemOverlay INSTANCE = new SelectItemOverlay();
    static IItemSelectable selectable = null;
    static ArrayList<ItemStack> sorted;
    static String search = "";
    static int position;
    static Type type;

    public SelectItemOverlay() {
        ItemHelper.addInventory();
    }

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

    public void select(IItemSelectable selectable, Type type) {
        if (reset()) {
            //Setup the info
            SelectItemOverlay.type = type;
            SelectItemOverlay.selectable = selectable;
            super.position = search.length();
        }
    }

    public void scroll(boolean scrolledDown) {
        if (selectable != null) {
            if (scrolledDown) {
                position = Math.min(sorted.size() - 200, position + 8);
            } else {
                position = Math.max(0, position - 8);
            }
        }
    }

    @Override
    void clear() {
        SelectItemOverlay.selectable = null;
        SelectItemOverlay.search = "";
        SelectItemOverlay.position = 0;
    }

    public void updateSearch() {
        if (search == null || search.equals("")) {
            sorted = new ArrayList(ItemHelper.getItems());
        } else {
            position = 0;
            sorted = new ArrayList();
            for (ItemStack stack : ItemHelper.getItems()) {
                if (stack != null && stack.getItem() != null) {
                    try {
                        if (stack.getDisplayName().toLowerCase().contains(search.toLowerCase())) {
                            sorted.add(stack);
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

        ScaledResolution res = GuiCriteriaEditor.INSTANCE.res;
        int fullWidth = res.getScaledWidth() - 10;
        int width = (int) ((double) fullWidth / 16.633333334D);
        int j = 0;
        int k = 0;
        for (int i = position; i < position + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                if (mouseX >= 8 + (j * 16) && mouseX <= 8 + (j * 16) + 16) {
                    if (mouseY >= 45 + (k * 16) && mouseY <= 45 + (k * 16) + 16) {
                        selectable.setItemStack(sorted.get(i).copy());
                        clear();
                        return true;
                    }
                }

                j++;

                if (j > width) {
                    j = 0;
                    k++;
                }
            }
        }

        return false;
    }

    @Override
    public void draw(int x, int y) {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        if (selectable != null) {
            if (sorted == null) {
                updateSearch();
            }

            int offsetX = GuiCriteriaEditor.INSTANCE.offsetX;

            drawGradient(-1, 25 + type.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 15, theme.blackBarGradient1, theme.blackBarGradient2, theme.blackBarBorder);
            drawBox(-1, 40 + type.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 73, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);

            ScaledResolution res = GuiCriteriaEditor.INSTANCE.res;
            int fullWidth = res.getScaledWidth() - 10;
            drawText("Select Item - Click elsewhere to close", 5 - offsetX, 29 + type.yOffset, theme.blackBarFontColor);
            drawBox(285 - offsetX, 27 + type.yOffset, 200, 12, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);
            drawText(getText(), 290 - offsetX, 29 + type.yOffset, theme.blackBarFontColor);

            int mouseX = GuiCriteriaEditor.INSTANCE.mouseX;
            int mouseY = GuiCriteriaEditor.INSTANCE.mouseY - type.yOffset;
            int width = (int) ((double) fullWidth / 16.633333334D);
            int j = 0;
            int k = 0;
            for (int i = position; i < position + (width * 4); i++) {
                if (i >= 0 && i < sorted.size()) {
                    ItemStack stack = sorted.get(i);
                    drawStack(stack, -offsetX + 8 + (j * 16), type.yOffset + 45 + (k * 16), 1F);
                    if (mouseX >= 8 + (j * 16) && mouseX < 8 + (j * 16) + 16) {
                        if (mouseY >= 45 + (k * 16) && mouseY < 45 + (k * 16) + 16) {
                            GuiCriteriaEditor.INSTANCE.addTooltip(stack.getTooltip(ClientHelper.getPlayer(), false));
                        }
                    }

                    j++;

                    if (j > width) {
                        j = 0;
                        k++;
                    }
                }
            }
        }
    }
}