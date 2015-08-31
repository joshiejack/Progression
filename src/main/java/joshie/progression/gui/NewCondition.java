package joshie.progression.gui;

import joshie.progression.api.IConditionType;
import joshie.progression.criteria.Trigger;
import joshie.progression.gui.base.OverlayBase;
import joshie.progression.handlers.APIHandler;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class NewCondition extends OverlayBase {
    public static NewCondition INSTANCE = new NewCondition();
    private static Trigger trigger = null;

    public void select(Trigger trigger) {
        if (reset()) {
            NewCondition.trigger = trigger;
        }
    }
    
    @Override
    public void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawBox(x, y, width, height, color, border);
    }
    
    public void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawGradient(x, y, width, height, color, color2, border);
    }
    
    public void drawText(String text, int x, int y, int color) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawText(text, x, y, color);
    }
    
    public void drawStack(ItemStack stack, int x, int y, float size) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawStack(stack, x, y, size);
    }

    @Override
    public void clear() {
        trigger = null;
    }

    @Override
    public boolean isVisible() {
        return trigger != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int yPos = 0;
        int xPos = 0;
        for (IConditionType condition : APIHandler.conditionTypes.values()) {
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    APIHandler.cloneCondition(trigger, condition);
                    clear();
                    return true;
                }
            }

            yPos++;

            if (yPos > 6) {
                xPos++;
                yPos = 0;
            }
        }

        return false;
    }

    @Override
    public void draw(int x, int y) {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        int mouseX = GuiTriggerEditor.INSTANCE.mouseX;
        int mouseY = GuiTriggerEditor.INSTANCE.mouseY;
        drawBox(-GuiTriggerEditor.INSTANCE.offsetX + 150, 30, 200, 100, theme.newBox1, theme.newBox2);
        drawGradient(-GuiTriggerEditor.INSTANCE.offsetX + 150, 30, 200, 15, theme.newConditionGradient1, theme.newConditionGradient2, theme.newConditionBorder);
        drawText("Select a Type of Condition", -GuiTriggerEditor.INSTANCE.offsetX + 155, 34, theme.newConditionFont);
        int yPos = 0;
        int xPos = 0;
        for (IConditionType condition : APIHandler.conditionTypes.values()) {
            int color = theme.newConditionFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newConditionFontHover;
                }
            }

            drawText(condition.getLocalisedName(), -GuiTriggerEditor.INSTANCE.offsetX + (xPos * 100) + 155, 46 + (yPos * 12), color);

            yPos++;

            if (yPos > 6) {
                xPos++;
                yPos = 0;
            }
        }
    }
}
