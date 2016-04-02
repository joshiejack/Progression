package joshie.progression.gui.editors;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IFieldProvider;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.insert.FeatureNewTrigger;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;

import static joshie.progression.api.special.DisplayMode.EDIT;

public class FeatureTrigger extends FeatureDrawable<IProgressionTrigger> {
    public FeatureTrigger(IProgressionCriteria criteria) {
        super("trigger", criteria.getTriggers(), 45, FeatureNewTrigger.INSTANCE, theme.triggerGradient1, theme.triggerGradient2, theme.triggerFontColor, theme.triggerBoxGradient2);
    }

    @Override
    public int drawSpecial(IProgressionTrigger drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
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
            offsetX = super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
            for (int i = 0; i < drawing.getConditions().size(); i++) {
                IFieldProvider condition = drawing.getConditions().get(i);
                mouseOffsetX = mouseOffsetX - offsetX;
                drawingDraw(condition, offset, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
                offsetX = offsetX + condition.getWidth(mode);
            }
        }

        return offsetX;
    }

    @Override
    public boolean clickSpecial(IProgressionTrigger provider, int mouseOffsetX, int mouseOffsetY) {
        if (mouseOffsetX >= 2 && mouseOffsetX <= provider.getWidth(mode) - 3 && mouseOffsetY >= 66 && mouseOffsetY <= 77) {
            GuiConditionEditor.INSTANCE.setTrigger((IProgressionTrigger) provider);
            GuiCore.INSTANCE.setEditor(GuiConditionEditor.INSTANCE);
            return true;
        }

        return false;
    }
}
