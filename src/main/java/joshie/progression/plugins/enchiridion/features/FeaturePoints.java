package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.player.PlayerTracker;

public class FeaturePoints extends FeatureResizeable implements ISimpleEditorFieldProvider {
    public String description = "[amount] Gold";
    public String variable = "gold";

    public FeaturePoints() {}

    public FeaturePoints(String desc, String var) {
        description = desc;
        variable = var;
    }

    @Override
    public FeaturePoints copy() {
        FeaturePoints text = new FeaturePoints(description, variable);
        copySize(text);
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
}
