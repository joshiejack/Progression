package joshie.progression.gui.editors;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFieldProvider;
import joshie.progression.api.criteria.IReward;
import joshie.progression.api.gui.ICustomDrawGuiDisplay;
import joshie.progression.api.gui.ICustomDrawGuiEditor;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IEnum;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.core.*;
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
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static joshie.progression.api.special.DisplayMode.DISPLAY;
import static joshie.progression.api.special.DisplayMode.EDIT;
import static joshie.progression.gui.editors.FeatureItemSelector.Position.BOTTOM;
import static joshie.progression.gui.editors.FeatureItemSelector.Position.TOP;

public class FeatureDrawable<T extends IFieldProvider> extends FeatureAbstract {
    protected static final Theme theme = Theme.INSTANCE;
    private int color;
    private List<IFieldProvider> drawable;
    private HashMap<IFieldProvider, List<IField>> fieldsMap;
    private IGuiFeature newDrawable;
    private int gradient1, gradient2, fontColor;
    private int offsetY;
    private String text;

    protected DisplayMode mode;

    public FeatureDrawable(String text, List drawable, int offsetY, IGuiFeature newDrawable, int gradient1, int gradient2, int fontColor, int color) {
        this.text = EnumChatFormatting.BOLD + Progression.translate("new." + text);
        this.drawable = drawable;
        this.offsetY = offsetY;
        this.newDrawable = newDrawable;
        this.gradient1 = gradient1;
        this.gradient2 = gradient2;
        this.fontColor = fontColor;
        this.fieldsMap = new HashMap();
        this.mode = MCClientHelper.isInEditMode() ? EDIT: DISPLAY;
        this.color = color;
    }

    private int ticker = 0;

    private List<IField> getFields(IFieldProvider provider) {
        if (fieldsMap.containsKey(provider)) return fieldsMap.get(provider);
        else {
            List<IField> fields = new ArrayList();
            if (mode == EDIT) addFieldsViaReflection(provider, fields);
            boolean hideitemfields = false;
            if (provider instanceof ISpecialFieldProvider) {
                ISpecialFieldProvider special = (ISpecialFieldProvider) provider;
                special.addSpecialFields(fields, mode);
            }

            fieldsMap.put(provider, fields);
            return fields;
        }
    }

    private void addFieldsViaReflection(IFieldProvider provider, List<IField> fields) {
        FeatureItemSelector.Position type = provider instanceof IReward ? TOP : BOTTOM;
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

        int width = drawing.getWidth(mode) - 1;
        helper.drawGradient(renderX, renderY, 1, 2, width, 15, drawing.getColor(), gradient1, gradient2);
        helper.drawText(renderX, renderY, drawing.getLocalisedName(), 6, 6, fontColor);

        ICustomDrawGuiEditor editor = drawing instanceof ICustomDrawGuiEditor ? ((ICustomDrawGuiEditor) drawing) : null;
        if (editor == null || (editor != null && !editor.hideDefaultEditor())) {
            int yStart = 18;
            int index = 0;
            for (IField t : getFields(drawing)) {
                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = yStart + (index * 6);
                if (mode == EDIT) {
                    if (mouseX >= 1 && mouseX <= drawing.getWidth(mode) - 16) {
                        if (mouseY >= yPos && mouseY < yPos + 6) {
                            if (mode == EDIT) color = Theme.INSTANCE.optionsFontColorHover;
                            List<String> tooltip = new ArrayList();
                            String untranslated = drawing.getUnlocalisedName() + "." + t.getFieldName();
                            String translated = Progression.translate(untranslated);
                            if (!("progression." + untranslated).equals(translated) || t.getFieldName().equals("isVisible") || t.getFieldName().equals("mustClaim") || t.getFieldName().equals("inverted")) {
                                if (t.getFieldName().equals("isVisible")) translated = Progression.translate("isVisible");
                                if (t.getFieldName().equals("mustClaim")) translated = Progression.translate("mustClaim");
                                if (t.getFieldName().equals("inverted")) translated = Progression.translate("inverted");
                                FeatureTooltip.INSTANCE.addTooltip(WordUtils.wrap(StringEscapeUtils.unescapeJava(translated), 42).replace("\r", "").split("\n"));
                            }
                        }
                    }
                }

                t.draw(helper, renderX, renderY, color, yPos, mouseX, mouseY);
                index++;
            }

            if (editor != null) editor.drawEditor(offset, renderX, renderY, mouseX, mouseY);
            if (mode == DISPLAY) {
                ICustomDrawGuiDisplay display = drawing instanceof ICustomDrawGuiDisplay ? ((ICustomDrawGuiDisplay) drawing) : null;
                if (display == null) {
                    helper.drawSplitText(renderX, renderY, drawing.getDescription(), 4, 20, drawing.getWidth(mode) + drawing.getWidth(mode) / 4, fontColor, 0.75F);
                    //Draw Shit
                } else display.drawDisplay(offset, renderX, renderY, mouseX, mouseY);
            }
        }
    }

