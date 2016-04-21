package joshie.progression.plugins.enchiridion.features;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static joshie.progression.plugins.enchiridion.features.FeatureTab.addCriteriaToPage;


public class FeatureTabUpdater extends FeatureProgression implements ISimpleEditorFieldProvider {
    private transient UUID uuid;

    public FeatureTabUpdater() {}

    public FeatureTabUpdater(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public FeatureTabUpdater copy() {
        return new FeatureTabUpdater(uuid);
    }

    @Override
    public String getName() {
        return "Tab Updater";
    }

    private boolean pageContainsCriteria(ICriteria criteria) {
        for (IFeatureProvider feature: position.getPage().getFeatures()) {
            if (feature.getFeature() instanceof FeatureCriteria) {
                FeatureCriteria c = ((FeatureCriteria) feature.getFeature());
                if (c.uuid != null && criteria.getUniqueID().equals(c.uuid)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean tabContainsCriteria(ITab tab, UUID uuid) {
        for (ICriteria c: tab.getCriteria()) {
            if (c.getUniqueID().equals(uuid)) return true;
        }

        return false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!EnchiridionAPI.book.isEditMode()) return; //Don't update outside of edit mode
        ITab tab = APIHandler.getClientCache().getTab(uuid);
        if (tab != null) {
            //Update and add new criteria from the page
            for (ICriteria criteria: tab.getCriteria()) {
                if (!pageContainsCriteria(criteria)) {
                    addCriteriaToPage(position.getPage(), criteria);
                }
            }

            //Update and remove criteria that aren't supposed to be here
            List<IFeatureProvider> list = new ArrayList<IFeatureProvider>();
            for (IFeatureProvider feature: position.getPage().getFeatures()) {
                if (feature.getFeature() instanceof FeatureCriteria) {
                    FeatureCriteria c = ((FeatureCriteria) feature.getFeature());
                    if (c.getCriteria() == null || !tabContainsCriteria(tab, c.uuid)) {
                        list.add(feature);
                    }
                }
            }

            for (IFeatureProvider provider: list) {
                EnchiridionAPI.book.getPage().removeFeature(provider);
            }
        }

        //Update everything
        for (IFeatureProvider provider: position.getPage().getFeatures()) {
            provider.getFeature().update(provider);
        }
    }

    @Override
    public void readFromJson(JsonObject object) {
        try {
            uuid = UUID.fromString(JSONHelper.getString(object, "uuid", "d977334a-a7e9-5e43-b87e-91df8eebfdff"));
        } catch (Exception e){}
    }

    @Override
    public void writeToJson(JsonObject object) {
        if (uuid != null) {
            JSONHelper.setString(object, "uuid", uuid.toString(), "d977334a-a7e9-5e43-b87e-91df8eebfdff");
        }
    }
}
