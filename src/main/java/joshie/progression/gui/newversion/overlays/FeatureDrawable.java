package joshie.progression.gui.newversion.overlays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.IEnum;
import joshie.progression.api.IField;
import joshie.progression.api.IFieldProvider;
import joshie.progression.api.ITriggerType;
import joshie.progression.api.gui.ICustomDrawGuiDisplay;
import joshie.progression.api.gui.ICustomDrawGuiEditor;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.EnumField;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
import joshie.progression.lib.GuiIDs;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.renderer.GlStateManager;

public class FeatureDrawable extends FeatureAbstract {
    private List<IFieldProvider> drawable;
    private HashMap<IFieldProvider, List<IField>> fieldsMap;
    private IGuiFeature newDrawable;
    private int crossX1, crossX2, crossY1, crossY2;
    private int gradient1, gradient2, fontColor;
    private int offsetY;

    public FeatureDrawable(List<IFieldProvider> drawable, int offsetY, int x1, int x2, int y1, int y2, IGuiFeature newDrawable, int gradient1, int gradient2, int fontColor) {
        this.drawable = drawable;
        this.offsetY = offsetY;
        this.crossX1 = x1;
        this.crossX2 = x2;
        this.crossY1 = y1;
        this.crossY2 = y2;
        this.newDrawable = newDrawable;
        this.gradient1 = gradient1;
        this.gradient2 = gradient2;
        this.fontColor = fontColor;
        this.fieldsMap = new HashMap();
    }

    private int ticker = 0;

    private List<IField> getFields(IFieldProvider provider) {
        if (fieldsMap.containsKey(provider)) return fieldsMap.get(provider);
        else {
            List<IField> fields = new ArrayList();
            addFieldsViaReflection(provider, fields);
            if (provider instanceof ISpecialFieldProvider) {
                ISpecialFieldProvider special = (ISpecialFieldProvider) provider;
                special.addSpecialFields(fields);
            }

            fieldsMap.put(provider, fields);
            return fields;
        }
    }

    private void addFieldsViaReflection(IFieldProvider provider, List<IField> fields) {
        for (Field field : provider.getClass().getFields()) {
            if (field.getClass().isEnum()) fields.add(new EnumField(field.getName(), (IEnum) provider));
            if (field.getType() == boolean.class) fields.add(new BooleanField(field.getName(), provider));
            if (field.getType() == String.class) fields.add(new TextField(field.getName(), provider));
            if (field.getType() == int.class) fields.add(new TextField(field.getName(), provider));
            if (field.getType() == float.class) fields.add(new TextField(field.getName(), provider));
            if (field.getType() == double.class) fields.add(new TextField(field.getName(), provider));
            if (field.getGenericType().toString().equals("java.util.List<" + ProgressionInfo.FILTER + ">")) {
                fields.add(new ItemFilterField(field.getName(), provider));
            }
        }
    }

