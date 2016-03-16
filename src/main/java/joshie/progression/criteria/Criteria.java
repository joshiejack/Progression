package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import joshie.progression.api.ICriteria;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITab;
import joshie.progression.api.ITriggerType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class Criteria implements ICriteria {
    /** All the data for this **/
    public List<ITriggerType> triggers = new ArrayList();
    public List<IRewardType> rewards = new ArrayList();
    public List<ICriteria> prereqs = new ArrayList();
    public List<ICriteria> conflicts = new ArrayList();

    public ITab tab;
    public String uniqueName;
    public int isRepeatable = 1;
    public boolean infinite = false;
    public int tasksRequired = 1;
    public boolean allTasks = true;
    public int rewardsGiven;
    public boolean allRewards = true;
    public String displayName = "New Criteria";
    public boolean isVisible = true;
    public boolean mustClaim = false;
    public boolean achievement = true;
    public ItemStack stack = new ItemStack(Blocks.stone);
    public int x, y;

    public Criteria(ITab tab, String uniqueName, boolean isClientside) {
        this.tab = tab;
        this.uniqueName = uniqueName;
        this.isRepeatable = 1;
        this.tasksRequired = 1;
        this.allTasks = true;
        this.allRewards = true;
        this.rewardsGiven = 1;
    }

    public void init(ICriteria[] prereqs, ICriteria[] theConflicts, String displayName, boolean isVisible, boolean mustClaim, boolean achievement, int repeatable, ItemStack icon, boolean allRequired, int tasksRequired, boolean infinite, boolean allRewards, int rewardsGiven, int x, int y) {
        this.displayName = displayName;
        this.isVisible = isVisible;
        this.mustClaim = mustClaim;
        this.achievement = achievement;
        this.isRepeatable = repeatable;
        this.stack = icon;
        this.tasksRequired = tasksRequired;
        this.allTasks = allRequired;
        this.infinite = infinite;
        this.allRewards = allRewards;
        this.rewardsGiven = rewardsGiven;
        this.x = x;
        this.y = y;
        
        addRequirements(prereqs);
        addConflicts(theConflicts);
    }

    public void addTriggers(ITriggerType... triggers) {
        this.triggers.addAll(Arrays.asList((ITriggerType[]) triggers));
        this.triggers.removeAll(Collections.singleton(null));
    }

    public void addRewards(IRewardType... rewards) {
        this.rewards.addAll(Arrays.asList((IRewardType[]) rewards));
        this.rewards.removeAll(Collections.singleton(null));
        for (IRewardType reward : rewards) {
            if (reward != null) {
                reward.onAdded();
            }
        }
    }

    public void addRequirements(ICriteria... prereqs) {
        this.prereqs.addAll(Arrays.asList((ICriteria[]) prereqs));
    }

    public void addConflicts(ICriteria... conflicts) {
        this.conflicts.addAll(Arrays.asList((ICriteria[]) conflicts));
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
        ICriteria other = (ICriteria) obj;
        if (uniqueName == null) {
            if (other.getUniqueName() != null) return false;
        } else if (!uniqueName.equals(other.getUniqueName())) return false;
        return true;
    }

    @Override
    public List<ITriggerType> getTriggers() {
        return triggers;
    }

    @Override
    public List<IRewardType> getRewards() {
        return rewards;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    @Override
    public int getTasksRequired() {
        return tasksRequired;
    }

    @Override
    public boolean getIfRequiresAllTasks() {
        return allTasks;
    }

    @Override
    public boolean requiresClaiming() {
        return mustClaim;
    }

    @Override
    public boolean displayAchievement() {
        return achievement;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public boolean canRepeatInfinite() {
        return infinite;
    }

    @Override
    public int getRepeatAmount() {
        return isRepeatable;
    }

    @Override
    public List<ICriteria> getConflicts() {
        return conflicts;
    }

    @Override
    public ITab getTab() {
        return tab;
    }

    @Override
    public List<ICriteria> getPreReqs() {
        return prereqs;
    }

    @Override
    public int getAmountOfRewards() {
        return rewardsGiven;
    }

    @Override
    public boolean givesAllRewards() {
        return allRewards;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisiblity(boolean b) {
        this.isVisible = b;
    }

    @Override
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
