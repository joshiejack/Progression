package joshie.progression.plugins.enchiridion.actions;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.buttons.actions.AbstractAction;
import joshie.enchiridion.gui.book.features.FeaturePreviewWindow;
import joshie.enchiridion.util.ELocation;
import joshie.progression.api.criteria.ITab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.plugins.enchiridion.features.FeatureTab;
import joshie.progression.plugins.enchiridion.features.FeatureTabListUpdater;

public class ActionTabList extends AbstractAction {
    public ActionTabList() {
        super("tab.list");
    }

    @Override
    public IButtonAction copy() {
        return copyAbstract(new ActionTabList());
    }

    @Override
    public IButtonAction create() {
        return new ActionTabList();
    }

    @Override
    public void performAction() {
        //Create page 3
        IPage page = EnchiridionAPI.book.getPageIfNotExists(2);
        if (page != null) {
            page.toggleScroll(); //Make page 3 scrollable

            int index = 0;
            for (ITab tab: APIHandler.getCache(true).getSortedTabs()) {
                FeatureTab feature = new FeatureTab(tab);
                page.addFeature(feature, 26, 23 + (30 * index), 150, 18, true, false);
                index++;
            }

            //Add the autoupdater
            page.addFeature(new FeatureTabListUpdater(), -250, -250, 1, 1, true, false);
        }

        //Create page 2
        page = EnchiridionAPI.book.getPageIfNotExists(1);
        if (page != null) {
            FeaturePreviewWindow preview = new FeaturePreviewWindow(2);
            page.addFeature(preview, 16, 18, 189, 183, true, false);
            IButtonActionProvider button = EnchiridionAPI.editor.getJumpPageButton(1);
            button.getAction().setResourceLocation(true, new ELocation("arrow_left_on")).setResourceLocation(false, new ELocation("arrow_left_off"));
            page.addFeature(button, 21, 200, 18, 10, true, false);
        }

        EnchiridionAPI.book.jumpToPageIfExists(1);
    }
}
