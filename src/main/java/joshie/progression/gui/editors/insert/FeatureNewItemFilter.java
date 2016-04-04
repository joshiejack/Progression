package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.gui.editors.GuiFilterEditor;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

public class FeatureNewItemFilter extends FeatureNew<IFilterProvider> {
    public static final FeatureNewItemFilter INSTANCE = new FeatureNewItemFilter();

    public FeatureNewItemFilter() {
        super("item");
    }

    @Override
    public Collection<IFilterProvider> getFields() {
        return APIHandler.filterTypes.values();
    }

    @Override
    public void clone(IFilterProvider provider) {
        APIHandler.cloneFilter(GuiFilterEditor.INSTANCE.getField(), provider);
        //GuiFilterEditor.INSTANCE.initGui(); //Refresh the gui
    }

    @Override
    public boolean isValid(IFilterProvider filter) {
        return GuiFilterEditor.INSTANCE.getField().isAccepted(filter);
    }
}
