package joshie.progression.plugins.enchiridion;

        import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.plugins.enchiridion.actions.ActionClaimReward;
import joshie.progression.plugins.enchiridion.actions.ActionCompleteCriteria;
        import joshie.progression.plugins.enchiridion.actions.ActionTabList;
        import joshie.progression.plugins.enchiridion.features.*;

public class EnchiridionSupport {
    private static final Class[] classes = new Class[] { FeaturePoints.class, FeatureRewards.class, FeatureTasks.class, FeatureTabList.class, FeatureCriteria.class , FeatureTab.class};

    public static void init() {
        EnchiridionAPI.instance.registerButtonAction(new ActionCompleteCriteria());
        EnchiridionAPI.instance.registerButtonAction(new ActionClaimReward());
        EnchiridionAPI.instance.registerButtonAction(new ActionTabList());
        for (Class clazz: classes) {
            EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertProgression(clazz));
        }

        //ProgressionAPI.registry.registerRewardType(RewardShowLayer.class, "layer.show", 0xFFCCCCCC);
        //ProgressionAPI.registry.registerRewardType(RewardOpenBook.class, "open.book", 0xFFCCCCCC);
    }
}
