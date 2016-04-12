package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

import static joshie.progression.gui.core.GuiList.FILTER_EDITOR;

public class FeatureNewFilter extends FeatureNew<IFilterProvider> {

    public FeatureNewFilter() {
        super("item");
    }

    @Override
    public Collection<IFilterProvider> getFields() {
        return APIHandler.filterTypes.values();
    }

    @Override
    public void clone(IFilterProvider provider) {
        APIHandler.cloneFilter(FILTER_EDITOR.get(), provider);
        //GuiFilterEditor.GROUP_EDITOR.initGui(); //Refresh the gui
    }

    @Override
    public boolean isValid(IFilterProvider filter) {
        return FILTER_EDITOR.get().isAccepted(filter);
    }
}
