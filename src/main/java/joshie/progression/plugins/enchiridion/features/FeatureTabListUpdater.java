package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.ITab;
import joshie.progression.handlers.APICache;
import joshie.progression.plugins.enchiridion.actions.ActionTabList;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import static joshie.progression.gui.core.GuiList.LAST;


public class FeatureTabListUpdater extends FeatureProgression implements ISimpleEditorFieldProvider {
    public FeatureTabListUpdater() {}

    @Override
    public FeatureTabListUpdater copy() {
        return new FeatureTabListUpdater();
    }

    @Override
    public String getName() {
        return "Tabs List Updater";
    }

    private boolean tabExists(ITab tab) {
        for (ITab itab: APICache.getClientCache().getSortedTabs()) {
            if (itab == null) continue; //WHY WOULD YOU CACHE BROKE TABS
            if (itab.getUniqueID().equals(tab.getUniqueID())) return true;
        }

        return false;
    }

    private void rebuildTheList() {
        position.getPage().clear();
        ActionTabList.createPage(position.getPage());
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!EnchiridionAPI.book.isEditMode()) return; //Don't update outside of edit mode
        //Update everything
        boolean rebuild = false;
        Set<ITab> inList = new LinkedHashSet<ITab>();
        for (IFeatureProvider provider: position.getPage().getFeatures()) {
            if (provider.getFeature() instanceof FeatureTab) {
                FeatureTab feature = ((FeatureTab)provider.getFeature());
                if (feature.getTab() != null) {
                    if (!tabExists(feature.getTab())) {
                        rebuild = true;
                        break;
                    } else inList.add(feature.getTab());
                } else {
                    rebuild = true;
                    break;
                }
            }
        }

        if (!rebuild) {
            for (ITab tab : APICache.getClientCache().getSortedTabs()) {
                if (!inList.contains(tab)) {
                    rebuild = true;
                    break;
                }
            }
        }

        if (rebuild) {
            LAST.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    rebuildTheList();
                    return null;
                }
            }); //Let's rebuild the entire list, if the tab no longer exists
        }
    }
}
