package joshie.progression.gui.newversion.overlays;

import java.util.List;

import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.ProgressionInfo;

public class FeatureDrawable extends FeatureAbstract {
    private List<IDrawable> drawable;
    private int offsetY;

    public FeatureDrawable(List<IDrawable> drawable, int offsetY) {
        this.drawable = drawable;
        this.offsetY = offsetY;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        for (int i = 0; i < drawable.size(); i++) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * i) : (80 * i));
            int mouseOffsetX = mouseX - offsetX - xPos;
            int mouseOffsetY = mouseY - offsetY;
            IDrawable drawing = drawable.get(i);
            //Draw The Delete Button
            if (MCClientHelper.isInEditMode()) {
                int xXcoord = 234;
                if (mouseOffsetX >= 87 && mouseOffsetX <= 97 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    xXcoord += 11;
                }

                offset.drawTexture(xPos, offsetY, ProgressionInfo.textures, 87, 4, xXcoord, 52, 11, 11);
            }
            
            drawing.draw(offset, xPos, offsetY, mouseOffsetX, mouseOffsetY);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return false; //If the item selector is visible, don't process clicks
        for (int i = 0; i < drawable.size(); i++) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * i) : (80 * i));
            int mouseOffsetX = mouseX - offsetX - xPos;
            int mouseOffsetY = mouseY - offsetY;
            IDrawable drawing = drawable.get(i);
            if (MCClientHelper.isInEditMode()) {
                //Delete Button
                if (mouseOffsetX >= 87 && mouseOffsetX <= 97 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    drawing.remove(drawable);
                    return true;
                }
            }
            
            
            if(drawing.mouseClicked(mouseOffsetX, mouseOffsetY, xPos, button)) return true;
        }

        return false;
    }
}
