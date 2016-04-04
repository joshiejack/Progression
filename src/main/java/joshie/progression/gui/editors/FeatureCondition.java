package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IConditionProvider;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.gui.editors.insert.FeatureNewCondition;

public class FeatureCondition extends FeatureDrawable<IConditionProvider> {
    public FeatureCondition(ITriggerProvider trigger) {
        super("condition", trigger.getConditions(), 45, FeatureNewCondition.INSTANCE, theme.conditionGradient1, theme.conditionGradient2, theme.conditionFontColor, trigger.getColor());
    }

    @Override
    public int drawSpecial(IConditionProvider drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IConditionProvider provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
