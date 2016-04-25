package joshie.progression.gui.editors;

import joshie.progression.Progression;
import joshie.progression.api.criteria.*;
import joshie.progression.api.gui.ICustomDrawGuiDisplay;
import joshie.progression.api.gui.ICustomDrawGuiEditor;
import joshie.progression.api.gui.IDrawHelper;
import joshie.progression.api.gui.Position;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IEnum;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.core.GuiList;
import joshie.progression.gui.core.IGuiFeature;
import joshie.progression.gui.editors.insert.FeatureNew;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.EnumField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.lib.PInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import static joshie.progression.api.gui.Position.BOTTOM;
import static joshie.progression.api.gui.Position.TOP;
import static joshie.progression.api.special.DisplayMode.DISPLAY;
import static joshie.progression.api.special.DisplayMode.EDIT;
import static joshie.progression.gui.core.GuiList.*;

public abstract class FeatureDrawable<T extends IRuleProvider> extends FeatureAbstract {
    private EnumMap<DisplayMode, HashMap<IRule, List<IField>>> displayMap;
    private IGuiFeature newDrawable;
    private int gradient1, gradient2, fontColor, color;
    private int offsetY;
    private String text;

    public FeatureDrawable(String text, int offsetY, IGuiFeature newDrawable, int gradient1, int gradient2, int fontColor, int color) {
        this.text = EnumChatFormatting.BOLD + Progression.translate("new." + text);
        this.offsetY = offsetY;
        this.newDrawable = newDrawable;
        this.gradient1 = gradient1;
        this.gradient2 = gradient2;
        this.fontColor = fontColor;
        this.color = color;
        this.displayMap = new EnumMap<DisplayMode, HashMap<IRule, List<IField>>>(DisplayMode.class);
        this.displayMap.put(DISPLAY, new HashMap<IRule, List<IField>>());
        this.displayMap.put(EDIT, new HashMap<IRule, List<IField>>());
    }

    protected abstract List<T> getList();
    public abstract boolean isReady();

    private HashMap<IRule, List<IField>> getFieldsMap() {
        return displayMap.get(MODE);
    }

    private List<IField> getFields(IRuleProvider provider) {
        HashMap<IRule, List<IField>> fieldsMap = getFieldsMap();
        if (fieldsMap.containsKey(provider.getProvided())) return fieldsMap.get(provider.getProvided());
        else {
            List<IField> fields = new ArrayList();
            if (MODE == EDIT) {
                addFieldsViaReflection(provider, fields);
                addFieldsViaReflection(provider.getProvided(), fields);
            }

            if (provider.getProvided() instanceof ISpecialFieldProvider) {
                ((ISpecialFieldProvider) provider.getProvided()).addSpecialFields(fields, MODE);
            }

            fieldsMap.put(provider.getProvided(), fields);
            return fields;
        }
    }

    private void addFieldsViaReflection(Object provider, List<IField> fields) {
        Position type = provider instanceof IRewardProvider ? TOP : BOTTOM;
        for (Field field : provider.getClass().getFields()) {
            try {
                if (field.getType() == boolean.class) {
                    if (provider instanceof ITriggerProvider) {
                        if (field.getName().equals("isCanceling") && !((ITriggerProvider)provider).isCancelable()) continue;
                    }

                    fields.add(new BooleanField(field.getName(), provider));
                }

                else if (field.getType() == String.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.getType() == int.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.getType() == float.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.getType() == double.class) fields.add(new TextField(field.getName(), provider, type));
                else if (field.get(provider) instanceof Enum) fields.add(new EnumField(field.getName(), (IEnum) provider));
            } catch (Exception e) {}
        }
    }

    protected void drawingDraw(IRuleProvider drawing, IDrawHelper helper, int renderX, int renderY, int mouseX, int mouseY) {
        int width = drawing.getWidth(MODE) - 1;
        helper.drawGradient(renderX, renderY, 1, 2, width, 15, drawing.getColor(), gradient1, gradient2);
        helper.drawText(renderX, renderY, drawing.getLocalisedName(), 6, 6, fontColor);

        ICustomDrawGuiEditor editor = drawing instanceof ICustomDrawGuiEditor ? ((ICustomDrawGuiEditor) drawing) : null;
        if (editor == null || (editor != null && !editor.hideDefaultEditor())) {
            int yStart = 18;
            int index = 0;
            for (IField t : getFields(drawing)) {
                int color = THEME.optionsFontColor;
                int yPos = yStart + (index * 6);
                if (MODE == EDIT && !FeatureNew.IS_OPEN) {
                    if (mouseX >= 1 && mouseX <= drawing.getWidth(MODE) - 16) {
                        if (mouseY >= yPos && mouseY < yPos + 6) {
                            color = THEME.optionsFontColorHover;
                            String untranslated = drawing.getUnlocalisedName() + "." + t.getFieldName();
                            String translated = Progression.translate(untranslated);
                            if (!("progression." + untranslated).equals(translated) || t.getFieldName().equals("isVisible") || t.getFieldName().equals("mustClaim") || t.getFieldName().equals("inverted")) {
                                if (t.getFieldName().equals("isVisible"))
                                    translated = Progression.translate("isVisible");
                                if (t.getFieldName().equals("mustClaim"))
                                    translated = Progression.translate("mustClaim");
                                if (t.getFieldName().equals("inverted")) translated = Progression.translate("inverted");
                                TOOLTIP.add(WordUtils.wrap(StringEscapeUtils.unescapeJava(translated).replace("\r", ""), 42).split("\n"));
                            }
                        }
                    }
                }

                t.draw(drawing, helper, renderX, renderY, color, yPos, mouseX, mouseY);

                index++;
            }

            if (editor != null) editor.drawEditor(offset, renderX, renderY, mouseX, mouseY);
            if (MODE == DISPLAY) {
                ICustomDrawGuiDisplay display = drawing instanceof ICustomDrawGuiDisplay ? ((ICustomDrawGuiDisplay) drawing) : null;
                if (display == null) {
                    helper.drawSplitText(renderX, renderY, drawing.getDescription(), 4, 20, drawing.getWidth(MODE) + drawing.getWidth(MODE) / 4, fontColor, 0.75F);
                    //Draw Shit
                } else display.drawDisplay(offset, renderX, renderY, mouseX, mouseY);
            }
        }
    }

