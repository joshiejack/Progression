package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.api.criteria.IProgressionTab;
import joshie.progression.api.criteria.IProgressionTrigger;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class Criteria implements IProgressionCriteria {
    /** All the data for this **/
    public List<IProgressionTrigger> triggers = new ArrayList();
    public List<IProgressionReward> rewards = new ArrayList();
    public List<IProgressionCriteria> prereqs = new ArrayList();
    public List<IProgressionCriteria> conflicts = new ArrayList();

    public IProgressionTab tab;
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

    public Criteria(IProgressionTab tab, String uniqueName, boolean isClientside) {
        this.tab = tab;
        this.uniqueName = uniqueName;
        this.isRepeatable = 1;
        this.tasksRequired = 1;
        this.allTasks = true;
        this.allRewards = true;
        this.rewardsGiven = 1;
        this.displayName = "New Criteria";
    }

    public void init(IProgressionCriteria[] prereqs, IProgressionCriteria[] theConflicts, String displayName, boolean isVisible, boolean mustClaim, boolean achievement, int repeatable, ItemStack icon, boolean allRequired, int tasksRequired, boolean infinite, boolean allRewards, int rewardsGiven, int x, int y) {
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

    private void addRequirements(IProgressionCriteria... prereqs) {
        this.prereqs.addAll(Arrays.asList((IProgressionCriteria[]) prereqs));
    }

    private void addConflicts(IProgressionCriteria... conflicts) {
        this.conflicts.addAll(Arrays.asList((IProgressionCriteria[]) conflicts));
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
        IProgressionCriteria other = (IProgressionCriteria) obj;
        if (uniqueName == null) {
            if (other.getUniqueName() != null) return false;
        } else if (!uniqueName.equals(other.getUniqueName())) return false;
        return true;
    }

    @Override
    public List<IProgressionTrigger> getTriggers() {
        return triggers;
    }

    @Override
    public List<IProgressionReward> getRewards() {
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
    public List<IProgressionCriteria> getConflicts() {
        return conflicts;
    }

    @Override
    public IProgressionTab getTab() {
        return tab;
    }

    @Override
    public List<IProgressionCriteria> getPreReqs() {
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

    @Override
    public void setIcon(ItemStack icon) {
        stack = icon;
    }
}
