package joshie.progression.gui.newversion;

import joshie.progression.Progression;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.newversion.overlays.FeatureBarsX2;
import joshie.progression.gui.newversion.overlays.FeatureDrawable;
import joshie.progression.gui.newversion.overlays.FeatureItemPreview;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.FeatureNewItemFilter;
import joshie.progression.gui.newversion.overlays.IBarProvider;
import joshie.progression.lib.GuiIDs;
import net.minecraft.util.EnumChatFormatting;

public class GuiItemFilterEditor extends GuiCore implements IBarProvider {
    public static final GuiItemFilterEditor INSTANCE = new GuiItemFilterEditor();
    public ItemFilterField field;

    private GuiItemFilterEditor() {}

    public void setFilterSet(ItemFilterField field) {
        this.field = field;
    }

    @Override
    public Object getKey() {
        return field;
    }
    
    @Override
    public int getPreviousGuiID() {
        return GuiIDs.CRITERIA;
    }

    @Override
    public void initGuiData() {
        switching = false;
        //Setup the features
        features.add(new FeatureBarsX2(this, "filters", "preview"));
        features.add(new FeatureDrawable(EnumChatFormatting.BOLD + Progression.translate("new.filter"), field.getFilters(), 45, 201, 201, 64, 119, FeatureNewItemFilter.INSTANCE, theme.triggerGradient1, theme.triggerGradient2, theme.triggerFontColor));
        features.add(FeatureItemPreview.INSTANCE);
        features.add(FeatureItemSelector.INSTANCE); //Add the item selector
        features.add(FeatureNewItemFilter.INSTANCE); //Add new item filter screen
        FeatureItemPreview.INSTANCE.updateSearch();
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {
        FeatureItemPreview.INSTANCE.updateSearch();
    }
    
    @Override
    public boolean guiMouseClicked(boolean overlayvisible, int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public int getColorForBar(BarColorType type) {
        switch (type) {
            case BAR1_GRADIENT1:
                return theme.triggerBoxGradient1;
            case BAR1_GRADIENT2:
                return theme.triggerBoxGradient2;
            case BAR1_BORDER:
                return theme.triggerBoxUnderline1;
            case BAR1_FONT:
                return theme.triggerBoxFont;
            case BAR1_UNDERLINE:
                return theme.triggerBoxUnderline1;
            case BAR2_GRADIENT1:
                return theme.rewardBoxGradient1;
            case BAR2_GRADIENT2:
                return theme.rewardBoxGradient2;
            case BAR2_BORDER:
                return theme.rewardBoxBorder;
            case BAR2_FONT:
                return theme.rewardBoxFont;
            default:
                return 0;
        }
    }
}
