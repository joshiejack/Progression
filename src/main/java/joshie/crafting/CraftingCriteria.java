package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ICriteriaEditor;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITreeEditor;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.EditorCriteria;
import joshie.crafting.gui.EditorTree;
import scala.actors.threadpool.Arrays;

public class CraftingCriteria implements ICriteria {
    /** All the data for this **/
    private List<ITrigger> triggers = new ArrayList();
    private List<IReward> rewards = new ArrayList();
    private List<ICriteria> prereqs = new ArrayList();
    private List<ICriteria> conflicts = new ArrayList();
    private ITreeEditor treeEditor;
    private ICriteriaEditor criteriaEditor;
    private int isRepeatable;

    private String name;
    
    public CraftingCriteria() {
        this.treeEditor = new EditorTree(this);
        this.criteriaEditor = new EditorCriteria(this);
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    @Override
    public ICriteria setUniqueName(String unique) {
        this.name = unique;
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
    public ITreeEditor getTreeEditor() {
        return treeEditor;
    }
    
    @Override
    public ICriteriaEditor getCriteriaEditor() {
        return criteriaEditor;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CraftingCriteria other = (CraftingCriteria) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
