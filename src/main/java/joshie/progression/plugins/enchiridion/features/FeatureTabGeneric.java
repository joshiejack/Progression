package joshie.progression.plugins.enchiridion.features;

import com.google.gson.JsonObject;
import joshie.progression.api.criteria.ITab;
import joshie.progression.handlers.APICache;
import joshie.progression.helpers.JSONHelper;

import java.util.UUID;

import static joshie.progression.ItemProgression.tab;


public abstract class FeatureTabGeneric extends FeatureProgression {
    protected transient UUID uuid = UUID.randomUUID();
    protected transient boolean isInit = false;
    public String display = "New Criteria";

    public FeatureTabGeneric() {}

    public FeatureTabGeneric(ITab tab) {
        if (tab != null) {
            uuid = tab.getUniqueID();
            display = tab.getLocalisedName();
        }
    }

    public ITab getTab() {
        return APICache.getClientCache().getTab(uuid);
    }

    @Override
    public void onFieldsSet(String field) {
        super.onFieldsSet(field);

        if (field.equals("")) {
            ITab tab = getTab();
            if (tab != null) display = tab.getLocalisedName();
        } else if (field.equals("display")) {
            for (ITab t : APICache.getClientCache().getTabSet()) {
                if (t.getLocalisedName().equals(display)) {
                    uuid = t.getUniqueID();
                }
            }
        }
    }

   public abstract void drawFeature(int mouseX, int mouseY);

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!isInit) {
            isInit = true;
            onFieldsSet("");
        }

        if (tab != null) {
            drawFeature(mouseX, mouseY);
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
