package joshie.progression.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IConditionType extends IFieldProvider {
    /** Whether this condition checks for true or false **/
    public boolean isInverted();

    /** Should true true if this condition is satisfied
     * @param world     the world object
     * @param player    the player (may be null, if the player is offline)
     * @param uuid      the uuid of the player **/
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid);

    /** This is called when the criteria is created, so you have upper access **/
    public void setTrigger(ITriggerType trigger);
    
    /** Return the trigger type this condition is attached to **/
    public ITriggerType getTrigger();
}
