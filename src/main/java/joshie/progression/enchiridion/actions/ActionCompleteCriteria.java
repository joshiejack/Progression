package joshie.progression.enchiridion.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.api.ICriteria;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;

public class ActionCompleteCriteria extends AbstractAction implements IButtonAction {
    public transient String displayName;
    public transient String criteriaID;
    public transient ICriteria criteria;

    public ActionCompleteCriteria() {
        super("criteria.complete");
    }

    public ActionCompleteCriteria(String displayName, String criteriaID, ICriteria criteria) {
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
        return new ActionCompleteCriteria("New Criteria", "", null);
    }

    private ICriteria getCriteria() {
        if (criteria != null) {
            if (criteria.getDisplayName().equals(displayName)) return criteria;
        }

        //Attempt to grab the criteria based on the displayname
        for (ICriteria c : APIHandler.getCriteria().values()) {
            String display = c.getDisplayName();
            if (c.getDisplayName().equals(displayName)) {
                criteria = c;
                criteriaID = c.getUniqueName();
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
        criteriaID = JSONHelper.getString(data, "criteriaID", "");
    }

    @Override
    public void writeToJson(JsonObject data) {
        super.writeToJson(data);
        if (criteriaID != null) {
            JSONHelper.setString(data, "criteriaID", criteriaID, "");
        }
    }
}