    private void drawingDraw(IFieldProvider drawing, DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY) {
        //For updating the render ticker
        ticker++;
        if (ticker == 0 || ticker >= 200) {
            drawing.updateDraw();
            ticker = 1;
        }

        int width = MCClientHelper.isInEditMode() ? 99 : 79;
        helper.drawGradient(renderX, renderY, 1, 2, width, 15, drawing.getColor(), gradient1, gradient2);
        helper.drawText(renderX, renderY, drawing.getLocalisedName(), 6, 6, fontColor);
        if (MCClientHelper.isInEditMode()) {
            ICustomDrawGuiEditor editor = drawing instanceof ICustomDrawGuiEditor ? ((ICustomDrawGuiEditor) drawing) : null;
            if (editor == null || (editor != null && !editor.hideDefaultEditor())) {
                int yStart = 18;
                int index = 0;
                for (IField t : getFields(drawing)) {
                    int color = Theme.INSTANCE.optionsFontColor;
                    int yPos = yStart + (index * 6);
                    if (MCClientHelper.canEdit()) {
                        if (mouseX >= 1 && mouseX <= 84) {
                            if (mouseY >= yPos && mouseY < yPos + 6) {
                                color = Theme.INSTANCE.optionsFontColorHover;
                                List<String> tooltip = new ArrayList();
                                for (int i = 0; i < 5; i++) {
                                    String untranslated = "tooltip." + drawing.getUnlocalisedName() + "." + t.getFieldName() + "." + i;
                                    String translated = Progression.translate(untranslated);
                                    if (!("progression." + untranslated).equals(translated)) {
                                        FeatureTooltip.INSTANCE.addTooltip(translated);
                                    }
                                }
                            }
                        }
                    }

                    t.draw(helper, renderX, renderY, color, yPos, mouseX, mouseY);
                    index++;
                }

                if (editor != null) editor.drawEditor(offset, renderX, renderY, mouseX, mouseY);
            }
        } else {
            ICustomDrawGuiDisplay display = drawing instanceof ICustomDrawGuiDisplay ? ((ICustomDrawGuiDisplay) drawing) : null;
            if (display == null) {
                helper.drawSplitText(renderX, renderY, drawing.getDescription(), 6, 20, 80, fontColor);
                //Draw Shit
            } else display.drawDisplay(offset, renderX, renderY, mouseX, mouseY);
        }
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int xCoord = 0;
        for (int i = 0; i < drawable.size(); i++) {
            int xPos = (MCClientHelper.isInEditMode() ? (100 * xCoord) : (80 * xCoord));
            int mouseOffsetX = mouseX - offsetX - xPos;
            int mouseOffsetY = mouseY - offsetY;
            IFieldProvider drawing = drawable.get(i);
            drawingDraw(drawing, offset, xPos, offsetY, mouseOffsetX, mouseOffsetY);
            //Draw The Delete Button
            if (MCClientHelper.isInEditMode()) {
                int xXcoord = 234;
                if (mouseOffsetX >= 87 && mouseOffsetX <= 97 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    xXcoord += 11;
                }

                offset.drawTexture(xPos, offsetY, ProgressionInfo.textures, 87, 4, xXcoord, 52, 11, 11);
            }

            //We have drawn the deleted button now we check for conditions
            if (drawing instanceof ITriggerType) {
                int color = Theme.INSTANCE.blackBarBorder;
                if (mouseOffsetX >= 2 && mouseOffsetX <= 87 && mouseOffsetY >= 66 && mouseOffsetY <= 77) {
                    color = Theme.INSTANCE.blackBarFontColor;
                }

                offset.drawGradient(xPos, offsetY, 2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
                offset.drawText(xPos, offsetY, Progression.translate((MCClientHelper.isInEditMode() ? "editor" : "display") + ".condition"), 6, 67, Theme.INSTANCE.blackBarFontColor);
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

    private boolean drawingMouseClicked(IFieldProvider provider, int mouseX, int mouseY, int button) {
        if (MCClientHelper.canEdit()) {
            ICustomDrawGuiEditor editor = provider instanceof ICustomDrawGuiEditor ? ((ICustomDrawGuiEditor) provider) : null;
            if (editor == null || (editor != null && !editor.hideDefaultEditor())) {
                int yStart = 18;
                int index = 0;
                for (IField t : getFields(provider)) {
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

            if (editor != null && editor.mouseClicked(mouseX, mouseY)) return true;
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
            IFieldProvider provider = drawable.get(i);
            if (MCClientHelper.isInEditMode()) {
                //Delete Button
                if (mouseOffsetX >= 87 && mouseOffsetX <= 97 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    CollectionHelper.removeAndUpdate(drawable, provider);
                    return true;
                }
            }

            //Delete button done, on to condition editor!!!!!!!!!!!!!!!!!!!!!!!
            if (provider instanceof ITriggerType) {
                int color = Theme.INSTANCE.blackBarBorder;
                if (mouseOffsetX >= 2 && mouseOffsetX <= 87 && mouseOffsetY >= 66 && mouseOffsetY <= 77) {
                    GuiConditionEditor.INSTANCE.setTrigger((ITriggerType) provider);
                    GuiCriteriaEditor.INSTANCE.switching = true; //Switching yey!
                    MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.CONDITION, MCClientHelper.getWorld(), 0, 0, 0);
                }

                offset.drawGradient(xPos, offsetY, 2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
                offset.drawText(xPos, offsetY, Progression.translate((MCClientHelper.isInEditMode() ? "editor" : "display") + ".condition"), 6, 67, Theme.INSTANCE.blackBarFontColor);
            }

            if (drawingMouseClicked(provider, mouseOffsetX, mouseOffsetY, button)) return true;
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
