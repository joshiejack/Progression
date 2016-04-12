package joshie.progression.gui.editors;

import joshie.progression.gui.core.IBarProvider;
import joshie.progression.gui.fields.ItemFilterField;

import static joshie.progression.gui.core.GuiList.*;

public class GuiFilterEditor extends GuiBaseEditorRule<ItemFilterField> implements IBarProvider {
    private ItemFilterField field;
    private IEditorMode previous;

    public GuiFilterEditor() {
        //Setup the features
        features.add(BACKGROUND);
        features.add(FILTER_BG);
        features.add(FILTERS);
        features.add(PREVIEW);
        features.add(TEXT_EDITOR_FULL);
        features.add(ITEM_EDITOR);
        features.add(NEW_FILTER);
        features.add(FOOTER);
    }

    @Override
    public ItemFilterField get() {
        return this.field;
    }

    @Override
    public void set(ItemFilterField field) {
        this.field = field;
    }

    public GuiFilterEditor setPrevious(IEditorMode editor) {
        this.previous = editor;
        return this;
    }
    
    @Override
    public IEditorMode getPreviousGui() {
        return previous;
    }

    @Override
    public void initData() {
        //Setup the features
        FILTER_BG.setProvider(this);
        FILTERS.setFilterField(field);
        PREVIEW.updateSearch();
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {
        //FeatureItemPreview.GROUP_EDITOR.updateSearch();
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
                return THEME.triggerBoxFont;
            case BAR1_UNDERLINE:
                return 0xFF091D2F;
            case BAR1_GRADIENT1:
                return 0xFFEBE35A;
            case BAR1_GRADIENT2:
                return 0xFFB78B30;
            case BAR1_BORDER:
                return 0xFF9A6721;
            case BAR2_FONT:
                return THEME.rewardBoxFont;
            default:
                return 0;
        }
    }
}