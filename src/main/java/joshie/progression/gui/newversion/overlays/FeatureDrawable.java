package joshie.progression.gui.newversion.overlays;

import java.util.List;

import joshie.progression.gui.NewReward;
import joshie.progression.gui.NewTrigger;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.renderer.GlStateManager;

public class FeatureDrawable extends FeatureAbstract {
    private List<IDrawable> drawable;
    private IGuiFeature newDrawable;
    private int crossX1, crossX2, crossY1, crossY2;
    private int offsetY;

    public FeatureDrawable(List<IDrawable> drawable, int offsetY, int x1, int x2, int y1, int y2, IGuiFeature newDrawable) {
        this.drawable = drawable;
        this.offsetY = offsetY;
        this.crossX1 = x1;
        this.crossX2 = x2;
        this.crossY1 = y1;
        this.crossY2 = y2;
        this.newDrawable = newDrawable;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int xCoord = 0;
        for (int i = 0; i < drawable.size(); i++) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * xCoord) : (80 * xCoord));
            int mouseOffsetX = mouseX - offsetX - xPos;
            int mouseOffsetY = mouseY - offsetY;
            IDrawable drawing = drawable.get(i);
            drawing.draw(offset, xPos, offsetY, mouseOffsetX, mouseOffsetY);
            //Draw The Delete Button
            if (MCClientHelper.isInEditMode()) {
                int xXcoord = 234;
                if (mouseOffsetX >= 87 && mouseOffsetX <= 97 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    xXcoord += 11;
                }

                offset.drawTexture(xPos, offsetY, ProgressionInfo.textures, 87, 4, xXcoord, 52, 11, 11);
            }

            xCoord++;
        }

        //Draw the addition texture
        if (MCClientHelper.isInEditMode()) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * xCoord) : (80 * xCoord));
            int mouseOffsetX = mouseX - offsetX - xPos;
            int mouseOffsetY = mouseY - offsetY;

            int crossX = crossX1;
            int crossY = crossY1;
            if (!NewTrigger.INSTANCE.isVisible() && !NewReward.INSTANCE.isVisible()) {
                if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
                    crossX = crossX2;
                    crossY = crossY2;
                }
            }

            GlStateManager.color(1F, 1F, 1F);
            offset.drawTexture(xPos, offsetY, ProgressionInfo.textures, 15, 10, crossX, crossY, 55, 55);
        }
    }

    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, int button) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return false; //If the item selector is visible, don't process clicks
        if (FeatureNew.IS_OPEN) return false;
        int xCoord = 0;
        for (int i = 0; i < drawable.size(); i++) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * xCoord) : (80 * xCoord));
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

            if (drawing.mouseClicked(mouseOffsetX, mouseOffsetY, xPos, button)) return true;
            xCoord++;
        }

        //Now that we've tried all, let's try the new button
        if (MCClientHelper.isInEditMode()) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * xCoord) : (80 * xCoord));
            int mouseOffsetX = mouseX - offsetX - xPos;
            int mouseOffsetY = mouseY - offsetY;
            if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
                newDrawable.init(offset.getGui());
                newDrawable.setVisibility(true);
                return true;
            }
        }

        return false;
    }
}
