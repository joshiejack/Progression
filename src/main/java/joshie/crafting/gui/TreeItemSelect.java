package joshie.crafting.gui;

import joshie.crafting.helpers.StackHelper;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

public class TreeItemSelect extends TextEditable {
    public static TreeItemSelect INSTANCE = new TreeItemSelect();

    public boolean mouseClicked(int mouseX, int mouseY) {
        if (SelectItemOverlay.INSTANCE.sorted == null) {
            SelectItemOverlay.INSTANCE.updateSearch();
        }

        ScaledResolution res = GuiTreeEditor.INSTANCE.res;
        int fullWidth = res.getScaledWidth() - 10;
        int width = (int) ((double) fullWidth / 18.633333334D);
        int j = 0;
        int k = 0;
        for (int i = SelectItemOverlay.INSTANCE.position; i < SelectItemOverlay.INSTANCE.position + (width * 10); i++) {
            if (i >= 0 && i < SelectItemOverlay.INSTANCE.sorted.size()) {
                if (mouseX >= 32 + (j * 16) && mouseX <= 32 + (j * 16) + 16) {
                    if (mouseY >= 45 + (k * 16) && mouseY <= 45 + (k * 16) + 16) {
                        SelectItemOverlay.INSTANCE.selectable.setItemStack(SelectItemOverlay.INSTANCE.sorted.get(i).copy());
                        return true;
                    }
                }

                j++;

                if (j > width) {
                    j = 0;
                    k++;
                }
            }
        }

        return false;
    }

    public void draw() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        if (SelectItemOverlay.INSTANCE.selectable != null) {
            if (SelectItemOverlay.INSTANCE.sorted == null) {
                SelectItemOverlay.INSTANCE.updateSearch();
            }
            
            int y = GuiTreeEditor.INSTANCE.y;

            int offsetX = GuiCriteriaEditor.INSTANCE.offsetX;
            ScaledResolution res = GuiTreeEditor.INSTANCE.res;
            int fullWidth = res.getScaledWidth() - 10;

            GuiTreeEditor.INSTANCE.drawGradientRectWithBorder(30, y + 20, res.getScaledWidth() - 30, y + 40, 0xFF222222, 0xFF000000, 0xFF000000);
            GuiTreeEditor.INSTANCE.drawRectWithBorder(30, y + 40, res.getScaledWidth() - 30, y + 210, 0xFF000000, 0xFFFFFFFF);

            GuiTreeEditor.INSTANCE.mc.fontRenderer.drawString("Select Item - Click elsewhere to close", 35 - offsetX, y + 27, 0xFFFFFFFF);
            GuiTreeEditor.INSTANCE.drawRectWithBorder(res.getScaledWidth() - 180, y + 23, res.getScaledWidth() - 35, y + 38, 0xFF000000, 0xFFFFFFFF);
            GuiTreeEditor.INSTANCE.mc.fontRenderer.drawString(SelectItemOverlay.INSTANCE.getText(), res.getScaledWidth() - 175, y + 29, 0xFFFFFFFF);

            int width = (int) ((double) fullWidth / 18.633333334D);
            int j = 0;
            int k = 0;

            //Switch 8 > 32 (-offsetX + 32)
            //Switch 16.6333333 to 18
            //width * 4 to width *10
            for (int i = SelectItemOverlay.INSTANCE.position; i < SelectItemOverlay.INSTANCE.position + (width * 10); i++) {
                if (i >= 0 && i < SelectItemOverlay.INSTANCE.sorted.size()) {
                    StackHelper.drawStack(SelectItemOverlay.INSTANCE.sorted.get(i), -offsetX + 32 + (j * 16), y + 45 + (k * 16), 1F);

                    j++;

                    if (j > width) {
                        j = 0;
                        k++;
                    }
                }
            }
        }
    }

    @Override
    public String getTextField() {
        return SelectItemOverlay.INSTANCE.getTextField();
    }

    @Override
    public void setTextField(String str) {
        SelectItemOverlay.INSTANCE.setTextField(str);
    }
}