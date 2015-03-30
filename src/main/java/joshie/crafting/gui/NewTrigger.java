package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITriggerType;

import org.lwjgl.opengl.GL11;

public class NewTrigger extends OverlayBase {
    public static NewTrigger INSTANCE = new NewTrigger();
    private static ICriteria criteria = null;

    public void select(ICriteria criteria) {
        if (reset()) {
            NewTrigger.criteria = criteria;
        }
    }

    @Override
    public void clear() {
        criteria = null;
    }

    @Override
    public boolean isVisible() {
        return criteria != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int yPos = 0;
        int xPos = 0;
        for (ITriggerType trigger : CraftAPIRegistry.triggerTypes.values()) {
            int color = 0xFF000000;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY <= 46 + (yPos * 12) + 12) {
                    CraftingAPI.registry.cloneTrigger(criteria, trigger);
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
        int mouseX = GuiCriteriaEditor.INSTANCE.mouseX;
        int mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
        drawBox(-GuiCriteriaEditor.INSTANCE.offsetX + 150, 30, 200, 100, 0xFFFFFFFF, 0xFF000000);
        drawBox(-GuiCriteriaEditor.INSTANCE.offsetX + 150, 30, 200, 15, 0xFF00008C, 0xFF000000);
        drawText("Select a Type of Trigger", -GuiCriteriaEditor.INSTANCE.offsetX + 155, 34, 0xFFFFFFFF);
        int yPos = 0;
        int xPos = 0;
        for (ITriggerType trigger : CraftAPIRegistry.triggerTypes.values()) {
            int color = 0xFF000000;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY <= 46 + (yPos * 12) + 12) {
                    color = 0xFFDDDDDD;
                }
            }

            drawText(trigger.getLocalisedName(), -GuiCriteriaEditor.INSTANCE.offsetX + (xPos * 100) + 155, 46 + (yPos * 12), color);

            yPos++;

            if (yPos > 6) {
                xPos++;
                yPos = 0;
            }
        }
    }
}
