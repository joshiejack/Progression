package joshie.progression.criteria;

import joshie.progression.api.criteria.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Criteria implements ICriteria {
    /** All the data for this **/
    public List<ITriggerProvider> triggers = new ArrayList();
    public List<IRewardProvider> rewards = new ArrayList();
    public List<ICriteria> prereqs = new ArrayList();
    public List<ICriteria> conflicts = new ArrayList();

    public ITab tab;
    public UUID uuid;
    public int isRepeatable = 1;
    public boolean infinite = false;
    public int tasksRequired = 1;
    public boolean allTasks = true;
    public int rewardsGiven;
    public boolean allRewards = true;
    public String displayName = "New Criteria";
    public boolean isVisible = true;
    public boolean achievement = true;
    public ItemStack stack = new ItemStack(Blocks.stone);
    public int x, y;

    public Criteria(ITab tab, UUID uuid) {
        this.tab = tab;
        this.uuid = uuid;
        this.isRepeatable = 1;
        this.tasksRequired = 1;
        this.allTasks = true;
        this.allRewards = true;
        this.rewardsGiven = 1;
        this.displayName = "New Criteria";
    }

    public void init(ICriteria[] prereqs, ICriteria[] theConflicts, String displayName, boolean isVisible, boolean achievement, int repeatable, ItemStack icon, boolean allRequired, int tasksRequired, boolean infinite, boolean allRewards, int rewardsGiven, int x, int y) {
        this.displayName = displayName;
        this.isVisible = isVisible;
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

    private void addRequirements(ICriteria... prereqs) {
        this.prereqs.addAll(Arrays.asList((ICriteria[]) prereqs));
    }

    private void addConflicts(ICriteria... conflicts) {
        this.conflicts.addAll(Arrays.asList((ICriteria[]) conflicts));
    }

    public void addTooltip(List<String> toolTip) {
        toolTip.add("Requires: " + displayName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Criteria criteria = (Criteria) o;
        return uuid != null ? uuid.equals(criteria.uuid) : criteria.uuid == null;

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public List<ITriggerProvider> getTriggers() {
        return triggers;
    }

    @Override
    public List<IRewardProvider> getRewards() {
        return rewards;
    }

    @Override
    public UUID getUniqueID() {
        return uuid;
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
    public boolean displayAchievement() {
        return achievement;
    }

    @Override
    public String getLocalisedName() {
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

    @Override
    public void setIcon(ItemStack icon) {
        stack = icon;
    }
}
