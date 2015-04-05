package joshie.crafting.api;

import java.util.List;

/** Conditions are the classes that are created by the users
 *  In their config files. Conditions can have unlimited triggers
 *  as well as unlimited rewards. The conditions are checked, everytime
 *  that any trigger is fired */
public interface ICriteria extends IHasUniqueName {
	/** Adds triggers to this condition **/
	public ICriteria addTriggers(ITrigger... triggers);

	/** Adds rewards to this condition **/
	public ICriteria addRewards(IReward... rewards);

	/** Adds Prereqs to this condition **/
	public ICriteria addRequirements(ICriteria... prereqs);
	
	/** Adds conflicts to this condition **/
	public ICriteria addConflicts(ICriteria... prereqs);

	/** Marks the condition as repeatable **/
	public ICriteria setRepeatAmount(int amount);
    
    /** Sets the display name **/
    public ICriteria setDisplayName(String name);
    
    /** Set the visibility **/
    public ICriteria setVisibility(boolean isVisible);
    
    /** Assigns the master tab for this critera **/
    public ICriteria setTab(ITab tab);

	/** Returns all the triggers that this condition needs to be met **/
	public List<ITrigger> getTriggers();

	public List<IReward> getRewards();

	/** Returns a list of all the requirements **/
	public List<ICriteria> getRequirements();
	
	/** Returns a list of all the conflicts **/
	public List<ICriteria> getConflicts();
	
	/** Returns if the criteria is repeatable **/
	public int getRepeatAmount();
	   
    /** Returns the display name **/
    public String getDisplayName();
	    
    /** Return the instance of the editor for this criteria, when in tree edit mode **/
    public ITreeEditor getTreeEditor();

    /** Whether this criteria is visible before it unlocks ***/
    public boolean isVisible();

    /** Returns the master tab associated with this criteria **/
    public ITab getTabID();

    public void addTooltip(List<String> toolTip);

    public ICriteriaViewer getCriteriaViewer();
}
