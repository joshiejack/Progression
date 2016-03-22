package joshie.progression.api.criteria;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.special.IHasEventBus;
import net.minecraft.item.ItemStack;

public interface IProgressionTrigger extends IFieldProvider, IHasEventBus {
    /** Return the list this trigger is saving it's conditions to **/
    public List<IProgressionCondition> getConditions();
    
    /** Associates this reward type with the criteria
     *  Most trigger types will not need access to this. **/
    public void setCriteria(IProgressionCriteria criteria);
    
    /** Return the criteria this trigger is attached to **/
    public IProgressionCriteria getCriteria();
    
    /** Create a new instance for storing data **/
    public IProgressionTriggerData newData();
           
    /** Called to fire this trigger
     *  @return     return false if the trigger cancelled the event **/
    public boolean onFired(UUID uuid, IProgressionTriggerData triggerData, Object... data);
    
    /** Called to determine if this trigger has been completed as of yet **/
    public boolean isCompleted(IProgressionTriggerData triggerData);

    /** Returns an icon representation for this trigger **/
    public ItemStack getIcon();
}
