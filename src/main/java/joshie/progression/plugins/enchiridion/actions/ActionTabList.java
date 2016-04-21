package joshie.progression.plugins.enchiridion.actions;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.buttons.actions.AbstractAction;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.gui.book.features.FeaturePreviewWindow;
import joshie.enchiridion.gui.book.features.FeatureText;
import joshie.enchiridion.util.ELocation;
import joshie.progression.api.criteria.ITab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.plugins.enchiridion.features.FeatureTab;
import joshie.progression.plugins.enchiridion.features.FeatureTabInfo;
import joshie.progression.plugins.enchiridion.features.FeatureTabListUpdater;
import net.minecraft.util.ResourceLocation;

import static joshie.progression.lib.ProgressionInfo.BOOKPATH;
import static joshie.progression.plugins.enchiridion.EnchiridionSupport.TRANSPARENT;

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

    public static void createPage(IPage page) {
        if (page != null) {
            if (!page.isScrollingEnabled()) {
                page.toggleScroll(); //Make page 3 scrollable
            }

            int index = 0;
            for (ITab tab: APIHandler.getClientCache().getSortedTabs()) {
                FeatureTab feature = new FeatureTab(tab);
                page.addFeature(feature, 26, 20 + (20 * index), 150, 15, true, false);

                //Create the dummy page for this pages description
                int number = tab.getUniqueID().hashCode() + 1;
                IPage dummy = EnchiridionAPI.book.getPageIfNotExists(number);
                if (dummy != null) {
                    FeatureText text = new FeatureText("Go to page " + number + " to edit this text, easiest way to do this, is to go to the chapter, and this page is the page directly after the page it is on.");
                    dummy.addFeature(text, 229, 22, 181, 82, false, false);
                }

                index++;
            }

            //Add the autoupdater
            page.addFeature(new FeatureTabListUpdater(), 20, 20, 1, 1, true, false);
        }
    }

    @Override
    public boolean performAction() {
        //Create page 3
        IPage page = EnchiridionAPI.book.getPageIfNotExists(2);
        if (page != null) {
            createPage(page);
        }

        //Create page 2
        page = EnchiridionAPI.book.getPageIfNotExists(1);
        if (page != null) {
            //Preview the Tab's Description
            ITab first = null;
            for (ITab tab: APIHandler.getCache(true).getSortedTabs()) {
                first = tab;
                break;
            }

            if (first != null) {
                FeaturePreviewWindow window = new FeaturePreviewWindow(first.getUniqueID().hashCode() + 1);
                page.addFeature(window, 226, 18, 193, 120, true, false);

                //Display the Tab's Info
                FeatureTabInfo info = new FeatureTabInfo(first);
                page.addFeature(info, 230, 140, 171, 44, true, false);

                //Jump to Next Page
                IButtonActionProvider jump = new FeatureButton(new ActionJumpTab(first.getUniqueID().hashCode()));
                jump.setResourceLocation(true, new ResourceLocation(BOOKPATH + "open_button_on.png")).setResourceLocation(false, new ResourceLocation(BOOKPATH + "open_button_off.png"));
                jump.setTooltip("Open " + first.getLocalisedName());
                jump.setText(true, "[color=gray]Open").setText(false, "Open");
                jump.setTextOffsetX(true, 13).setTextOffsetX(false, 13);
                jump.setTextOffsetY(true, 3).setTextOffsetY(false, 3);
                page.addFeature(jump, 313, 175, 50, 14, true, false);
            }

            //Preview the Tab List
            FeaturePreviewWindow preview = new FeaturePreviewWindow(2);
            page.addFeature(preview, 16, 18, 189, 183, true, false);

            //Return to homepage
            IButtonActionProvider button = EnchiridionAPI.editor.getJumpPageButton(0);
            button.setResourceLocation(true, new ELocation("arrow_left_on")).setResourceLocation(false, new ELocation("arrow_left_off"));
            page.addFeature(button, 21, 200, 18, 10, true, false);

            //Return to homepage right click anywhere
            IButtonActionProvider pageBack = EnchiridionAPI.editor.getJumpPageButton(0);
            pageBack.setResourceLocation(true, TRANSPARENT).setResourceLocation(false, TRANSPARENT);
            pageBack.setProcessesClick(0, false);
            page.addFeature(pageBack, -10, -10, 450, 250, true, false);
        }

        return EnchiridionAPI.book.jumpToPageIfExists(1);
    }
}
