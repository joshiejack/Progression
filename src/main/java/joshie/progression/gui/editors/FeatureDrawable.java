package joshie.progression.gui.editors;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IFieldProvider;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.api.gui.ICustomDrawGuiDisplay;
import joshie.progression.api.gui.ICustomDrawGuiEditor;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IEnum;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.IGuiFeature;
import joshie.progression.gui.editors.FeatureItemSelector.Position;
import joshie.progression.gui.editors.insert.FeatureNew;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.EnumField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.gui.filters.FeatureItemPreview;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.renderer.GlStateManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeatureDrawable<T extends IFieldProvider> extends FeatureAbstract {
    private List<IFieldProvider> drawable;
    private HashMap<IFieldProvider, List<IProgressionField>> fieldsMap;
    private IGuiFeature newDrawable;
    private int crossX1, crossX2, crossY1, crossY2;
    private int gradient1, gradient2, fontColor;
    private int offsetY;
    private String text;

    public FeatureDrawable(String text, List drawable, int offsetY, int x1, int x2, int y1, int y2, IGuiFeature newDrawable, int gradient1, int gradient2, int fontColor) {
        this.text = text;
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

    protected DisplayMode getMode() {
        return MCClientHelper.isInEditMode() ? DisplayMode.EDIT: DisplayMode.DISPLAY;
    }

    private List<IProgressionField> getFields(IFieldProvider provider, DisplayMode mode) {
        if (fieldsMap.containsKey(provider)) return fieldsMap.get(provider);
        else {
            List<IProgressionField> fields = new ArrayList();
            if (mode == DisplayMode.EDIT) addFieldsViaReflection(provider, fields);
            boolean hideitemfields = false;
            if (provider instanceof ISpecialFieldProvider) {
                ISpecialFieldProvider special = (ISpecialFieldProvider) provider;
                special.addSpecialFields(fields, mode);
            }

            fieldsMap.put(provider, fields);
            return fields;
        }
    }

    private void addFieldsViaReflection(IFieldProvider provider, List<IProgressionField> fields) {
        Position type = provider instanceof IProgressionReward ? Position.TOP : Position.BOTTOM;
        for (Field field : provider.getClass().getFields()) {
            try {
                if (field.getType() == boolean.class) fields.add(new BooleanField(field.getName(), provider));
                else if (field.getType() == String.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.getType() == int.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.getType() == float.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.getType() == double.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.get(provider) instanceof Enum) fields.add(new EnumField(field.getName(), (IEnum) provider));
            } catch (Exception e) {}
        }
    }

    protected void drawingDraw(IFieldProvider drawing, DrawHelper helper, int renderX, int renderY, int mouseX, int mouseY) {
        //For updating the render ticker
        ticker++;
        if (ticker == 0 || ticker >= 200) {
            drawing.updateDraw();
            ticker = 1;
        }

        int width = MCClientHelper.isInEditMode() ? 99 : drawing.getWidth(getMode()) - 1;
        helper.drawGradient(renderX, renderY, 1, 2, width, 15, drawing.getColor(), gradient1, gradient2);
        helper.drawText(renderX, renderY, drawing.getLocalisedName(), 6, 6, fontColor);

        ICustomDrawGuiEditor editor = drawing instanceof ICustomDrawGuiEditor ? ((ICustomDrawGuiEditor) drawing) : null;
        if (editor == null || (editor != null && !editor.hideDefaultEditor())) {
            DisplayMode mode = getMode();
            int yStart = 18;
            int index = 0;
            for (IProgressionField t : getFields(drawing, mode)) {
                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = yStart + (index * 6);
                if (MCClientHelper.isInEditMode()) {
                    if (mouseX >= 1 && mouseX <= 84) {
                        if (mouseY >= yPos && mouseY < yPos + 6) {
                            if (mode == DisplayMode.EDIT) color = Theme.INSTANCE.optionsFontColorHover;
                            List<String> tooltip = new ArrayList();
                            for (int i = 0; i < 10; i++) {
                                String untranslated = mode.name().toLowerCase() + ".tooltip." + drawing.getUnlocalisedName() + "." + t.getFieldName() + "." + i;
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
            if (mode == DisplayMode.DISPLAY) {
                ICustomDrawGuiDisplay display = drawing instanceof ICustomDrawGuiDisplay ? ((ICustomDrawGuiDisplay) drawing) : null;
                if (display == null) {
                    helper.drawSplitText(renderX, renderY, drawing.getDescription(), 4, 20, drawing.getWidth(mode) + drawing.getWidth(mode) / 4, fontColor, 0.75F);
                    //Draw Shit
                } else display.drawDisplay(offset, renderX, renderY, mouseX, mouseY);
            }
        }
    }

    public int drawSpecial(T drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return offsetX + drawing.getWidth(getMode());
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int offsetX = 0;
        for (int i = 0; i < drawable.size(); i++) {
            IFieldProvider drawing = drawable.get(i);
            int mouseOffsetX = mouseX - this.offsetX - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;
            drawingDraw(drawing, offset, offsetX, this.offsetY, mouseOffsetX, mouseOffsetY);
            //Draw The Delete Button
            if (MCClientHelper.isInEditMode()) {
                int xXcoord = 234;
                if (mouseOffsetX >= 87 && mouseOffsetX <= 97 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    xXcoord += 11;
                }

                offset.drawTexture(offsetX, offsetY, ProgressionInfo.textures, 87, 4, xXcoord, 52, 11, 11);
            }

            offsetX = drawSpecial((T)drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
        }

        //Draw the addition texture
        if (MCClientHelper.isInEditMode()) {
            int mouseOffsetX = mouseX - this.offsetX - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;

            int crossX = crossX1;
            int crossY = crossY1;
            if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
                FeatureTooltip.INSTANCE.addTooltip(text);
                crossX = crossX2;
                crossY = crossY2;
            }

            GlStateManager.enableBlend();
            GlStateManager.color(1F, 1F, 1F);
            offset.drawTexture(offsetX, offsetY, ProgressionInfo.textures, 15, 10, crossX, crossY, 55, 55);
        }
    }

    private boolean drawingMouseClicked(IFieldProvider provider, int mouseX, int mouseY, int button) {
        if (MCClientHelper.isInEditMode()) {
            ICustomDrawGuiEditor editor = provider instanceof ICustomDrawGuiEditor ? ((ICustomDrawGuiEditor) provider) : null;
            if (editor == null || (editor != null && !editor.hideDefaultEditor())) {
                int yStart = 18;
                int index = 0;
                for (IProgressionField t : getFields(provider, DisplayMode.EDIT)) {
                    if (t.attemptClick(mouseX, mouseY)) {
                        return true;
                    }

                    int color = Theme.INSTANCE.optionsFontColor;
                    int yPos = yStart + (index * 6);
                    if (mouseX >= 1 && mouseX <= 99) {
                        if (mouseY >= yPos && mouseY < yPos + 6) {
                            t.click();

                            //Update the item preview when selecting toggling something
                            FeatureItemPreview.INSTANCE.updateSearch();
                            return true;
                        }
                    }

                    index++;
                }
            }

            //Update the item preview when selecting toggling something
            FeatureItemPreview.INSTANCE.updateSearch();
            if (editor != null && editor.mouseClicked(mouseX, mouseY)) return true;
        }

        return false;
    }

    public boolean clickSpecial(T provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }

    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, int button) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return false; //If the item selector is visible, don't process clicks
        if (FeatureNew.IS_OPEN) return false;
        int offsetX = 0;
        for (int i = 0; i < drawable.size(); i++) {
            int mouseOffsetX = mouseX - this.offsetX - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;
            IFieldProvider provider = drawable.get(i);
            if (MCClientHelper.isInEditMode()) {
                //Delete Button
                if (mouseOffsetX >= 87 && mouseOffsetX <= 97 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    CollectionHelper.removeAndUpdate(drawable, provider);
                    return true;
                }
            }

            if (clickSpecial((T)provider, mouseOffsetX, mouseOffsetY)) return true;
            if (drawingMouseClicked(provider, mouseOffsetX, mouseOffsetY, button)) return true;
            offsetX += provider.getWidth(getMode());
        }

        //Now that we've tried all, let's try the new button
        if (MCClientHelper.isInEditMode()) {
            int mouseOffsetX = mouseX - this.offsetX - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;
            if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
                newDrawable.init(offset.getGui());
                newDrawable.setVisibility(true);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isOverlay() {
        return false;
    }
}
