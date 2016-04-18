package joshie.progression.plugins.enchiridion.actions;

import com.google.gson.JsonObject;
import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;

import java.util.UUID;

public abstract class AbstractActionCriteria extends AbstractActionProgression {
    public AbstractActionCriteria() {}
    public AbstractActionCriteria(ICriteria criteria, String name) {
        super(name);
        if (criteria != null) {
            uuid = criteria.getUniqueID();
            display = criteria.getLocalisedName();
        }
    }

    public ICriteria getCriteria() {
        return APIHandler.getClientCache().getCriteria(uuid);
    }

    @Override
    public void onFieldsSet(String field) {
        if (field.equals("")) {
            ICriteria criteria = getCriteria();
            if (criteria != null) display = criteria.getLocalisedName();
        } else if (field.equals("display")) {
            for (ICriteria c : APIHandler.getClientCache().getCriteriaSet()) {
                if (c.getLocalisedName().equals(display)) {
                    uuid = c.getUniqueID();
                }
            }
        }
    }

    @Override
    public void readFromJson(JsonObject object) {
        super.readFromJson(object);

        try {
            uuid = UUID.fromString(JSONHelper.getString(object, "uuid", "d977334a-a7e9-5e43-b87e-91df8eebfdff"));
        } catch (Exception e){}
    }

    @Override
    public void writeToJson(JsonObject object) {
        super.writeToJson(object);

        if (uuid != null) {
            JSONHelper.setString(object, "uuid", uuid.toString(), "d977334a-a7e9-5e43-b87e-91df8eebfdff");
        }
    }

    @Override
    public String getName() {
        return Progression.translate("action." + name);
    }
}
