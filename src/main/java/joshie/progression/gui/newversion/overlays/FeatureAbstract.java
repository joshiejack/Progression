package joshie.progression.gui.newversion.overlays;

import joshie.progression.gui.newversion.GuiCore;
import joshie.progression.json.Theme;

public abstract class FeatureAbstract implements IGuiFeature {
    protected int offsetX; //OffsetX on the scroll position
    protected DrawFeatureHelper offset;
    protected DrawFeatureHelper draw;

    // Variables used when drawing shizz
    protected Theme theme;
    protected int screenWidth;
    protected int guiHeight;
    private boolean isHidden;

    @Override
    public FeatureAbstract init(GuiCore core) {
        offset = new DrawFeatureHelper(core);
        draw = new DrawFeatureOffsetHelper(core);
        return this;
    }

    @Override
    public final void draw(int mouseX, int mouseY) {
        draw.configure();
        offset.configure();
        theme = Theme.INSTANCE; // Grab that theme?
        guiHeight = draw.getGui().ySize; // Set that gui height
        screenWidth = draw.getGui().screenWidth; // Mark the size of the screen
        offsetX = draw.getGui().offsetX; //Scroll position
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
