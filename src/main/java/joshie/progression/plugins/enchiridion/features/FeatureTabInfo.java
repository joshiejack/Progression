package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import joshie.progression.gui.editors.TreeEditorElement;
import joshie.progression.helpers.PlayerHelper;

import java.util.Set;

import static net.minecraft.util.EnumChatFormatting.*;


public class FeatureTabInfo extends FeatureTabGeneric {
    public FeatureTabInfo() {}

    public FeatureTabInfo(ITab tab) {
        super(tab);
    }

    @Override
    public FeatureTabInfo copy() {
        return new FeatureTabInfo(tab);
    }

    public String getTotalCriteria() {
        return tab.getCriteria().size() + " criteria total";
    }

    public String getUnlockedCriteria() {
        return AQUA + "";
    }

    public String getCompletedCriteria() {
        int tasksdone = 0;
        if (tab.getCriteria().size() > 0) {
            Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID(), true);

            for (ICriteria criteria : completed) {
                if (tab.equals(criteria.getTab())) tasksdone++;
            }
        }

        return DARK_GREEN + "" + tasksdone + " criteria completed";
    }

    public String getCriteriaAvailable() {
        Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID(), true);
        int taskssone = 0;
        if (tab.getCriteria().size() > 0) {
            for (ICriteria criteria: tab.getCriteria()) {
                if (TreeEditorElement.isCriteriaCompleteable(criteria) && !completed.contains(criteria)) taskssone++;
            }
        }

        return BLUE + "" + taskssone + " criteria ready for completion";
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        EnchiridionAPI.draw.drawSplitScaledString(getTotalCriteria(), position.getLeft(), position.getTop(), 200, 0xFF404040, 1F);
        EnchiridionAPI.draw.drawSplitScaledString(getCompletedCriteria(), position.getLeft(), position.getTop() + 8, 200, 0xFF404040, 1F);
        EnchiridionAPI.draw.drawSplitScaledString(getCriteriaAvailable(), position.getLeft(), position.getTop() + 16, 200, 0xFF404040, 1F);
    }
}
