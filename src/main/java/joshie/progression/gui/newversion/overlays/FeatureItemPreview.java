package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import joshie.progression.api.IItemFilter;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class FeatureItemPreview extends FeatureAbstract {
    public static FeatureItemPreview INSTANCE = new FeatureItemPreview();
    private ArrayList<ItemStack> sorted;
    private boolean blocksOnly;
    private int position;

    public FeatureItemPreview() {}

    public void select(boolean blocksOnly) {
        this.blocksOnly = blocksOnly;
        updateSearch();
    }

    @Override
    public boolean scroll(int mouseX, int mouseY, boolean scrolledDown) {
        mouseY -= 95;
        if (mouseY >= 40 && mouseY <= 110) {
            int width = (int) ((double) (screenWidth - 10) / 16.133333334D) * 4;
            if (scrolledDown) position = Math.min(sorted.size() - 200, position + width);
            else position = Math.max(0, position - width);
            return true;
        }

        return false;
    }

    private void attemptToAddBlock(ItemStack stack) {
        Block block = null;
        int meta = 0;

        try {
            block = Block.getBlockFromItem(stack.getItem());
            meta = stack.getItemDamage();
        } catch (Exception e) {}

        if (block != null) sorted.add(stack);
    }

    public void updateSearch() {
        sorted = new ArrayList();
        for (ItemStack stack: ItemHelper.getCreativeItems()) {
            int matches = 0;
            for (IItemFilter filter: GuiItemFilterEditor.INSTANCE.field.getFilters()) {
                if (filter.matches(stack)) {
                    if (blocksOnly) attemptToAddBlock(stack);
                    else sorted.add(stack);
                    matches++;
                }
            }
        }
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return;
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        if (sorted == null) {
            updateSearch();
        }

        int offsetX = GuiCriteriaEditor.INSTANCE.offsetX;
        mouseY -= 95;

        int width = (int) ((double) (screenWidth - 10) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = position; i < position + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                ItemStack stack = sorted.get(i);
                offset.drawStack(stack, -offsetX + 8 + (j * 16), 95 + 45 + (k * 16), 1F);
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