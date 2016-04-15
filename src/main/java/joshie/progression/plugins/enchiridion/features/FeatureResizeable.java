package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import net.minecraft.client.gui.GuiScreen;

public abstract class FeatureResizeable extends FeatureProgression implements ISimpleEditorFieldProvider {
    private transient double cachedWidth = 0;
    public transient int wrap = 50;
    public float size = 1F;

    public FeatureResizeable() {}

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        cachedWidth = position.getWidth();
        wrap = Math.max(50, (int) (cachedWidth / size) + 4);
    }

    public FeatureResizeable copySize(FeatureResizeable feature) {
        feature.size = size;
        return this;
    }

    @Override
    public boolean getAndSetEditMode() {
        EnchiridionAPI.editor.setSimpleEditorFeature(this);
        return true;
    }

    @Override
    public void keyTyped(char character, int key) {
        if (GuiScreen.isShiftKeyDown()) {
            if (key == 78) {
                size = Math.min(15F, Math.max(0.5F, size + 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            } else if (key == 74) {
                size = Math.min(15F, Math.max(0.5F, size - 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            }
        }
    }
}
