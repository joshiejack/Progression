package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.ICancelable;
import joshie.progression.api.IField;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
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
    
    private int ticker = 0;
    
    private void drawingDraw(IDrawable drawing, DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY) {
        //For updating the render ticker
        ticker++;
        if (ticker == 0 || ticker >= 200) {
            drawing.update();
            ticker = 1;
        }

        ICancelable cancelable = drawing instanceof ICancelable ? ((ICancelable)drawing) : null;
        int width = MCClientHelper.isInEditMode() ? 99 : 79;
        helper.drawGradient(renderX, renderY, 1, 2, width, 15, drawing.getColor(), drawing.getGradient1(), drawing.getGradient2());
        helper.drawText(renderX, renderY, drawing.getLocalisedName(), 6, 6, drawing.getFontColor());
        if (MCClientHelper.isInEditMode()) {
            if (cancelable == null || !cancelable.isCanceling()) {
                int yStart = cancelable == null || !cancelable.isCancelable() ? 18 : 24;
                int index = 0;
                for (IField t : drawing.getFields()) {
                    int color = Theme.INSTANCE.optionsFontColor;
                    int yPos = yStart + (index * 6);
                    if (MCClientHelper.canEdit()) {
                        if (mouseX >= 1 && mouseX <= 84) {
                            if (mouseY >= yPos && mouseY < yPos + 6) {
                                color = Theme.INSTANCE.optionsFontColorHover;
                                List<String> tooltip = new ArrayList();
                                drawing.addFieldTooltip(t.getFieldName(), tooltip);
                                if (!tooltip.isEmpty()) FeatureTooltip.INSTANCE.addTooltip(tooltip);
                            }
                        }
                    }

                    t.draw(helper, renderX, renderY, color, yPos);
                    index++;
                }
            }

            if (cancelable != null && cancelable.isCancelable()) {
                int color = Theme.INSTANCE.optionsFontColor;
                if (MCClientHelper.canEdit()) {
                    if (mouseX >= 1 && mouseX <= 84) {
                        if (mouseY >= 18 && mouseY < 24) {
                            color = Theme.INSTANCE.optionsFontColorHover;
                        }
                    }

                    helper.drawSplitText(renderX, renderY, "cancel: " + cancelable.isCanceling(), 4, 18, 105, color, 0.75F);
                }
            }
        } else {
            helper.drawSplitText(renderX, renderY, drawing.getDescription(), 6, 20, 80, helper.getTheme().triggerFontColor);
            drawing.drawDisplay(mouseX, mouseY);
        }
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int xCoord = 0;
        for (int i = 0; i < drawable.size(); i++) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * xCoord) : (80 * xCoord));
            int mouseOffsetX = mouseX - offsetX - xPos;
            int mouseOffsetY = mouseY - offsetY;
            IDrawable drawing = drawable.get(i);
            drawingDraw(drawing, offset, xPos, offsetY, mouseOffsetX, mouseOffsetY);
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
            if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
                crossX = crossX2;
                crossY = crossY2;
            }

            GlStateManager.color(1F, 1F, 1F);
            offset.drawTexture(xPos, offsetY, ProgressionInfo.textures, 15, 10, crossX, crossY, 55, 55);
        }
    }
    
    private boolean drawingMouseClicked(IDrawable drawing, int mouseX, int mouseY, int button) {
        ICancelable cancelable = drawing instanceof ICancelable ? ((ICancelable)drawing) : null;
        if (MCClientHelper.canEdit()) {
            if (cancelable == null || !cancelable.isCanceling()) {
                int yStart = cancelable == null || !cancelable.isCancelable() ? 18 : 24;
                int index = 0;
                for (IField t : drawing.getFields()) {
                    if (t.attemptClick(mouseX, mouseY)) {
                        return true;
                    }

                    int color = Theme.INSTANCE.optionsFontColor;
                    int yPos = yStart + (index * 6);
                    if (mouseX >= 1 && mouseX <= 99) {
                        if (mouseY >= yPos && mouseY < yPos + 6) {
                            t.click();
                            return true;
                        }
                    }

                    index++;
                }
            }

            if (cancelable != null && cancelable.isCancelable()) {
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= 18 && mouseY < 24) {
                        cancelable.setCanceling(!cancelable.isCanceling());
                    }
                }
            }
        }

        return false;
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

            if (drawingMouseClicked(drawing, mouseOffsetX, mouseOffsetY, button)) return true;
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
