package joshie.progression.gui.editors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.Progression;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.gui.Position;
import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import static joshie.progression.gui.core.GuiList.*;

public class FeatureItemSelector extends FeatureAbstract implements ITextEditable {
    public IItemSelectable selectable = null;
    private IFilterType filter = FilterTypeItem.INSTANCE;
    public ArrayList<Object> sorted;
    private String search = "";
    private Position position;
    public int index;

    public FeatureItemSelector() {}

    public IItemSelectable getEditable() {
        return selectable;
    }

    public boolean select(IFilterType filter, IItemSelectable selectable, Position type) {
        ItemHelper.addInventory();
        TEXT_EDITOR_SIMPLE.setEditable(this);
        this.filter = filter;
        this.selectable = selectable;
        this.position = type;
        if (filter == null) this.filter = FilterTypeItem.INSTANCE;
        updateSearch();
        return true;
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

    private void attemptToAdd(ArrayList sorted, Object stack) {
        if (passesFilters(stack)) {
            if (!sorted.contains(stack)) {
                sorted.add(stack);
            }
        }
    }

    private static Cache<Object, ArrayList<Object>> cacheList = CacheBuilder.newBuilder().maximumSize(64).build();
    private static Cache<String, ArrayList<Object>> cacheSearch = CacheBuilder.newBuilder().maximumSize(256).build();
    private static HashMap<IFilterType, ArrayList<Object>> emptyList = new HashMap();

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

    private void buildEmptyList() {
        if (emptyList.get(filter) == null) {
            ArrayList emptyList = new ArrayList();
            for (Object stack : getAllItems()) {
                attemptToAdd(emptyList, stack);
            }

            FeatureItemSelector.emptyList.put(filter, emptyList);
        }
    }

    public void updateSearch() {
        buildEmptyList();
        if (search == null) sorted = emptyList.get(filter);
        else {
            index = 0; //reset the index

            try {
                sorted = cacheSearch.get(search, new Callable<ArrayList<Object>>() {
                    @Override
                    public ArrayList<Object> call() throws Exception {
                        ArrayList list = new ArrayList();
                        for (Object stack : getAllItems()) {
                            if (stack != null) {
                                if (filter.searchMatches(stack, search.toLowerCase())) {
                                    attemptToAdd(list, stack);
                                }
                            }
                        }

                        return list;
                    }
                });
            } catch (Exception e) {}
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
                    PREVIEW.updateSearch();
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
        offset.drawGradient(-1, 25 + position.yOffset, CORE.mc.displayWidth, 15, THEME.blackBarGradient1, THEME.blackBarGradient2, THEME.blackBarBorder);
        offset.drawRectangle(-1, 40 + position.yOffset, CORE.mc.displayWidth, 73, THEME.blackBarUnderLine, THEME.blackBarUnderLineBorder);

        offset.drawText(Progression.translate("selector.items"), 5, 29 + position.yOffset, THEME.blackBarFontColor);
        offset.drawRectangle(285, 27 + position.yOffset, 200, 12, THEME.blackBarUnderLine, THEME.blackBarUnderLineBorder);
        offset.drawText(TEXT_EDITOR_SIMPLE.getText(this), 290, 29 + position.yOffset, THEME.blackBarFontColor);

        int width = (int) ((double) (screenWidth - 10) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = index; i < index + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                filter.draw(offset, sorted.get(i), 0, j, position.yOffset, k, mouseX, mouseY);

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