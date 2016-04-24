package joshie.progression.plugins.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.book.Page;
import joshie.enchiridion.data.book.Template;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.gui.book.features.FeatureImage;
import joshie.enchiridion.gui.book.features.FeaturePreviewWindow;
import joshie.enchiridion.gui.book.features.FeatureText;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.handlers.ProgressionEvents;
import joshie.progression.handlers.ProgressionEvents.Checker;
import joshie.progression.plugins.enchiridion.actions.ActionClaimCriteria;
import joshie.progression.plugins.enchiridion.actions.ActionClaimReward;
import joshie.progression.plugins.enchiridion.actions.ActionCompleteCriteria;
import joshie.progression.plugins.enchiridion.actions.ActionTabList;
import joshie.progression.plugins.enchiridion.features.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import static joshie.progression.ItemProgression.ItemMeta.book;
import static joshie.progression.ItemProgression.ItemMeta.edit;
import static joshie.progression.ItemProgression.getStackFromMeta;

public class EnchiridionSupport {
    private static final Class[] classes = new Class[] { FeaturePoints.class, FeatureRewards.class, FeatureTasks.class, FeatureCriteria.class , FeatureTab.class};
    public static final ResourceLocation TRANSPARENT = new ResourceLocation("progression:textures/books/transparent.png");

    public static void init() {
        EnchiridionAPI.library.registerBookHandlerForStack("rightClick", getStackFromMeta(book), true, false);
        EnchiridionAPI.library.registerBookHandlerForStack("rightClick", getStackFromMeta(edit), true, false);
        EnchiridionAPI.instance.registerButtonAction(new ActionClaimCriteria());
        EnchiridionAPI.instance.registerButtonAction(new ActionCompleteCriteria());
        EnchiridionAPI.instance.registerButtonAction(new ActionClaimReward());
        EnchiridionAPI.instance.registerButtonAction(new ActionTabList());
        for (Class clazz: classes) {
            EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertProgression(clazz));
        }

        Page page = new Page(0);
        //Add the button
        FeatureButton button = new FeatureButton(new ActionTabList());
        button.setResourceLocation(true, TRANSPARENT).setResourceLocation(false, TRANSPARENT);
        button.setText(true, "[color=gray]Click here to start").setText(false, "Click here to start");
        button.setSize(0.79999995F);
        page.addFeature(button, 63, 191, 91, 9, true, false);
        //Add the logo
        FeatureImage image = new FeatureImage("progression:textures/books/logo.png");
        page.addFeature(image, 17, 5, 177, 63, true, false);
        //Add the text subtitle
        FeatureText text = new FeatureText("[i]HQM CLONE EDITION");
        page.addFeature(text, 111, 53, 107, 11, true, false);
        //Add the preview
        FeaturePreviewWindow preview = new FeaturePreviewWindow(3);
        page.addFeature(preview, 224, 9, 190, 195, true, false);

        Template template = new Template("progression_hqm_clone", "HQM Clone", new ResourceLocation("progression:textures/books/HQM_Clone.png"), page);
        EnchiridionAPI.instance.registerTemplate(template);

        ProgressionEvents.checker = new Checker() {
            @Override
            public boolean isRunnable(GuiScreen screen) {
                return screen instanceof GuiCore || screen instanceof GuiBook;
            }
        };

        //ProgressionAPI.registry.registerRewardType(RewardShowLayer.class, "layer.show", 0xFFCCCCCC);
        //ProgressionAPI.registry.registerRewardType(RewardOpenBook.class, "open.book", 0xFFCCCCCC);
    }
}
