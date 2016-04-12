package joshie.progression.gui.editors;

import joshie.progression.api.gui.Position;
import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.core.GuiList;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import static joshie.progression.gui.core.GuiList.CORE;
import static joshie.progression.gui.core.GuiList.TEXT_EDITOR_SIMPLE;

public class FeatureFullTextEditor extends FeatureAbstract implements ITextEditable {
    private ITextEditable editable = null;
    private Position type;

    public FeatureFullTextEditor() {}

    public ITextEditable getEditable() {
        return editable;
    }

    public void select(ITextEditable editable, Position type) {
        this.type = type;
        this.editable = editable;
        TEXT_EDITOR_SIMPLE.setEditable(this);
    }

    public void clearEditable() {
        this.editable = null;
    }

    @Override
    public boolean scroll(int mouseX, int mouseY, boolean scrolledDown) {
        return false;
    }

    @Override
    public void setTextField(String text) {
        editable.setTextField(text);
    }

    @Override
    public String getTextField() {
        return editable.getTextField();
    }
    
    public String getText(ITextEditable editable) {
        return TEXT_EDITOR_SIMPLE.getText(editable);
    }

    @Override
    public boolean isVisible() {
        return editable != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        offset.drawGradient(-1, 25 + type.yOffset, CORE.mc.displayWidth, 15, GuiList.THEME.blackBarGradient1, GuiList.THEME.blackBarGradient2, GuiList.THEME.blackBarBorder);
        offset.drawRectangle(-1, 40 + type.yOffset, CORE.mc.displayWidth, 81, GuiList.THEME.blackBarUnderLine, GuiList.THEME.blackBarUnderLineBorder);

        offset.drawText("Text Editor", 5, 29 + type.yOffset, GuiList.THEME.blackBarFontColor);
        offset.drawSplitText(TEXT_EDITOR_SIMPLE.getText(this), 5, 45 + type.yOffset, screenWidth - 5, GuiList.THEME.blackBarFontColor);
    }

    @Override
    public boolean isOverlay() {
        return true;
    }
}