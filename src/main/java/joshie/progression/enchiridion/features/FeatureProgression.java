package joshie.progression.enchiridion.features;

import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.features.FeatureAbstract;

public abstract class FeatureProgression extends FeatureAbstract implements ISimpleEditorFieldProvider {
    protected transient IFeatureProvider provider;

    public FeatureProgression() {}

    @Override
    public void update(IFeatureProvider position) {
        provider = position;
    }

    @Override
    public void onFieldsSet() {
        update(provider);
    }
}
