package joshie.progression.gui.newversion.overlays;

import org.lwjgl.opengl.GL11;

import joshie.progression.Progression;
import joshie.progression.criteria.Criteria;
import joshie.progression.gui.newversion.GuiCore;
import joshie.progression.gui.newversion.GuiCriteriaEditor;

public abstract class FeatureNew extends FeatureAbstract {
    public static boolean IS_OPEN = false;
    protected Criteria criteria;
    
    public String text;
    
    public FeatureNew(String text) {
        this.text = text;
    }

    @Override
    public FeatureAbstract init(GuiCore core) {
        super.init(core);
        setVisibility(false);
        criteria = GuiCriteriaEditor.INSTANCE.criteria;
        return this;
    }
    
    @Override
    public void drawFeature(int mouseX, int mouseY) {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        offset.drawRectangle(150, 30, 200, 150, theme.newBox1, theme.newBox2);
        offset.drawGradient(150, 30, 200, 15, theme.newTriggerGradient1, theme.newTriggerGradient2, theme.newTriggerBorder);
        offset.drawText(Progression.translate("new." + text), 155, 34, theme.newTriggerFont);
        drawForeground(mouseX, mouseY);
    }

    protected void drawForeground(int mouseX, int mouseY) {}
    
    @Override
    public void setVisibility(boolean value) {
        super.setVisibility(value);
        IS_OPEN = value;
    }
}