    public int drawSpecial(T drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return offsetX + drawing.getWidth(MODE);
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        if (!isReady()) {
            CORE.mc.thePlayer.closeScreen(); //Close this gui if stuff is in sync
            return;
        }

        int offsetX = 0;
        for (IRuleProvider drawing: getList()) {
            int mouseOffsetX = mouseX - CORE.getOffsetX() - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;

            if ((drawing.isVisible() && MODE == DISPLAY) || MODE == EDIT) {
                drawingDraw(drawing, offset, offsetX, this.offsetY, mouseOffsetX, mouseOffsetY);
                //Draw The Delete Button
                if (MODE == EDIT) {
                    int xXcoord = 234;
                    if (mouseOffsetX >= drawing.getWidth(MODE) - 13 && mouseOffsetX <= drawing.getWidth(MODE) - 3 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                        xXcoord += 11;
                    }

                    offset.drawTexture(offsetX, offsetY, PInfo.textures, drawing.getWidth(MODE) - 13, 4, xXcoord, 52, 11, 11);
                }
            }

            offsetX = drawSpecial((T)drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
        }

        //Draw the addition texture
        if (MODE == EDIT) {
            int mouseOffsetX = mouseX - CORE.getOffsetX() - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;

            int crossX = 201;
            int crossY = 64;
            if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65 && !FeatureNew.IS_OPEN) {
                TOOLTIP.add(text);
                crossY = 119;
            }

            GlStateManager.enableBlend();
            float red = (color >> 16 & 255) / 255.0F;
            float green = (color >> 8 & 255) / 255.0F;
            float blue = (color & 255) / 255.0F;
            //GlStateManager.resetColor();
            //GlStateManager.color(red, green, blue, 1F);
            GL11.glColor4f(red, green, blue, 1F);
            offset.drawTexture(offsetX, offsetY, PInfo.textures, 15, 10, crossX, crossY, 55, 55);
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
    }

    private boolean drawingMouseClicked(IRuleProvider provider, int mouseX, int mouseY, int button) {
        ICustomDrawGuiEditor editor = provider instanceof ICustomDrawGuiEditor ? ((ICustomDrawGuiEditor) provider) : null;
        if (editor == null || (editor != null && !editor.hideDefaultEditor())) {
            int index = 0;
            for (IField t : getFields(provider)) {
                if (t.attemptClick(mouseX, mouseY)) {
                    return true;
                }

                if (MODE == EDIT) {
                    int yPos = 18 + (index * 6);
                    if (mouseX >= 1 && mouseX <= provider.getWidth(MODE) - 1) {
                        if (mouseY >= yPos && mouseY < yPos + 6) {
                            t.click(button);

                            //Update the item preview when selecting toggling something
                            PREVIEW.updateSearch();
                            return true;
                        }
                    }
                }

                index++;
            }
        }

        if (MODE == DISPLAY) return false;
        //Update the item preview when selecting toggling something
        PREVIEW.updateSearch();
        if (editor != null && editor.mouseClicked(mouseX, mouseY)) return true;
        return false;
    }

    public boolean clickSpecial(T provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }

    @Override //Only called in Edit Mode
    public boolean mouseClicked(final int mouseX, final int mouseY, int button) {
        if (!isReady()) return false;
        if (GuiList.ITEM_EDITOR.isVisible()) return false; //If the item selector is visible, don't process clicks
        if (FeatureNew.IS_OPEN) return false;
        int offsetX = 0;
        for (IRuleProvider provider: getList()) {
            if (!provider.isVisible() && MODE == DISPLAY) continue;
            int mouseOffsetX = mouseX - CORE.getOffsetX() - offsetX;
            int mouseOffsetY = mouseY - this.offsetY;

            if (clickSpecial((T) provider, mouseOffsetX, mouseOffsetY)) return true;

            //Delete Button
            if(MODE == EDIT) {
                if (mouseOffsetX >= provider.getWidth(MODE) - 13 && mouseOffsetX <= provider.getWidth(MODE) - 3 && mouseOffsetY >= 4 && mouseOffsetY <= 14) {
                    return CollectionHelper.removeAndUpdate(getList(), provider);
                }
            }

            if (drawingMouseClicked(provider, mouseOffsetX, mouseOffsetY, button)) return true;
            offsetX += provider.getWidth(MODE);
        }

        //If we're in display return
        if (MODE == DISPLAY) return false;
        //Now that we've tried all, let's try the new button
        int mouseOffsetX = mouseX - CORE.getOffsetX() - offsetX;
        int mouseOffsetY = mouseY - this.offsetY;
        if (mouseOffsetX >= 15 && mouseOffsetX <= 70 && mouseOffsetY >= 10 && mouseOffsetY <= 65) {
            newDrawable.init();
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
