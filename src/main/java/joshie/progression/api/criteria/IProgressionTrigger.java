package joshie.progression.api.criteria;

import joshie.progression.api.special.IHasEventBus;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IProgressionTrigger extends ICanHaveEvents, IHasEventBus {
    /** Return the list this trigger is saving it's conditions to **/
    public List<IProgressionCondition> getConditions();
    
    /** Associates this trigger type with the criteria, and marks the uuid
     *  Most trigger types will not need access to this. **/
    public void setCriteria(IProgressionCriteria criteria, UUID uuid);
    
    /** Return the criteria this trigger is attached to **/
    public IProgressionCriteria getCriteria();
           
    /** Called to fire this trigger
     *  @return     return false if the trigger cancelled the event **/
    public boolean onFired(UUID uuid, Object... data);
    
    /** Called to determine if this trigger has been completed as of yet **/
    public boolean isCompleted();

    /** Returns an icon representation for this trigger **/
    public ItemStack getIcon();

    /** This should create an exact copy of this object type,
     *  This is used so that players have their own unique, versions,
     *  for storing the data. If you don't return,
     *  an exact copy, then everything will not work correctly */
    public IProgressionTrigger copy();
}
