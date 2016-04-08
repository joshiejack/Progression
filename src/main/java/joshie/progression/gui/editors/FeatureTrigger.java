package joshie.progression.gui.editors;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IConditionProvider;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.insert.FeatureNewTrigger;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;

import static joshie.progression.api.special.DisplayMode.DISPLAY;
import static joshie.progression.api.special.DisplayMode.EDIT;

public class FeatureTrigger extends FeatureDrawable<ITriggerProvider> {
    public FeatureTrigger(ICriteria criteria) {
        super("trigger", criteria.getTriggers(), 45, FeatureNewTrigger.INSTANCE, theme.triggerGradient1, theme.triggerGradient2, theme.triggerFontColor, theme.triggerBoxGradient2);
    }

    @Override
    public int drawSpecial(ITriggerProvider drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        //System.out.println("Reading in the trigger: " + drawing + " for uuid " + drawing.getUniqueID());
        //We have drawn the deleted button now we check for conditions for triggers.
        if (mode == EDIT) {
            int color = Theme.INSTANCE.blackBarBorder;
            if (mouseOffsetX >= 2 && mouseOffsetX <= drawing.getWidth(mode) - 3 && mouseOffsetY >= 65 && mouseOffsetY <= 77) {
                color = Theme.INSTANCE.blackBarFontColor;
            }

            offset.drawGradient(offsetX, offsetY, 2, 65, drawing.getWidth(mode) - 5, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
            offset.drawText(offsetX, offsetY, Progression.translate((MCClientHelper.isInEditMode() ? "editor" : "display") + ".condition"), (drawing.getWidth(EDIT) / 5), 67, Theme.INSTANCE.blackBarFontColor);
            offsetX = super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
        } else {
            if (drawing.isVisible()) offsetX = super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
            for (IConditionProvider condition: drawing.getConditions()) {
                if (!condition.isVisible()) continue;
                mouseOffsetX = mouseOffsetX - offsetX;
                drawingDraw(condition, offset, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
                offsetX = offsetX + condition.getWidth(mode);
            }
        }

        return offsetX;
    }

    @Override
    public boolean clickSpecial(ITriggerProvider provider, int mouseOffsetX, int mouseOffsetY) {
        if (mode == DISPLAY) return false;
        if (mouseOffsetX >= 2 && mouseOffsetX <= provider.getWidth(mode) - 3 && mouseOffsetY >= 66 && mouseOffsetY <= 77) {
            GuiConditionEditor.INSTANCE.setTrigger(provider);
            GuiCore.INSTANCE.setEditor(GuiConditionEditor.INSTANCE);
            return true;
        }

        return false;
    }
}
