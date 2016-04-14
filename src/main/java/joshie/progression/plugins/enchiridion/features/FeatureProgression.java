package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.enchiridion.gui.book.features.FeatureAbstract;

public abstract class FeatureProgression extends FeatureAbstract implements ISimpleEditorFieldProvider {
    protected static final String ARROWPATH = "enchiridion:textures/books/arrow_left_";
    public FeatureProgression() {}

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
    }

    @Override
    public void onFieldsSet(String field) {
        update(position);
    }

    @Override
    public boolean getAndSetEditMode() {
        EnchiridionAPI.editor.setSimpleEditorFeature(this);
        return true;
    }
}
