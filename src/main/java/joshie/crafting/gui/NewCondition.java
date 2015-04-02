package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IConditionType;
import joshie.crafting.api.ITrigger;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class NewCondition extends OverlayBase {
    public static NewCondition INSTANCE = new NewCondition();
    private static ITrigger trigger = null;

    public void select(ITrigger trigger) {
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
        for (IConditionType condition : CraftAPIRegistry.conditionTypes.values()) {
            int color = 0xFF000000;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY <= 46 + (yPos * 12) + 12) {
                    CraftingAPI.registry.cloneCondition(trigger, condition);
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
        drawBox(-GuiTriggerEditor.INSTANCE.offsetX + 150, 30, 200, 100, 0xDD000000, 0xFF000000);
        drawGradient(-GuiTriggerEditor.INSTANCE.offsetX + 150, 30, 200, 15, 0xFFFF8000, 0xFF8C4600, 0xFF000000);
        drawText("Select a Type of Condition", -GuiTriggerEditor.INSTANCE.offsetX + 155, 34, 0xFFFFFFFF);
        int yPos = 0;
        int xPos = 0;
        for (IConditionType condition : CraftAPIRegistry.conditionTypes.values()) {
            int color = 0xFFFFFFFF;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY <= 46 + (yPos * 12) + 12) {
                    color = 0xFF2693FF;
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
