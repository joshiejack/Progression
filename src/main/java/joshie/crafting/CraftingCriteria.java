package joshie.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ICriteriaEditor;
import joshie.crafting.api.ICriteriaViewer;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITab;
import joshie.crafting.api.ITreeEditor;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.EditorCriteria;
import joshie.crafting.gui.EditorTree;
import joshie.crafting.gui.ViewerCriteria;

public class CraftingCriteria implements ICriteria {
    /** All the data for this **/
    private List<ITrigger> triggers = new ArrayList();
    private List<IReward> rewards = new ArrayList();
    private List<ICriteria> prereqs = new ArrayList();
    private List<ICriteria> conflicts = new ArrayList();
    private ITreeEditor treeEditor;
    private ICriteriaEditor criteriaEditor;
    private ICriteriaViewer criteriaViewer;
    private int isRepeatable = 1;
    private String uniqueName;
    private String displayName;
    private boolean isVisible;
    private ITab tab;
    
    public CraftingCriteria() {
        this.treeEditor = new EditorTree(this);
        this.criteriaEditor = new EditorCriteria(this);
        this.criteriaViewer = new ViewerCriteria(this);
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    @Override
    public ICriteria setUniqueName(String unique) {
        this.uniqueName = unique;
        return this;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ICriteria setDisplayName(String display) {
        this.displayName = display;
        return this;
    }

    @Override
    public ICriteria addTriggers(ITrigger... triggers) {
        this.triggers.addAll(Arrays.asList((ITrigger[]) triggers));
        return this;
    }

    @Override
    public ICriteria addRewards(IReward... rewards) {
        this.rewards.addAll(Arrays.asList((IReward[]) rewards));
        for (IReward reward : rewards) {
            reward.onAdded();
        }

        return this;
    }

    @Override
    public ICriteria addRequirements(ICriteria... prereqs) {
        this.prereqs.addAll(Arrays.asList((ICriteria[]) prereqs));
        return this;
    }

    @Override
    public ICriteria addConflicts(ICriteria... conflicts) {
        this.conflicts.addAll(Arrays.asList((ICriteria[]) conflicts));
        return this;
    }

    @Override
    public ICriteria setRepeatAmount(int amount) {
        this.isRepeatable = amount;
        return this;
    }

    @Override
    public ICriteria setVisibility(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    @Override
    public ICriteria setTab(ITab tab) {
        this.tab = tab;
        return this;
    }

    @Override
    public List<ITrigger> getTriggers() {
        return triggers;
    }

    @Override
    public List<IReward> getRewards() {
        return rewards;
    }

    @Override
    public List<ICriteria> getRequirements() {
        return prereqs;
    }

    @Override
    public List<ICriteria> getConflicts() {
        return conflicts;
    }

    @Override
    public int getRepeatAmount() {
        return isRepeatable;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public ITab getTabID() {
        return tab;
    }

    @Override
    public ITreeEditor getTreeEditor() {
        return treeEditor;
    }
    
    @Override
    public ICriteriaEditor getCriteriaEditor() {
        return criteriaEditor;
    }
    
    @Override
    public ICriteriaViewer getCriteriaViewer() {
        return criteriaViewer;
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
        CraftingCriteria other = (CraftingCriteria) obj;
        if (uniqueName == null) {
            if (other.uniqueName != null) return false;
        } else if (!uniqueName.equals(other.uniqueName)) return false;
        return true;
    }

    @Override
    public void addTooltip(List<String> toolTip) {
        toolTip.add("Requires: " + getDisplayName());
    }
}
