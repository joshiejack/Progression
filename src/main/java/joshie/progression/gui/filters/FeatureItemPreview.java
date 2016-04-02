package joshie.progression.gui.filters;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.FeatureItemSelector;
import joshie.progression.gui.editors.FeatureItemSelector.Position;
import joshie.progression.gui.editors.GuiItemFilterEditor;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FeatureItemPreview extends FeatureAbstract {
    public static FeatureItemPreview INSTANCE = new FeatureItemPreview();
    private IProgressionFilterSelector filter = FilterSelectorItem.INSTANCE;
    private ArrayList<Object> sorted;
    private int position;

    public FeatureItemPreview() {}

    public void select(IProgressionFilterSelector filter) {
        this.filter = filter;
        updateSearch();
    }

    @Override
    public boolean scroll(int mouseX, int mouseY, boolean scrolledDown) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return false;
        mouseY -= 95;
        if (mouseY >= 40 && mouseY <= 110) {
            if (scrolledDown) position = Math.min(sorted.size() - filter.getChange(), position + filter.getChange());
            else position = Math.max(0, position - filter.getChange());
            return true;
        }

        return false;
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
        int size1 = sorted.size();
        position = 0; //Reset the position on update

        if (GuiItemFilterEditor.INSTANCE.getField() == null) return; //NO UPDATES!!!
        sorted = new ArrayList();
        for (Object stack: getAllItems()) {
            for (IProgressionFilter filter: GuiItemFilterEditor.INSTANCE.getField().getFilters()) {
                if (filter.matches(stack)) {
                    sorted.add(stack);
                }
            }
        }

        if (sorted.size() < size1) {
            GuiCore.INSTANCE.offsetX = 0; //Reset the offset on update
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
                filter.draw(offset, sorted.get(i), 0, j, Position.BOTTOM.yOffset, k, mouseX, mouseY);

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