package joshie.progression.gui.editors;

import org.lwjgl.opengl.GL11;

import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.helpers.RenderItemHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class FeatureItemSelectorTree extends FeatureAbstract implements ITextEditable {
    public static FeatureItemSelectorTree INSTANCE = new FeatureItemSelectorTree();

    @Override
    public boolean isOverlay() {
        return true;
    }
    
    @Override
    public boolean isVisible() {
        return FeatureItemSelector.INSTANCE.getEditable() != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (FeatureItemSelector.INSTANCE.sorted == null) {
            FeatureItemSelector.INSTANCE.updateSearch();
        }

        int width = (int) ((double) (screenWidth - 75) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = FeatureItemSelector.INSTANCE.index; i < FeatureItemSelector.INSTANCE.index + (width * 10) + 10; i++) {
            if (i >= 0 && i < FeatureItemSelector.INSTANCE.sorted.size()) {
                if (mouseX >= 32 + (j * 16) && mouseX <= 32 + (j * 16) + 16) {
                    if (mouseY >= 45 + (k * 16) && mouseY <= 45 + (k * 16) + 16) {
                        FeatureItemSelector.INSTANCE.selectable.setObject(((ItemStack) FeatureItemSelector.INSTANCE.sorted.get(i)).copy());
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

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        if (FeatureItemSelector.INSTANCE.selectable != null) {
            if (FeatureItemSelector.INSTANCE.sorted == null) {
                FeatureItemSelector.INSTANCE.updateSearch();
            }

            int offsetX = GuiCore.INSTANCE.getOffsetX();
            ScaledResolution res = GuiTreeEditor.INSTANCE.res;
            int fullWidth = res.getScaledWidth() - 10;

            GuiCore.INSTANCE.drawGradientRectWithBorder(30, 20, res.getScaledWidth() - 30, 40, theme.blackBarGradient1, theme.blackBarGradient2, theme.blackBarBorder);
            GuiCore.INSTANCE.drawRectWithBorder(30, 40, res.getScaledWidth() - 30, 210, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);

            GuiTreeEditor.INSTANCE.mc.fontRendererObj.drawString("Select Item - Click elsewhere to close", 35 - offsetX, GuiCore.INSTANCE.screenTop + 27, theme.blackBarFontColor);
            GuiCore.INSTANCE.drawRectWithBorder(res.getScaledWidth() - 180, 23, res.getScaledWidth() - 35, 38, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);

            String text = TextEditor.INSTANCE.getText(this);
            if (text.equals("")) text = "Type to search";
            GuiTreeEditor.INSTANCE.mc.fontRendererObj.drawString(text + "...", res.getScaledWidth() - 175, GuiCore.INSTANCE.screenTop + 28, theme.blackBarFontColor);

            int width = (int) ((double) (screenWidth - 75) / 16.133333334D);
            //Switch 8 > 32 (-offsetX + 32)
            //Switch 16.6333333 to 18
            //width * 4 to width *10
            int j = 0;
            int k = 0;
            for (int i = FeatureItemSelector.INSTANCE.index; i < FeatureItemSelector.INSTANCE.index + (width * 10) + 10; i++) {
                if (i >= 0 && i < FeatureItemSelector.INSTANCE.sorted.size()) {
                    RenderItemHelper.drawStack((ItemStack) FeatureItemSelector.INSTANCE.sorted.get(i), 32 + (j * 16), GuiCore.INSTANCE.screenTop + 45 + (k * 16), 1F);

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
        return FeatureItemSelector.INSTANCE.getTextField();
    }

    @Override
    public void setTextField(String str) {
        FeatureItemSelector.INSTANCE.setTextField(str);
    }
}