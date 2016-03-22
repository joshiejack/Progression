package joshie.progression.plugins.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.plugins.enchiridion.actions.ActionClaimReward;
import joshie.progression.plugins.enchiridion.actions.ActionCompleteCriteria;
import joshie.progression.plugins.enchiridion.features.ButtonInsertProgression;
import joshie.progression.plugins.enchiridion.features.FeaturePoints;
import joshie.progression.plugins.enchiridion.features.FeatureRewards;
import joshie.progression.plugins.enchiridion.features.FeatureTasks;
import joshie.progression.plugins.enchiridion.rewards.RewardShowLayer;

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
