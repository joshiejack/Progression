package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.gui.editors.TreeEditorElement;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.util.StatCollector;

import java.util.Set;


public class FeatureTabInfo extends FeatureTabGeneric {
    public boolean total = true;
    public String totalText = "%s criteria total";
    public boolean completed = true;
    public String completedText = "[color=green]%s criteria completed";
    public boolean ready = true;
    public String readyText = "[color=blue]%s criteria ready for completion";

    public FeatureTabInfo() {}

    public FeatureTabInfo(ITab tab) {
        super(tab);
    }

    @Override
    public FeatureTabInfo copy() {
        return new FeatureTabInfo(getTab());
    }

    public String getTotalCriteria(ITab tab) {
        return StatCollector.translateToLocalFormatted(totalText, tab.getCriteria().size());
    }

    public String getCompletedCriteria(ITab tab) {
        int tasksdone = 0;
        if (tab.getCriteria().size() > 0) {
            Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID(), true);

            for (ICriteria criteria : completed) {
                if (tab.equals(criteria.getTab())) tasksdone++;
            }
        }

        return StatCollector.translateToLocalFormatted(completedText, tasksdone);
    }

    public String getCriteriaAvailable(ITab tab) {
        Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID(), true);
        int taskssone = 0;
        if (tab.getCriteria().size() > 0) {
            for (ICriteria criteria: tab.getCriteria()) {
                if (TreeEditorElement.isCriteriaCompleteable(criteria) && !completed.contains(criteria)) taskssone++;
            }
        }

        return StatCollector.translateToLocalFormatted(readyText, taskssone);
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        ITab tab = getTab();
        if (tab != null) {
            int yOffset = 0;
            if (total) {
                EnchiridionAPI.draw.drawSplitScaledString(getTotalCriteria(tab), position.getLeft(), position.getTop(), 200, 0xFF404040, 1F);
                yOffset += 8;
            }

            if (completed) {
                EnchiridionAPI.draw.drawSplitScaledString(getCompletedCriteria(tab), position.getLeft(), position.getTop() + yOffset, 200, 0xFF404040, 1F);
                yOffset += 8;
            }

            if (ready)
                EnchiridionAPI.draw.drawSplitScaledString(getCriteriaAvailable(tab), position.getLeft(), position.getTop() + yOffset, 200, 0xFF404040, 1F);
        }
    }
}
