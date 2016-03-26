package joshie.progression.plugins.enchiridion.features;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionTab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.ProgressionInfo;

import java.util.Set;

public class FeatureTabList extends FeatureProgression {
    public FeatureTabList() {}

    private transient Cache<IProgressionTab, IFeature> tabToButtonCache = CacheBuilder.newBuilder().maximumSize(1024).build();
    private static final String TRANSPARENT = ProgressionInfo.BOOKPATH + "transparent.png";

    private IFeature getButtonFromTab(final IProgressionTab tab, final IBook book, final int y) {
        FeatureButton button = (FeatureButton) EnchiridionAPI.editor.getJumpPageButton(TRANSPARENT, TRANSPARENT, y + 50);
        button.update(position);
        //button.textUnhover = new FeatureText(tab.getDisplayName());

        return button;
        /*
        return EnchiridionAPI.editor.getJumpPageButton(TRANSPARENT, TRANSPARENT, y + 50);

        try {
            return tabToButtonCache.get(tab, new Callable<IFeature>() {
                @Override
                public IFeature call() throws Exception {
                    return EnchiridionAPI.editor.getJumpPageButton(TRANSPARENT, TRANSPARENT, y + 50);
                }
            });
        } catch (Exception e) {
            return null;
        } */
    }

    @Override
    public FeatureTabList copy() {
        return new FeatureTabList();
    }

    public int getCompletionAmount(IProgressionTab tab) {
        int totaltasks = tab.getCriteria().size();
        if (totaltasks == 0) return 100;
        Set<IProgressionCriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID());

        int tasksdone = 0;
        for (IProgressionCriteria criteria: completed) {
            if (tab.equals(criteria.getTab())) tasksdone++;
        }

        return (tasksdone * 100) / totaltasks;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int pos = 0;
        position.setHeight(APIHandler.getTabs().values().size() * 20);
        for (IProgressionTab tab: APIHandler.getTabs().values()) {
            EnchiridionAPI.draw.drawSplitScaledString((pos + 1) + ".  " + tab.getDisplayName(), position.getLeft(), position.getTop() + pos * 20, 100, 0xFF404040, 1F);
            EnchiridionAPI.draw.drawSplitScaledString(getCompletionAmount(tab) + "% Completed", position.getLeft() + 13, position.getTop() + 10 + pos * 20, 100, 0xFF404040, 0.75F);

            pos++;
        }
    }
}
