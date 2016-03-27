package joshie.progression.gui.editors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.filters.FeatureItemPreview;
import joshie.progression.gui.filters.FilterSelectorItem;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FeatureItemSelector extends FeatureAbstract implements ITextEditable {
    public static FeatureItemSelector INSTANCE = new FeatureItemSelector();
    public IItemSelectable selectable = null;
    private IProgressionFilterSelector filter = FilterSelectorItem.INSTANCE;
    public ArrayList<Object> sorted;
    private String search = "";
    private Position position;
    public int index;

    public FeatureItemSelector() {}

    public IItemSelectable getEditable() {
        return selectable;
    }

    public static enum Position {
        TOP(0), BOTTOM(95);

        public int yOffset;

        private Position(int offset) {
            this.yOffset = offset;
        }
    }

    public void select(IProgressionFilterSelector filter, IItemSelectable selectable, Position type) {
        ItemHelper.addInventory();
        TextEditor.INSTANCE.setEditable(this);
        this.filter = filter;
        this.selectable = selectable;
        this.position = type;
        if (filter == null) this.filter = FilterSelectorItem.INSTANCE;
        updateSearch();
    }

    public void clearEditable() {
        this.selectable = null;
    }

    @Override
    public boolean scroll(int mouseX, int mouseY, boolean scrolledDown) {
        mouseY -= position.yOffset;
        if (mouseY >= 40 && mouseY <= 110) {
            if (selectable != null) {
                int width = (int) ((double) (screenWidth - 10) / 16.133333334D) * 4;
                if (scrolledDown) index = Math.min(sorted.size() - 200, index + width);
                else index = Math.max(0, index - width);
                return true;
            }
        }

        return false;
    }

    public boolean passesFilters(Object stack) {
        if (filter != null) {
            return (filter.isAcceptable(stack));
        }

        return true;
    }

    private void attemptToAdd(Object stack) {
        if (passesFilters(stack)) {
            if (!sorted.contains(stack)) {
                sorted.add(stack);
            }
        }
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
        if (search == null || search.equals("")) {
            sorted = new ArrayList();
            for (Object stack : getAllItems()) {
                attemptToAdd(stack);
            }
        } else {
            index = 0;
            sorted = new ArrayList();
            for (Object stack : getAllItems()) {
                if (stack != null) {
                    if (filter.searchMatches(stack, search.toLowerCase())) {
                        attemptToAdd(stack);
                    }
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

        mouseY -= position.yOffset;
        int width = (int) ((double) (screenWidth - 10) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = index; i < index + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                if (filter.mouseClicked(mouseX, mouseY, j, k)) {
                    selectable.setObject(sorted.get(i));
                    selectable = null;

                    //Update the item preview when selecting an item
                    FeatureItemPreview.INSTANCE.updateSearch();
                    return true;
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
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        if (sorted == null) {
            updateSearch();
        }

        mouseY -= position.yOffset;
        offset.drawGradient(-1, 25 + position.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 15, theme.blackBarGradient1, theme.blackBarGradient2, theme.blackBarBorder);
        offset.drawRectangle(-1, 40 + position.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 73, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);

        offset.drawText(Progression.translate("selector.items"), 5, 29 + position.yOffset, theme.blackBarFontColor);
        offset.drawRectangle(285 - offsetX, 27 + position.yOffset, 200, 12, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);
        offset.drawText(TextEditor.INSTANCE.getText(this), 290, 29 + position.yOffset, theme.blackBarFontColor);

        int width = (int) ((double) (screenWidth - 10) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = index; i < index + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                filter.draw(offset, sorted.get(i), offsetX, j, position.yOffset, k, mouseX, mouseY);

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
        return true;
    }
}