    public int drawSpecial(T drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return offsetX + drawing.getWidth(mode);
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int offsetX = 0;
        for (IFieldProvider drawing: drawable) {
            int mouseOffsetX = mouseX - this.offsetX - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;
            if ((drawing.isVisible() && mode == DISPLAY) || mode == EDIT) {
                drawingDraw(drawing, offset, offsetX, this.offsetY, mouseOffsetX, mouseOffsetY);
                //Draw The Delete Button
                if (mode == EDIT) {
                    int xXcoord = 234;
                    if (mouseOffsetX >= drawing.getWidth(mode) - 13 && mouseOffsetX <= drawing.getWidth(mode) - 3 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                        xXcoord += 11;
                    }

                    offset.drawTexture(offsetX, offsetY, ProgressionInfo.textures, drawing.getWidth(mode) - 13, 4, xXcoord, 52, 11, 11);
                }
            }

            offsetX = drawSpecial((T)drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
        }

        //Draw the addition texture
        if (mode == EDIT) {
            int mouseOffsetX = mouseX - this.offsetX - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;

            int crossX = 201;
            int crossY = 64;
            if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
                FeatureTooltip.INSTANCE.addTooltip(text);
                crossY = 119;
            }

            GlStateManager.enableBlend();
            float red = (color >> 16 & 255) / 255.0F;
            float green = (color >> 8 & 255) / 255.0F;
            float blue = (color & 255) / 255.0F;
            //GlStateManager.resetColor();
            //GlStateManager.color(red, green, blue, 1F);
            GL11.glColor4f(red, green, blue, 1F);
            offset.drawTexture(offsetX, offsetY, ProgressionInfo.textures, 15, 10, crossX, crossY, 55, 55);
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
    }

    private boolean drawingMouseClicked(IFieldProvider provider, int mouseX, int mouseY, int button) {
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
                if (mouseX >= 1 && mouseX <= provider.getWidth(mode) - 1) {
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

        if (1 == 1) return false;

        //Update the item preview when selecting toggling something
        FeatureItemPreview.INSTANCE.updateSearch();
        if (editor != null && editor.mouseClicked(mouseX, mouseY)) return true;


        return false;
    }

    public boolean clickSpecial(T provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }

    @Override //Only called in Edit Mode
    public boolean mouseClicked(final int mouseX, final int mouseY, int button) {
        if (FeatureItemSelector.INSTANCE.isVisible()) return false; //If the item selector is visible, don't process clicks
        if (FeatureNew.IS_OPEN) return false;
        int offsetX = 0;
        for (IFieldProvider provider: drawable) {
            if (!provider.isVisible() && mode == DISPLAY) continue;
            int mouseOffsetX = mouseX - this.offsetX - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;
            //Delete Button
            if (mouseOffsetX >= provider.getWidth(mode) - 13 && mouseOffsetX <= provider.getWidth(mode) - 3 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                CollectionHelper.removeAndUpdate(drawable, provider);
                return true;
            }

            if (clickSpecial((T)provider, mouseOffsetX, mouseOffsetY)) return true;
            if (drawingMouseClicked(provider, mouseOffsetX, mouseOffsetY, button)) return true;
            offsetX += provider.getWidth(mode);
        }

        //Now that we've tried all, let's try the new button
        int mouseOffsetX = mouseX - this.offsetX - offsetX;
        int mouseOffsetY = mouseY - this.offsetY;
        if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
            newDrawable.init(offset.getGui());
            newDrawable.setVisibility(true);
            return true;
        }

        return false;
    }

    @Override
    public boolean isOverlay() {
        return false;
    }
}
