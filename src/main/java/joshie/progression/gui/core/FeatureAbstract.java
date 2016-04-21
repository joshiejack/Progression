package joshie.progression.gui.core;

import joshie.progression.api.gui.IDrawHelper;

import static joshie.progression.gui.core.GuiList.CORE;

public abstract class FeatureAbstract implements IGuiFeature {
    protected IDrawHelper offset;
    protected IDrawHelper draw;

    // Variables used when drawing shizz
    protected int screenWidth;
    protected int guiHeight;
    private boolean isHidden;

    @Override
    public FeatureAbstract init() {
        offset = new DrawHelper();
        draw = new DrawHelperOffset();
        return this;
    }

    @Override
    public final void draw(int mouseX, int mouseY) {
        if (draw == null || offset == null) return; //Avoid crashes
        guiHeight = CORE.ySize; // Set that gui height
        screenWidth = CORE.screenWidth; // Mark the size of the screen
        drawFeature(mouseX, mouseY); // Draw the features
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    public abstract void drawFeature(int mouseX, int mouseY);

    @Override
    public boolean scroll(int mouseX, int mouseY, boolean scrolledDown) {
        return false;
    }

    @Override
    public void setVisibility(boolean value) {
        this.isHidden = !value;
    }

    @Override
    public boolean isVisible() {
        return !isHidden;
    }
}
