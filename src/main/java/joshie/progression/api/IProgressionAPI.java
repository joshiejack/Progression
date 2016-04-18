package joshie.progression.api;

import joshie.progression.api.criteria.*;
import joshie.progression.api.special.IRequestItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import java.util.UUID;

/** The registry is where you addCriteria new types of rewards, triggers and
 *  conditions, and the criteria. All triggers and rewards should be registered before any criteria, and conditions before triggers */
public interface IProgressionAPI {
    /** Fires all triggers, of the type specified, Triggers should only be fired
     *  On the server side.
     *  @return     returns allow if the triggers suceeded
     *              returns deny if one of the triggers cancelled the event
     *              returns default if nothing happened **/
    public Result fireTrigger(UUID uuid, String trigger, Object... data);
    
    /** Convenience method **/
    public Result fireTrigger(EntityPlayer player, String trigger, Object... data);
    
    /** Call this when firing triggers on the server, the data objects you can use are limited to the following
     *  boolean, byte, short, int, long, double, float, String
     *  ItemStack, NBTTagCompound, Criteria, Block, Item, Entity 
     *  If you want to be able to handle your own items, send them in one of these forms
     *  Then use the ICustomDataBuilder to build your objects again from the data you passed,
     *  Keep note that */
    public void fireTriggerClientside(String trigger, Object... data);

    /** Call this on rewards that need access to an item
     *  These rewards should implement IRequestItem **/
    public void requestItem(IRequestItem reward, EntityPlayer player);
    
    /** Register an ICustomDataBuilder
     *  Keep in mind you need to pass the same string
     *  to fireTrigger for this to work correctly. **/
    public void registerCustomDataBuilder(String trigger, ICustomDataBuilder builder);

    /** Register a new crafting type **/
    public IAction registerActionType(String unlocalised);
    
    /** Register a damage source **/
    public void registerDamageSource(DamageSource source);

    /** Similiar to above, but this is whether the item can be used to perform, the action
     *  e.g. for crafting it would be whether you can use the item for crafting, or with blocks
     *  it's whether you can use x item to harvest a block.
     *  @param     the type of action
     *  @param     the stack attempting to be used
     *  @param     the player or tile tile entity  */
    public boolean canUseToPerformAction(String actionType, ItemStack stack, Object tileOrPlayer);
}
