package joshie.progression.gui.editors;

import joshie.progression.api.criteria.ICondition;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.gui.editors.insert.FeatureNewCondition;

public class FeatureCondition extends FeatureDrawable<ICondition> {
    public FeatureCondition(ITrigger trigger) {
        super("condition", trigger.getConditions(), 45, FeatureNewCondition.INSTANCE, theme.conditionGradient1, theme.conditionGradient2, theme.conditionFontColor, trigger.getColor());
    }

    @Override
    public int drawSpecial(ICondition drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(ICondition provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
