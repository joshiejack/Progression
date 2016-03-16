package joshie.progression.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.gui.book.buttons.ButtonAbstract;

public class ButtonInsertProgression extends ButtonAbstract {
    private Class clazz;

    public ButtonInsertProgression(Class clazz) {
        super(clazz.getSimpleName().toLowerCase().replace("feature", ""));
        this.clazz = clazz;
    }

    @Override
    public void performAction() {
        try {
            EnchiridionAPI.book.getPage().addFeature((IFeature) clazz.newInstance(), 0, 0, 18D, 10D, false, false);
        } catch (Exception e) {}
    }
}
