package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.lwjgl.opengl.GL11;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import joshie.progression.Progression;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.gui.GuiTreeEditor;
import joshie.progression.gui.editors.EditText.ITextEditable;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.selector.filters.ActionFilter;
import joshie.progression.gui.selector.filters.ItemFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.client.renderer.GlStateManager;

public class FeatureItemSelector extends FeatureAbstract implements ITextEditable {
    public static FeatureItemSelector INSTANCE = new FeatureItemSelector();
    private IItemSelectable selectable = null;
    private IFilterSelectorFilter filter = null;
    private ArrayList<Object> sorted;
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

    public void select(IFilterSelectorFilter filter, IItemSelectable selectable, Type type) {
        ItemHelper.addInventory();
        TextEditor.INSTANCE.setEditable(this);
        this.filter = filter;
        this.selectable = selectable;
        this.type = type;
        System.out.println(filter);
        if (this.filter == null) this.filter = ItemFilter.INSTANCE;
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
            position = 0;
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

        mouseY -= type.yOffset;
        int width = (int) ((double) (screenWidth - 10) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = position; i < position + (width * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                if (filter.mouseClicked(mouseX, mouseY, j, k)) {
                    selectable.setObject(sorted.get(i));
                    selectable = null;
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
                filter.draw(offset, sorted.get(i), offsetX, j, type.yOffset, k, mouseX, mouseY);

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