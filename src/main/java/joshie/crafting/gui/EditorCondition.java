package joshie.crafting.gui;

import java.util.Iterator;
import java.util.List;

import joshie.crafting.Condition;
import joshie.crafting.Trigger;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.RenderItemHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.lib.CraftingInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class EditorCondition {
    private final Trigger trigger;
    private int xCoord;
    private int yCoord;

    public EditorCondition(Trigger criteria) {
        this.trigger = criteria;
    }

    public void drawSplitText(String text, int x, int y, int color, int width) {
        GuiTreeEditor.INSTANCE.mc.fontRenderer.drawSplitString(text, xCoord + x, yCoord + y, width, color);
    }

    public void drawText(String text, int x, int y, int color) {
        GuiTreeEditor.INSTANCE.mc.fontRenderer.drawString(text, xCoord + x, yCoord + y, color);
    }

    public void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiTreeEditor.INSTANCE.drawRectWithBorder(xCoord + x, yCoord + y, xCoord + x + width, yCoord + y + height, color, border);
    }

    public void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
        GuiTreeEditor.INSTANCE.drawGradientRectWithBorder(xCoord + x, yCoord + y, xCoord + x + width, yCoord + y + height, color, color2, border);
    }

    public void drawStack(ItemStack stack, int x, int y, float scale) {
    	RenderItemHelper.drawStack(stack, xCoord + x, yCoord + y, scale);
    }

    public void drawTexture(int x, int y, int u, int v, int width, int height) {
        GuiTreeEditor.INSTANCE.drawTexturedModalRect(xCoord + x, yCoord + y, u, v, width, height);
    }

    private int offsetX = 0;

    public void draw(int x, int y, int offsetX) {
        this.offsetX = offsetX;
        this.xCoord = x + offsetX;
        this.yCoord = y;

        ScaledResolution res = GuiTriggerEditor.INSTANCE.res;
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        //Title and Repeatability Box
        drawText("Editing Trigger conditions for the Criteria: " + trigger.getCriteria().displayName + " - " + trigger.getType().getLocalisedName(), 9 - offsetX, 9, Theme.INSTANCE.conditionEditorFont);
        drawBox(-1, 210, fullWidth, 1, Theme.INSTANCE.conditionEditorUnderline2, Theme.INSTANCE.conditionEditorUnderline2);
        drawText("Use arrow keys to scroll sideways, or use the scroll wheel. (Down to go right)", 9 - offsetX, 215, Theme.INSTANCE.scrollTextFontColor);
        drawText("Hold shift with arrow keys to scroll faster.", 9 - offsetX, 225, Theme.INSTANCE.scrollTextFontColor);

        //Conditions
        drawGradient(-1, 25, fullWidth, 15, Theme.INSTANCE.conditionEditorGradient1, Theme.INSTANCE.conditionEditorGradient2, Theme.INSTANCE.conditionEditorBorder);
        drawBox(-1, 40, fullWidth, 1, Theme.INSTANCE.conditionEditorUnderline, Theme.INSTANCE.invisible);
        drawText("Conditions", 9 - offsetX, 29, Theme.INSTANCE.conditionEditorFont);
        int xCoord = 0;
        List<Condition> conditions = trigger.getConditions();
        int mouseX = GuiTriggerEditor.INSTANCE.mouseX - offsetX;
        int mouseY = GuiTriggerEditor.INSTANCE.mouseY;
        for (int i = 0; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            int xPos = 100 * xCoord;
            condition.draw(mouseX, mouseY, xPos);
            xCoord++;
        }

        if (ClientHelper.canEdit()) {
            int crossX = 55;
            if (!NewCondition.INSTANCE.isVisible()) {
                if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                    if (mouseY >= 49 && mouseY <= 49 + 55) {
                        crossX = 165;
                    }
                }
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            ClientHelper.getMinecraft().getTextureManager().bindTexture(CraftingInfo.textures);
            drawTexture(15 + 100 * xCoord, 49, crossX, 180, 55, 55);
        }
    }

    private void remove(List list, Object object) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o.equals(object)) {
                it.remove();
                break;
            }
        }
    }

    public boolean click(int mouseX, int mouseY, boolean isDoubleClick) {
        if (!ClientHelper.canEdit()) {
            return false;
        }

        boolean hasClicked = false;
        //Name and repeat
        ScaledResolution res = GuiTreeEditor.INSTANCE.res;
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;

        //Triggers
        xCoord = 0;
        List<Condition> conditions = trigger.getConditions();
        for (int i = 0; i < conditions.size(); i++) {
            Result result = conditions.get(i).onClicked();
            if (result != Result.DEFAULT) {
                hasClicked = true;
            }

            if (result == Result.DENY) {
                remove(conditions, conditions.get(i));
                break;
            }

            xCoord++;
        }

        mouseX = GuiTriggerEditor.INSTANCE.mouseX - offsetX;
        mouseY = GuiTriggerEditor.INSTANCE.mouseY;
        if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
            if (mouseY >= 49 && mouseY <= 49 + 55) {
                NewCondition.INSTANCE.select(trigger);
                hasClicked = true;
            }
        }

        return hasClicked;
    }
}
