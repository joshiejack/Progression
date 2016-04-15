package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.ITab;

import java.util.HashSet;
import java.util.Set;


public class FeatureTabListUpdater extends FeatureProgression implements ISimpleEditorFieldProvider {
    public FeatureTabListUpdater() {}
    private transient Set<ITab> list = new HashSet();

    @Override
    public FeatureTabListUpdater copy() {
        return new FeatureTabListUpdater();
    }

    @Override
    public String getName() {
        return "Tabs List Updater";
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!EnchiridionAPI.book.isEditMode()) return; //Don't update outside of edit mode
        //Step one, Compare the list to the special list


        //Update everything
        for (IFeatureProvider provider: position.getPage().getFeatures()) {
            provider.getFeature().update(provider);
        }
    }
}
