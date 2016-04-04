package joshie.progression.api.criteria;

import joshie.progression.api.special.IHasEventBus;

import java.util.UUID;

public interface ITrigger extends IRule<ITriggerProvider>, IHasEventBus {
    /** This should create an exact copy of this object type,
     *  This is used so that players have their own unique, versions,
     *  for storing the data. If you don't return,
     *  an exact copy, then everything will not work correctly */
    public ITrigger copy();

    /** Called to fire this trigger
     *  @return     return false if the trigger cancelled the event **/
    public boolean onFired(UUID uuid, Object... data);
    
    /** Called to determine if this trigger has been completed as of yet **/
    public boolean isCompleted();

    /** Returns the percentage completion **/
    public int getPercentage();
}
