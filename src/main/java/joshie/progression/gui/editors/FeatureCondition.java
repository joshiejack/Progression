package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IConditionProvider;

import java.util.List;

import static joshie.progression.gui.core.GuiList.*;

public class FeatureCondition extends FeatureDrawable<IConditionProvider> {
    public FeatureCondition() {
        super("condition", 45, NEW_CONDITION, THEME.conditionGradient1, THEME.conditionGradient2, THEME.conditionFontColor, 0xFFCCCCCC);
    }

    @Override
    public boolean isReady() {
        return CONDITION_EDITOR.get() != null;
    }

    @Override
    public List<IConditionProvider> getList() {
        return CONDITION_EDITOR.get().getConditions();
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
