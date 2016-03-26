package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.player.PlayerTracker;

public class FeaturePoints extends FeatureProgression implements ISimpleEditorFieldProvider {
    private transient double cachedWidth = 0;
    public transient int wrap = 50;
    public float size = 1F;
    public String description = "[amount] Gold";
    public String variable = "gold";

    public FeaturePoints() {}

    public FeaturePoints(String desc, String var) {
        description = desc;
        variable = var;
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        cachedWidth = position.getWidth();
        wrap = Math.max(50, (int) (cachedWidth / size) + 4);
    }

    @Override
    public FeaturePoints copy() {
        FeaturePoints text = new FeaturePoints(description, variable);
        text.size = size;
        return text;
    }

    public String amountAsString(double amount) {
        if (amount == (long) amount) return String.format("%d", (long) amount);
        else return String.format("%s", amount);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (variable != null) {
            double amount = PlayerTracker.getClientPlayer().getPoints().getDouble(variable);
            EnchiridionAPI.draw.drawSplitScaledString(description.replace("[amount]", amountAsString(amount)), position.getLeft(), position.getTop(), wrap, 0x555555, size);
        }
    }

    @Override
    public boolean getAndSetEditMode() {
        EnchiridionAPI.editor.setSimpleEditorFeature(this);
        return true;
    }

    @Override
    public void keyTyped(char character, int key) {
        if (MCClientHelper.isShiftPressed()) {
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
