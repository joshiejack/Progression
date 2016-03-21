package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import joshie.progression.Progression;
import joshie.progression.api.ICriteria;
import joshie.progression.api.ITriggerType;
import joshie.progression.api.fields.IFieldProvider;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.gui.newversion.GuiCore;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import net.minecraft.client.renderer.GlStateManager;

public abstract class FeatureNew<T> extends FeatureAbstract {
    public static boolean IS_OPEN = false;
    protected ICriteria criteria;
    protected ITriggerType trigger;
    protected ItemFilterField field;
    private final List<IFieldProvider> sorted;

    public String text;

    public FeatureNew(String text) {
        this.text = text;
        this.sorted = new ArrayList(getFields());
        Collections.sort(this.sorted, new Alphabetical());
    }

    private static class Alphabetical implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            IFieldProvider provider1 = ((IFieldProvider) o1);
            IFieldProvider provider2 = ((IFieldProvider) o2);
            return provider1.getLocalisedName().compareTo(provider2.getLocalisedName());
        }
    }

    public abstract Collection<T> getFields();

    public abstract void clone(T t);
    
    public boolean isValid(T t) {
        return true;
    }

    @Override
    public FeatureAbstract init(GuiCore core) {
        super.init(core);
        setVisibility(false);
        criteria = GuiCriteriaEditor.INSTANCE.getCriteria();
        trigger = GuiConditionEditor.INSTANCE.getTrigger();
        field = GuiItemFilterEditor.INSTANCE.getField();
        return this;
    }

    @Override
    public boolean isOverlay() {
        return true;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        offset.drawRectangle(150, 30, 200, 150, theme.newBox1, theme.newBox2);
        offset.drawGradient(150, 30, 200, 15, theme.newGradient1, theme.newGradient2, theme.newBorder);
        offset.drawText(Progression.translate("new." + text), 155, 34, theme.newFont);
        drawForeground(mouseX, mouseY);
    }

    public void drawForeground(int mouseX, int mouseY) {
        int yPos = 0;
        int xPos = 0;
        for (IFieldProvider provider : sorted) {
            if (!isValid((T) provider)) continue;
            int color = theme.newFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newFontHover;
                }
            }

            offset.drawText(provider.getLocalisedName(), (xPos * 100) + 155, 46 + (yPos * 12), color);

            xPos++;

            if (xPos > 1) {
                yPos++;
                xPos = 0;
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int yPos = 0;
        int xPos = 0;
        for (IFieldProvider provider : sorted) {
            if (!isValid((T) provider)) continue;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    IS_OPEN = false;
                    clone((T) provider);
                    setVisibility(false);
                    return true;
                }
            }

            xPos++;

            if (xPos > 1) {
                yPos++;
                xPos = 0;
            }
        }

        return false;
    }

    @Override
    public void setVisibility(boolean value) {
        super.setVisibility(value);
        IS_OPEN = value;
    }
}
