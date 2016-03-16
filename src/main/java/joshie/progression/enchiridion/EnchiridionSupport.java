package joshie.progression.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.enchiridion.actions.ActionClaimReward;
import joshie.progression.enchiridion.actions.ActionCompleteCriteria;
import joshie.progression.enchiridion.features.ButtonInsertPoints;
import joshie.progression.enchiridion.rewards.RewardShowLayer;

public class EnchiridionSupport {
    public static void init() {
        EnchiridionAPI.instance.registerButtonAction(new ActionCompleteCriteria());
        EnchiridionAPI.instance.registerButtonAction(new ActionClaimReward());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertPoints());
        ProgressionAPI.registry.registerRewardType(new RewardShowLayer());
    }
}
