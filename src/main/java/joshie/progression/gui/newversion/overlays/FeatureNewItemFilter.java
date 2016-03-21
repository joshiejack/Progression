package joshie.progression.gui.newversion.overlays;

import java.util.Collection;

import joshie.progression.api.IFilter;
import joshie.progression.handlers.APIHandler;

public class FeatureNewItemFilter extends FeatureNew<IFilter> {
    public static final FeatureNewItemFilter INSTANCE = new FeatureNewItemFilter();

    public FeatureNewItemFilter() {
        super("item");
    }

    @Override
    public Collection<IFilter> getFields() {
        return APIHandler.itemFilterTypes.values();
    }

    @Override
    public void clone(IFilter provider) {
        APIHandler.cloneFilter(field, provider);
        //GuiItemFilterEditor.INSTANCE.initGui(); //Refresh the gui
    }

    @Override
    public boolean isValid(IFilter filter) {
        return field.isAccepted(filter);
    }
}
