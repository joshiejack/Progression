package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;

public abstract class FeatureProgression extends FeatureAbstract implements ISimpleEditorFieldProvider {
    public FeatureProgression() {}

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
    }

    @Override
    public void onFieldsSet() {
        update(position);
    }
}
