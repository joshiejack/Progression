package joshie.progression.api.criteria;

import joshie.progression.api.IPlayerTeam;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public interface ICondition extends IFieldProvider {
    /** Whether this condition checks for true or false **/
    public boolean isInverted();

    /** Should true true if this condition is satisfied
     * @param team this is information about the team **/
    public boolean isSatisfied(IPlayerTeam team);

    /** This is called when the criteria is created, so you have upper access **/
    public void setTrigger(ITrigger trigger, UUID uuid);
    
    /** Return the trigger type this condition is attached to **/
    public ITrigger getTrigger();

    /** Returns an icon represenation of this condition **/
    public ItemStack getIcon();
}
