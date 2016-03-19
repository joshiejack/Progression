package joshie.progression.gui.newversion;

import java.util.ArrayList;

import joshie.progression.api.ITriggerType;
import joshie.progression.gui.newversion.overlays.FeatureBarsX1;
import joshie.progression.gui.newversion.overlays.FeatureDrawable;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.FeatureNewCondition;
import joshie.progression.gui.newversion.overlays.FeatureNewReward;
import joshie.progression.gui.newversion.overlays.IBarProvider;
import joshie.progression.lib.GuiIDs;

public class GuiConditionEditor extends GuiCore implements IBarProvider {
    public static final GuiConditionEditor INSTANCE = new GuiConditionEditor();
    private ITriggerType trigger;

    private GuiConditionEditor() {}

    public void setTrigger(ITriggerType trigger) {
        this.trigger = trigger;
    }
    
    public ITriggerType getTrigger() {
        return trigger;
    }

    @Override
    public Object getKey() {
        return trigger;
    }

    @Override
    public int getPreviousGuiID() {
        return GuiIDs.CRITERIA;
    }

    @Override
    public void initGuiData() {
        switching = false;
        //Setup the features
        features.add(new FeatureBarsX1(this, "conditions"));
        features.add(new FeatureDrawable(new ArrayList(trigger.getConditions()), 45, 201, 201, 64, 119, FeatureNewCondition.INSTANCE, theme.conditionGradient1, theme.conditionGradient2, theme.conditionFontColor));
        //features.add(new FeatureDrawable(new ArrayList(criteria.rewards), 140, 0, 55, 201, 201, FeatureNewReward.INSTANCE, theme.rewardBoxGradient1, theme.rewardBoxGradient2, theme.rewardBoxFont));
        features.add(FeatureItemSelector.INSTANCE); //Add the item selector
        features.add(FeatureNewCondition.INSTANCE); //Add new trigger popup
        features.add(FeatureNewReward.INSTANCE); //Add new reward popup
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {}

    @Override
    public boolean guiMouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public int getColorForBar(BarColorType type) {
        switch (type) {
            case BAR1_GRADIENT1:
                return theme.conditionEditorGradient1;
            case BAR1_GRADIENT2:
                return theme.conditionEditorGradient2;
            case BAR1_BORDER:
                return theme.conditionEditorUnderline2;
            case BAR1_FONT:
                return theme.conditionEditorFont;
            case BAR1_UNDERLINE:
                return theme.conditionEditorUnderline;
            default:
                return 0;
        }
    }
}
