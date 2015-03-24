package joshie.crafting.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ICondition extends IConditionType, IHasUniqueName {
	/** Whether this condition is satisfied for this player
	 *  Please keep in mind that the player can be offline,
	 *  IF they are offline, the world that is passed is ALWAYS
	 *  the OVERWORLD.
	 *  This is also only called serverside.**/
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid);
}
