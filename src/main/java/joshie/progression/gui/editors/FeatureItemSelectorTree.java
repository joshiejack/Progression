package joshie.progression.gui.editors;

import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.helpers.RenderItemHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import static joshie.progression.gui.core.GuiList.*;

public class FeatureItemSelectorTree extends FeatureAbstract implements ITextEditable {
    @Override
    public boolean isOverlay() {
        return true;
    }
    
    @Override
    public boolean isVisible() {
        return ITEM_EDITOR.getEditable() != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (ITEM_EDITOR.sorted == null) {
            ITEM_EDITOR.updateSearch();
        }

        int width = (int) ((double) (screenWidth - 75) / 16.133333334D);
        int j = 0;
        int k = 0;
        for (int i = ITEM_EDITOR.index; i < ITEM_EDITOR.index + (width * 10) + 10; i++) {
            if (i >= 0 && i < ITEM_EDITOR.sorted.size()) {
                if (mouseX >= 32 + (j * 16) && mouseX <= 32 + (j * 16) + 16) {
                    if (mouseY >= 45 + (k * 16) && mouseY <= 45 + (k * 16) + 16) {
                        ITEM_EDITOR.selectable.setObject(((ItemStack) ITEM_EDITOR.sorted.get(i)).copy());
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
        if (ITEM_EDITOR.selectable != null) {
            if (ITEM_EDITOR.sorted == null) {
                ITEM_EDITOR.updateSearch();
            }

            int offsetX = CORE.getOffsetX();
            ScaledResolution res = CORE.res;
            int fullWidth = res.getScaledWidth() - 10;

            CORE.drawGradientRectWithBorder(30, 20, res.getScaledWidth() - 30, 40, THEME.blackBarGradient1, THEME.blackBarGradient2, THEME.blackBarBorder);
            CORE.drawRectWithBorder(30, 40, res.getScaledWidth() - 30, 210, THEME.blackBarUnderLine, THEME.blackBarUnderLineBorder);

            CORE.mc.fontRendererObj.drawString("Select Item - Click elsewhere to close", 35 - offsetX, CORE.screenTop + 27, THEME.blackBarFontColor);
            CORE.drawRectWithBorder(res.getScaledWidth() - 180, 23, res.getScaledWidth() - 35, 38, THEME.blackBarUnderLine, THEME.blackBarUnderLineBorder);

            String text = TEXT_EDITOR_SIMPLE.getText(this);
            if (text.equals("")) text = "Type to search";
            CORE.mc.fontRendererObj.drawString(text + "...", res.getScaledWidth() - 175, CORE.screenTop + 28, THEME.blackBarFontColor);

            int width = (int) ((double) (screenWidth - 75) / 16.133333334D);
            //Switch 8 > 32 (-offsetX + 32)
            //Switch 16.6333333 to 18
            //width * 4 to width *10
            int j = 0;
            int k = 0;
            for (int i = ITEM_EDITOR.index; i < ITEM_EDITOR.index + (width * 10) + 10; i++) {
                if (i >= 0 && i < ITEM_EDITOR.sorted.size()) {
                    RenderItemHelper.drawStack((ItemStack) ITEM_EDITOR.sorted.get(i), 32 + (j * 16), CORE.screenTop + 45 + (k * 16), 1F);

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
        return ITEM_EDITOR.getTextField();
    }

    @Override
    public void setTextField(String str) {
        ITEM_EDITOR.setTextField(str);
    }
}