package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.lwjgl.opengl.GL11;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import joshie.progression.api.IFilter;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class FeatureItemPreview extends FeatureAbstract {
    public static FeatureItemPreview INSTANCE = new FeatureItemPreview();
    private IFilterSelectorFilter filter;
    private ArrayList<Object> sorted;
    private boolean blocksOnly;
    private int position;

    public FeatureItemPreview() {}

    public void select(IFilterSelectorFilter filter) {
        this.filter = filter;
        updateSearch();
    }

    @Override
    public boolean scroll(int mouseX, int mouseY, boolean scrolledDown) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return false;
        mouseY -= 95;
        if (mouseY >= 40 && mouseY <= 110) {
            int width = (int) ((double) (screenWidth - 10) / filter.getScale()) * 4;
            if (scrolledDown) position = Math.min(sorted.size() - filter.getChange(), position + filter.getChange());
            else position = Math.max(0, position - filter.getChange());
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
    
    private static Cache<Object, ArrayList<Object>> cacheList = CacheBuilder.newBuilder().maximumSize(64).build();

    public ArrayList<Object> getAllItems() {
        try {
            return cacheList.get(filter, new Callable<ArrayList<Object>>() {
                @Override
                public ArrayList<Object> call() throws Exception {
                    return (ArrayList<Object>) filter.getAllItems();
                }
            });
        } catch (Exception e) {
            return (ArrayList<Object>) filter.getAllItems();
        }
    }

    public void updateSearch() {
        sorted = new ArrayList();
        for (Object stack: getAllItems()) {
            int matches = 0;
            for (IFilter filter: GuiItemFilterEditor.INSTANCE.field.getFilters()) {
                if (filter.matches(stack)) {
                    sorted.add(stack);
                    matches++;
                }
            }
        }
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return;
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        if (sorted == null) {
            updateSearch();
        }

        mouseY -= 95;

        int width = (int) ((double) (screenWidth - 10) / filter.getScale());
        int j = 0;
        int k = 0;
        for (int i = position; i < position + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                filter.draw(offset, sorted.get(i), 0, j, Type.TRIGGER.yOffset, k, mouseX, mouseY);

                j++;

                if (j >= width) {
                    j = 0;
                    k++;
                }
            }
        }
    }

    @Override
    public boolean isOverlay() {
        return false;
    }
}