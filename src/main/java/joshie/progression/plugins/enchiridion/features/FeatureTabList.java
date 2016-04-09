package joshie.progression.plugins.enchiridion.features;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.data.book.Page;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.enchiridion.helpers.JumpHelper;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.ProgressionInfo;

import java.util.Set;

public class FeatureTabList extends FeatureProgression {
    public FeatureTabList() {}

    private transient Cache<ITab, IFeature> tabToButtonCache = CacheBuilder.newBuilder().maximumSize(1024).build();
    private static final String TRANSPARENT = ProgressionInfo.BOOKPATH + "transparent.png";

    private IFeature getButtonFromTab(final ITab tab, final IBook book, final int y) {
        FeatureButton button = (FeatureButton) EnchiridionAPI.editor.getJumpPageButton(TRANSPARENT, TRANSPARENT, y + 50);
        button.update(position);
        //button.textUnhover = new FeatureText(tab.getLocalisedName());

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

    private IPage getPageByNumber(IBook book, int number) {
        for (IPage page: book.getPages()) {
            if (page.getPageNumber() == number) {
                return page;
            }
        }

        return null;
    }

    @Override
    public void performClick(int mouseX, int mouseY) {
        int pos = 0;
        for (ITab tab: APIHandler.getCache(true).getSortedTabs()) {
            if (mouseX >= position.getLeft() && mouseX <= position.getRight()) {
                if (mouseY >= position.getTop() + (pos * 20) && mouseY <= position.getTop() + 8 + (pos * 20)) {
                    int number = 20 + pos;
                    IPage page = JumpHelper.getPageByNumber(EnchiridionAPI.book.getBook(), number);
                    if (page == null) {
                        page = DefaultHelper.addDefaults(GuiBook.INSTANCE.getBook(), new Page(number).setBook(GuiBook.INSTANCE.getBook()));
                        EnchiridionAPI.book.getBook().addPage(page);
                        JumpHelper.jumpToPageByNumber(number);
                    }
                }
            }

            pos++;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int pos = 0;
        position.setHeight(APIHandler.getCache(true).getTabs().values().size() * 20);
        for (ITab tab: APIHandler.getCache(true).getSortedTabs()) {
            int color = 0xFF404040;
            if (mouseX >= position.getLeft() && mouseX <= position.getRight()) {
                if (mouseY>= position.getTop() + (pos * 20) && mouseY <= position.getTop() + 8 + (pos * 20)) {
                    color = 0xFFCCCCCC;
                }
            }

            EnchiridionAPI.draw.drawSplitScaledString((pos + 1) + ".", position.getLeft(), position.getTop() + pos * 20, 200, color, 1F);
            EnchiridionAPI.draw.drawSplitScaledString(tab.getDisplayName(), position.getLeft() + 18, position.getTop() + pos * 20, 200, color, 1F);
            EnchiridionAPI.draw.drawSplitScaledString(getCompletionAmount(tab) + "% Completed", position.getLeft() + 13, position.getTop() + 10 + pos * 20, 100, 0xFF404040, 0.75F);

            pos++;
        }
    }
}
