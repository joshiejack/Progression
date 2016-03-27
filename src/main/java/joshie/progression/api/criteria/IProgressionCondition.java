package joshie.progression.api.criteria;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.UUID;

public interface IProgressionCondition extends IFieldProvider {
    /** Whether this condition checks for true or false **/
    public boolean isInverted();

    /** Should true true if this condition is satisfied
     * @param world     the world object
     * @param player    the player (may be null, if the player is offline)
     * @param uuid      the uuid of the player **/
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid);

    /** This is called when the criteria is created, so you have upper access **/
    public void setTrigger(IProgressionTrigger trigger, UUID uuid);
    
    /** Return the trigger type this condition is attached to **/
    public IProgressionTrigger getTrigger();

    /** Returns an icon represenation of this condition **/
    public ItemStack getIcon();
}
