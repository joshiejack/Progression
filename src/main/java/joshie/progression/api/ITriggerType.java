package joshie.progression.api;

import java.util.List;
import java.util.UUID;

public interface ITriggerType extends IFieldProvider, IHasEventBus {
    /** Return the list this trigger is saving it's conditions to **/
    public List<IConditionType> getConditions();
    
    /** Associates this reward type with the criteria
     *  Most trigger types will not need access to this. **/
    public void setCriteria(ICriteria criteria);
    
    /** Return the criteria this trigger is attached to **/
    public ICriteria getCriteria();
    
    /** Create a new instance for storing data **/
    public ITriggerData newData();
           
    /** Called to fire this trigger
     *  @return     return false if the trigger cancelled the event **/
    public boolean onFired(UUID uuid, ITriggerData triggerData, Object... data);
    
    /** Called to determine if this trigger has been completed as of yet **/
    public boolean isCompleted(ITriggerData triggerData);
}
