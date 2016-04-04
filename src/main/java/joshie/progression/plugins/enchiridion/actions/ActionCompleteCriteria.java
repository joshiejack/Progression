package joshie.progression.plugins.enchiridion.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;

import java.util.UUID;

public class ActionCompleteCriteria extends AbstractAction implements IButtonAction {
    public transient String displayName;
    public transient UUID criteriaID;
    public transient ICriteria criteria;

    public ActionCompleteCriteria() {
        super("criteria.complete");
    }

    public ActionCompleteCriteria(String displayName, UUID criteriaID, ICriteria criteria) {
        super("criteria.complete");
        this.displayName = displayName;
        this.criteriaID = criteriaID;
        this.criteria = criteria;
    }

    @Override
    public void initAction() {
        criteria = getCriteria();
    }

    @Override
    public IButtonAction copy() {
        return new ActionCompleteCriteria(displayName, criteriaID, criteria);
    }

    @Override
    public String[] getFieldNames() {
        initAction();
        return new String[] { "tooltip", "hoverText", "unhoveredText", "displayName" };
    }

    @Override
    public IButtonAction create() {
        return new ActionCompleteCriteria("New Criteria", UUID.randomUUID(), null);
    }

    private ICriteria getCriteria() {
        if (criteria != null) {
            if (criteria.getLocalisedName().equals(displayName)) return criteria;
        }

        //Attempt to grab the criteria based on the displayname
        for (ICriteria c : APIHandler.getCriteria().values()) {
            String display = c.getLocalisedName();
            if (c.getLocalisedName().equals(displayName)) {
                criteria = c;
                criteriaID = c.getUniqueID();
                return criteria;
            }
        }

        return null;
    }

    @Override
    public void performAction() {
        ICriteria criteria = getCriteria();
        if (criteria != null) {
            ProgressionAPI.registry.fireTriggerClientside("forced-complete", getCriteria());
        }
    }

    @Override
    public void readFromJson(JsonObject data) {
        super.readFromJson(data);
        criteriaID = UUID.fromString(JSONHelper.getString(data, "criteriaID", ""));
    }

    @Override
    public void writeToJson(JsonObject data) {
        super.writeToJson(data);
        if (criteriaID != null) {
            JSONHelper.setString(data, "criteriaID", criteriaID.toString(), "");
        }
    }
}
