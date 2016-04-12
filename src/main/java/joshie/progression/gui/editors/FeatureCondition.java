package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IConditionProvider;
import joshie.progression.api.criteria.ITriggerProvider;

import static joshie.progression.gui.core.GuiList.NEW_CONDITION;
import static joshie.progression.gui.core.GuiList.THEME;

public class FeatureCondition extends FeatureDrawable<IConditionProvider> {
    public FeatureCondition() {
        super("condition", 45, NEW_CONDITION, THEME.conditionGradient1, THEME.conditionGradient2, THEME.conditionFontColor, 0xFF000000);
    }

    public FeatureCondition setTrigger(ITriggerProvider trigger) {
        color = trigger.getColor();
        setDrawable(trigger.getConditions());
        return this;
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
