package joshie.progression.gui.editors.insert;

import java.util.Collection;

import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.handlers.APIHandler;

public class FeatureNewItemFilter extends FeatureNew<IProgressionFilter> {
    public static final FeatureNewItemFilter INSTANCE = new FeatureNewItemFilter();

    public FeatureNewItemFilter() {
        super("item");
    }

    @Override
    public Collection<IProgressionFilter> getFields() {
        return APIHandler.itemFilterTypes.values();
    }

    @Override
    public void clone(IProgressionFilter provider) {
        APIHandler.cloneFilter(field, provider);
        //GuiItemFilterEditor.INSTANCE.initGui(); //Refresh the gui
    }

    @Override
    public boolean isValid(IProgressionFilter filter) {
        return field.isAccepted(filter);
    }
}
