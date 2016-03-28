package joshie.progression.gui.editors;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IGuiFeature;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;

import java.util.List;

public class FeatureTrigger extends FeatureDrawable<IProgressionTrigger> {
    public FeatureTrigger(String text, List drawable, int offsetY, int x1, int x2, int y1, int y2, IGuiFeature newDrawable, int gradient1, int gradient2, int fontColor) {
        super(text, drawable, offsetY, x1, x2, y1, y2, newDrawable, gradient1, gradient2, fontColor);
    }

    @Override
    public void drawSpecial(IProgressionTrigger drawing, int xPos, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        //We have drawn the deleted button now we check for conditions for triggers.
        boolean display = MCClientHelper.isInEditMode() ? true : drawing.getConditions().size() > 0;
        if (display) {
            int color = Theme.INSTANCE.blackBarBorder;
            if (mouseOffsetX >= 2 && mouseOffsetX <= 87 && mouseOffsetY >= 66 && mouseOffsetY <= 77) {
                color = Theme.INSTANCE.blackBarFontColor;
            }

            offset.drawGradient(xPos, offsetY, 2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
            offset.drawText(xPos, offsetY, Progression.translate((MCClientHelper.isInEditMode() ? "editor" : "display") + ".condition"), 6, 67, Theme.INSTANCE.blackBarFontColor);
        }
    }

    @Override
    public boolean clickSpecial(IProgressionTrigger provider, int mouseOffsetX, int mouseOffsetY) {
        boolean display = MCClientHelper.isInEditMode() ? true: provider.getConditions().size() > 0;
        if (display) {
            if (mouseOffsetX >= 2 && mouseOffsetX <= 87 && mouseOffsetY >= 66 && mouseOffsetY <= 77) {
                GuiConditionEditor.INSTANCE.setTrigger((IProgressionTrigger) provider);
                GuiCore.INSTANCE.setEditor(GuiConditionEditor.INSTANCE);
                return true;
            }
        }

        return false;
    }
}
