package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IProgressionCondition;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.gui.editors.insert.FeatureNewCondition;

public class FeatureCondition extends FeatureDrawable<IProgressionCondition> {
    public FeatureCondition(IProgressionTrigger trigger) {
        super("condition", trigger.getConditions(), 45, FeatureNewCondition.INSTANCE, theme.conditionGradient1, theme.conditionGradient2, theme.conditionFontColor, trigger.getColor());
    }

    @Override
    public int drawSpecial(IProgressionCondition drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IProgressionCondition provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
