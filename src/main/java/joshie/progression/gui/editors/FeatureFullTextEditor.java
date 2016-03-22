package joshie.progression.gui.editors;

import org.lwjgl.opengl.GL11;

import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.editors.FeatureItemSelector.Position;
import net.minecraft.client.renderer.GlStateManager;

public class FeatureFullTextEditor extends FeatureAbstract implements ITextEditable {
    public static FeatureFullTextEditor INSTANCE = new FeatureFullTextEditor();
    private ITextEditable editable = null;
    private Position type;

    public FeatureFullTextEditor() {}

    public ITextEditable getEditable() {
        return editable;
    }

    public void select(ITextEditable editable, Position type) {
        this.type = type;
        this.editable = editable;
        TextEditor.INSTANCE.setEditable(this);
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
        return TextEditor.INSTANCE.getText(editable);
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
        offset.drawGradient(-1, 25 + type.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 15, theme.blackBarGradient1, theme.blackBarGradient2, theme.blackBarBorder);
        offset.drawRectangle(-1, 40 + type.yOffset, GuiTreeEditor.INSTANCE.mc.displayWidth, 73, theme.blackBarUnderLine, theme.blackBarUnderLineBorder);

        offset.drawText("Text Editor", 5, 29 + type.yOffset, theme.blackBarFontColor);
        offset.drawSplitText(TextEditor.INSTANCE.getText(this), 5, 45 + type.yOffset, screenWidth - 5, theme.blackBarFontColor);
    }

    @Override
    public boolean isOverlay() {
        return true;
    }
}