package joshie.progression.api;

import java.util.List;

import net.minecraft.item.ItemStack;

/** This is just a wrapper interface
 *  Don't use it for anything. */
public interface ICriteria {
    /** Returns a list of all the triggers in this criteria **/
    public List<ITriggerType> getTriggers();
    
    /** Returns a list of all the reward in this criteria **/
    public List<IRewardType> getRewards();

    /** Returns the unique name for this criteria **/
    public String getUniqueName();
    
    public int getTasksRequired();

    public boolean getIfRequiresAllTasks();

    public boolean requiresClaiming();

    public boolean displayAchievement();

    public String getDisplayName();

    public ItemStack getIcon();

    public boolean canRepeatInfinite();

    public int getRepeatAmount();

    public List<ICriteria> getConflicts();

    public ITab getTab();

    public List<ICriteria> getPreReqs();

    public int getAmountOfRewards();

    public boolean givesAllRewards();

    public void init(ICriteria[] thePrereqs, ICriteria[] theConflicts, String display, boolean isVisible, boolean mustClaim, boolean achievement, int repeatable, ItemStack icon, boolean allRequired, int tasksRequired, boolean infinite, boolean allRewards, int rewardsGiven, int x, int y);

    public int getX();

    public int getY();

    public boolean isVisible();

    public void setVisiblity(boolean b);

    public void setCoordinates(int x, int y);

    public void addTooltip(List<String> toolTip);

    public void setIcon(ItemStack icon);
}