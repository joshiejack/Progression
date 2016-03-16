package joshie.progression.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.gui.book.buttons.ButtonAbstract;

public class ButtonInsertPoints extends ButtonAbstract {
    public ButtonInsertPoints() {
        super("points");
    }

    @Override
    public void performAction() {
        EnchiridionAPI.book.getPage().addFeature(new FeaturePoints(), 0, 0, 18D, 10D, false, false);
    }
}
