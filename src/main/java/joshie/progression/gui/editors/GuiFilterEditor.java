package joshie.progression.gui.editors;

import joshie.progression.gui.core.FeatureBarsX2;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IBarProvider;
import joshie.progression.gui.editors.insert.FeatureNewItemFilter;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FeatureItemPreview;

public class GuiFilterEditor extends GuiBaseEditor implements IBarProvider {
    public static final GuiFilterEditor INSTANCE = new GuiFilterEditor();
    private IEditorMode previous;
    private ItemFilterField field;

    private GuiFilterEditor() {}
    
    public ItemFilterField getField() {
        return field;
    }

    public void setFilterSet(ItemFilterField field) {
        this.field = field;
    }
    
    public GuiFilterEditor setPrevious(IEditorMode editor) {
        this.previous = editor;
        return this;
    }
    
    @Override
    public Object getKey() {
        return field;
    }
    
    @Override
    public IEditorMode getPreviousGui() {
        return previous;
    }

    @Override
    public void initData(GuiCore core) {
        super.initData(core);
        //Setup the features
        features.add(new FeatureBarsX2(this, "filter", "preview"));
        features.add(new FeatureFilter(field));
        features.add(FeatureItemPreview.INSTANCE);
        features.add(FeatureFullTextEditor.INSTANCE); //Add the text selector
        features.add(FeatureItemSelector.INSTANCE); //Add the item selector
        features.add(FeatureNewItemFilter.INSTANCE); //Add new item filter screen
        FeatureItemPreview.INSTANCE.updateSearch();
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {
        //FeatureItemPreview.INSTANCE.updateSearch();
    }
    
    @Override
    public boolean guiMouseClicked(boolean overlayvisible, int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public int getColorForBar(BarColorType type) {
        switch (type) {
            case BAR2_GRADIENT1:
                return 0xFF123D62;
            case BAR2_GRADIENT2:
                return 0xFF0C273F;
            case BAR2_BORDER:
                return 0xFF091D2F;
            case BAR1_FONT:
                return theme.triggerBoxFont;
            case BAR1_UNDERLINE:
                return 0xFF091D2F;
            case BAR1_GRADIENT1:
                return 0xFFEBE35A;
            case BAR1_GRADIENT2:
                return 0xFFB78B30;
            case BAR1_BORDER:
                return 0xFF9A6721;
            case BAR2_FONT:
                return theme.rewardBoxFont;
            default:
                return 0;
        }
    }
}