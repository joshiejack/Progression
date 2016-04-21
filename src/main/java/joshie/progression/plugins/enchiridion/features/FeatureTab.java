package joshie.progression.plugins.enchiridion.features;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.gui.book.features.FeaturePreviewWindow;
import joshie.enchiridion.util.ELocation;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.gui.editors.GuiTreeEditor;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.plugins.enchiridion.actions.ActionJumpTab;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static joshie.progression.plugins.enchiridion.EnchiridionSupport.TRANSPARENT;


public class FeatureTab extends FeatureTabGeneric {
    private static final Cache<UUID, Integer> numberCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();
    protected transient boolean isSelected = false;
    public int pageNumber = 0;

    public FeatureTab() {}

    public FeatureTab(ITab tab) {
        if (tab != null) {
            uuid = tab.getUniqueID();
            display = tab.getLocalisedName();
            pageNumber = uuid.hashCode();
        }
    }

    @Override
    public FeatureTab copy() {
        return new FeatureTab(getTab());
    }

    public int getTabNumber(final ITab tab) {
        try {
            return numberCache.get(tab.getUniqueID(), new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int number = 1;
                    for (ITab t : APIHandler.getCache(true).getSortedTabs()) {
                        if (t.getUniqueID().equals(tab.getUniqueID())) return number;

                        number++;
                    }

                    return 0;
                }
            });
        } catch (Exception e) { return 0; }
    }

    public int getCompletionAmount(ITab tab) {
        int totaltasks = tab.getCriteria().size();
        if (totaltasks == 0) return 100;
        Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID(), true);

        int tasksdone = 0;
        for (ICriteria criteria: completed) {
            if (tab.equals(criteria.getTab())) tasksdone++;
        }

        return (tasksdone * 100) / totaltasks;
    }

    public static void addCriteriaToPage(IPage page, ICriteria criteria) {
        FeatureCriteria feature = new FeatureCriteria(criteria, true);
        page.addFeature(feature, new Random().nextInt(400), new Random().nextInt(200), 16, 16, false, false);
        // ^ Put the stuff in a random position :D
    }

    @Override
    public boolean performClick(int mouseX, int mouseY, int mouseButton) {
        ITab tab = getTab();
        if (tab != null) {
            if (mouseX >= position.getLeft() && mouseX <= position.getRight()) {
                if (mouseY >= position.getTop() && mouseY <= position.getBottom()) {
                    IPage page = EnchiridionAPI.book.getPageIfNotExists(pageNumber);
                    if (page != null) {
                        //Add the back button
                        IButtonActionProvider button = EnchiridionAPI.editor.getJumpPageButton(EnchiridionAPI.book.getPage().getPageNumber());
                        button.setResourceLocation(true, new ELocation("arrow_left_on")).setResourceLocation(false, new ELocation("arrow_left_off"));
                        page.addFeature(button, 21, 200, 18, 10, true, false);

                        //Add the criteria
                        for (ICriteria c: tab.getCriteria()) {
                            addCriteriaToPage(page, c);
                        }

                        //Return to this tab page
                        IButtonActionProvider pageBack = EnchiridionAPI.editor.getJumpPageButton(EnchiridionAPI.book.getPage().getPageNumber());
                        pageBack.setResourceLocation(true, TRANSPARENT).setResourceLocation(false, TRANSPARENT);
                        pageBack.setProcessesClick(0, false);
                        page.addFeature(pageBack, -10, -10, 450, 250, true, false);

                        //Add the autoupdater
                        FeatureTabUpdater updater = new FeatureTabUpdater(tab.getUniqueID());
                        page.addFeature(updater, -250, -250, 1, 1, true, false);
                    }

                    for (IFeatureProvider feature: EnchiridionAPI.book.getPage().getFeatures()) {
                        if (feature.getFeature() instanceof FeaturePreviewWindow) {
                            FeaturePreviewWindow window = ((FeaturePreviewWindow)feature.getFeature());
                            if (window.pageNumber > 10 || window.pageNumber < 0) {
                                window.pageNumber = pageNumber + 1; //Update the preview text to the description
                                window.update(feature);
                            }
                        } else if (feature.getFeature() instanceof FeatureTabInfo) {
                            ((FeatureTabInfo)feature.getFeature()).uuid = tab.getUniqueID(); //Change the tab being displayed to this one
                        } else if (feature.getFeature() instanceof FeatureButton) {
                            FeatureButton button = (FeatureButton)(feature.getFeature());
                            if (button.getAction() instanceof ActionJumpTab) {
                                button.setTooltip("Open " + tab.getLocalisedName());
                                ((ActionJumpTab) button.getAction()).tempPage = pageNumber;
                            }
                        }
                    }

                    for (IFeatureProvider feature: position.getPage().getFeatures()) {
                        if (feature.getFeature() instanceof FeatureTab) {
                            ((FeatureTab)feature.getFeature()).isSelected = false;
                        }
                    }

                    isSelected = true;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int color = 0xFF404040;
        if (mouseX >= position.getLeft() && mouseX <= position.getRight()) {
            if (mouseY>= position.getTop() && mouseY <= position.getBottom()) {
                color = 0xFFAAAAAA;
            }
        }

        if (isSelected) color = 0xFF7C7C7C;

        ITab tab = getTab();
        if (tab != null) {
            String completion = getCompletionAmount(tab) + "% Completed";
            if (!GuiTreeEditor.isTabVisible(tab)) {
                completion = "Invisible";
                color = 0xFFCCCCCC;
            }

            EnchiridionAPI.draw.drawSplitScaledString(getTabNumber(tab) + ".", position.getLeft(), position.getTop(), 200, color, 1F);
            EnchiridionAPI.draw.drawSplitScaledString(tab.getLocalisedName(), position.getLeft() + 12, position.getTop(), 200, color, 1F);
            EnchiridionAPI.draw.drawSplitScaledString(completion, position.getLeft() + 9, position.getTop() + 10, 100, color, 0.75F);
        }
    }
}
