package joshie.progression.plugins.enchiridion.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.network.PacketClaimReward;
import joshie.progression.network.PacketHandler;

import java.util.UUID;

public class ActionClaimReward extends AbstractAction implements IButtonAction {
    public transient String displayName;
    public transient UUID criteriaID;
    public transient ICriteria criteria;
    public transient boolean randomReward;
    public transient int rewardPosition;

    public ActionClaimReward() {
        super("reward");
    }

    public ActionClaimReward(String displayName, UUID criteriaID, ICriteria criteria, int rewardPosition) {
        super("reward");
        this.displayName = displayName;
        this.criteriaID = criteriaID;
        this.criteria = criteria;
        this.rewardPosition = rewardPosition;
    }

    @Override
    public void initAction() {
        criteria = getCriteria();
    }

    @Override
    public IButtonAction copy() {
        return new ActionClaimReward(displayName, criteriaID, criteria, rewardPosition);
    }

    @Override
    public String[] getFieldNames() {
        initAction();
        return new String[] { "tooltip", "hoverText", "unhoveredText", "displayName", "rewardPosition", "randomReward" };
    }

    @Override
    public IButtonAction create() {
        return new ActionClaimReward("New Criteria", UUID.randomUUID(), null, 1);
    }

    private ICriteria getCriteria() {
        if (criteria != null) {
            if (criteria.getLocalisedName().equals(displayName)) return criteria;
        }

        //Attempt to grab the criteria based on the displayname
        for (ICriteria c : APIHandler.getCache(true).getCriteria().values()) {
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
            PacketHandler.sendToServer(new PacketClaimReward(criteria, rewardPosition - 1, randomReward));
        }
    }

    @Override
    public void readFromJson(JsonObject data) {
        super.readFromJson(data);
        criteriaID = UUID.fromString(JSONHelper.getString(data, "criteriaID", ""));
        randomReward = JSONHelper.getBoolean(data, "randomReward", false);
        rewardPosition = JSONHelper.getInteger(data, "rewardPosition", 1);
    }

    @Override
    public void writeToJson(JsonObject data) {
        super.writeToJson(data);
        JSONHelper.setBoolean(data, "randomReward", randomReward, false);
        JSONHelper.setInteger(data, "rewardPosition", rewardPosition, 1);

        if (criteriaID != null) {
            JSONHelper.setString(data, "criteriaID", criteriaID.toString(), "");
        }
    }
}
