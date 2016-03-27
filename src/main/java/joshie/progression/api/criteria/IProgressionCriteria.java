package joshie.progression.api.criteria;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

/** This is just a wrapper interface
 *  Don't use it for anything. */
public interface IProgressionCriteria {
    /** Returns a list of all the triggers in this criteria **/
    public List<IProgressionTrigger> getTriggers();
    
    /** Returns a list of all the reward in this criteria **/
    public List<IProgressionReward> getRewards();

    /** Returns the unique id for this criteria **/
    public UUID getUniqueID();
    
    public int getTasksRequired();

    public boolean getIfRequiresAllTasks();

    public boolean displayAchievement();

    public String getDisplayName();

    public ItemStack getIcon();

    public boolean canRepeatInfinite();

    public int getRepeatAmount();

    public List<IProgressionCriteria> getConflicts();

    public IProgressionTab getTab();

    public List<IProgressionCriteria> getPreReqs();

    public int getAmountOfRewards();

    public boolean givesAllRewards();

    public void init(IProgressionCriteria[] thePrereqs, IProgressionCriteria[] theConflicts, String display, boolean isVisible, boolean achievement, int repeatable, ItemStack icon, boolean allRequired, int tasksRequired, boolean infinite, boolean allRewards, int rewardsGiven, int x, int y);

    public int getX();

    public int getY();

    public boolean isVisible();

    public void setVisiblity(boolean b);

    public void setCoordinates(int x, int y);

    public void addTooltip(List<String> toolTip);

    public void setIcon(ItemStack icon);
}