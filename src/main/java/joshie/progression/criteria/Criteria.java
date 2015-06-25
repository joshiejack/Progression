package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import joshie.progression.api.ICriteria;
import joshie.progression.gui.EditorTree;
import joshie.progression.gui.ViewerCriteria;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Criteria implements ICriteria {
    /** All the data for this **/
    public List<Trigger> triggers = new ArrayList();
    public List<Reward> rewards = new ArrayList();
    public List<Criteria> prereqs = new ArrayList();
    public List<Criteria> conflicts = new ArrayList();
    public EditorTree treeEditor;
    public ViewerCriteria criteriaViewer;

    public Tab tab;
    public String uniqueName;
    public int isRepeatable = 1;
    public boolean infinite = false;
    public String displayName = "New Criteria";
    public boolean isVisible = true;
    public boolean mustClaim = false;
    public boolean achievement = true;
    public ItemStack stack = new ItemStack(Blocks.stone);

    public Criteria(Tab tab, String uniqueName, boolean isClientside) {
        this.tab = tab;
        this.uniqueName = uniqueName;

        if (isClientside) {
            this.treeEditor = new EditorTree(this);
            this.criteriaViewer = new ViewerCriteria(this);
        }
    }

    public void init(Criteria[] prereqs, Criteria[] theConflicts, String displayName, boolean isVisible, boolean mustClaim, boolean achievement, int repeatable, ItemStack icon) {
        this.displayName = displayName;
        this.isVisible = isVisible;
        this.mustClaim = mustClaim;
        this.achievement = achievement;
        this.isRepeatable = repeatable;
        this.stack = icon;
        addRequirements(prereqs);
        addConflicts(theConflicts);
    }

    public void addTriggers(Trigger... triggers) {
        this.triggers.addAll(Arrays.asList((Trigger[]) triggers));
        this.triggers.removeAll(Collections.singleton(null));
    }

    public void addRewards(Reward... rewards) {
        this.rewards.addAll(Arrays.asList((Reward[]) rewards));
        this.rewards.removeAll(Collections.singleton(null));
        for (Reward reward : rewards) {
            if (reward != null) {
                reward.getType().onAdded();
            }
        }
    }

    public void addRequirements(Criteria... prereqs) {
        this.prereqs.addAll(Arrays.asList((Criteria[]) prereqs));
    }

    public void addConflicts(Criteria... conflicts) {
        this.conflicts.addAll(Arrays.asList((Criteria[]) conflicts));
    }

    public void addTooltip(List<String> toolTip) {
        toolTip.add("Requires: " + displayName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uniqueName == null) ? 0 : uniqueName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Criteria other = (Criteria) obj;
        if (uniqueName == null) {
            if (other.uniqueName != null) return false;
        } else if (!uniqueName.equals(other.uniqueName)) return false;
        return true;
    }
}
