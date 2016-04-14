package joshie.progression.plugins.enchiridion.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.network.PacketClaimReward;
import joshie.progression.network.PacketHandler;

public class ActionClaimReward extends AbstractActionCriteria implements IButtonAction {
    public boolean randomReward = false;
    public int rewardPosition = 1;

    public ActionClaimReward() {
        super(null, "reward");
    }

    public ActionClaimReward(ICriteria criteria, boolean randomReward, int rewardPosition) {
        super(criteria, "reward");
        this.randomReward = randomReward;
        this.rewardPosition = rewardPosition;
    }

    @Override
    public IButtonAction copy() {
        return copyAbstract(new ActionClaimReward(criteria, randomReward, rewardPosition));
    }

    @Override
    public IButtonAction create() {
        return new ActionClaimReward(APIHandler.getCache(true).getRandomCriteria(), false, 1);
    }

    @Override
    public void performAction() {
        if (criteria != null) {
            PacketHandler.sendToServer(new PacketClaimReward(criteria, rewardPosition - 1, randomReward));
        }
    }

    @Override
    public void readFromJson(JsonObject data) {
        super.readFromJson(data);
        randomReward = JSONHelper.getBoolean(data, "randomReward", false);
        rewardPosition = JSONHelper.getInteger(data, "rewardPosition", 1);
    }

    @Override
    public void writeToJson(JsonObject data) {
        super.writeToJson(data);
        JSONHelper.setBoolean(data, "randomReward", randomReward, false);
        JSONHelper.setInteger(data, "rewardPosition", rewardPosition, 1);
    }
}
