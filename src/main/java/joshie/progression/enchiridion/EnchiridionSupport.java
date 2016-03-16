package joshie.progression.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.enchiridion.actions.ActionClaimReward;
import joshie.progression.enchiridion.actions.ActionCompleteCriteria;
import joshie.progression.enchiridion.features.ButtonInsertProgression;
import joshie.progression.enchiridion.features.FeaturePoints;
import joshie.progression.enchiridion.features.FeatureRewards;
import joshie.progression.enchiridion.features.FeatureTasks;
import joshie.progression.enchiridion.rewards.RewardShowLayer;

public class EnchiridionSupport {
    private static final Class[] classes = new Class[] { FeaturePoints.class, FeatureRewards.class, FeatureTasks.class };
    
    public static void init() {
        EnchiridionAPI.instance.registerButtonAction(new ActionCompleteCriteria());
        EnchiridionAPI.instance.registerButtonAction(new ActionClaimReward());
        for (Class clazz: classes) {
            EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertProgression(clazz));
        }
        
        ProgressionAPI.registry.registerRewardType(new RewardShowLayer());
    }
}
