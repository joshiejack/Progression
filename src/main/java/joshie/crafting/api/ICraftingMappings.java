package joshie.crafting.api;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;


public interface ICraftingMappings {
	/** Fires all triggers of the specifed type, including additional data **/
	public boolean fireAllTriggers(String type, Object... data);

	/** Called whenever a server starts, or a player joins **/
	public void remap();

	public void markCriteriaAsCompleted(boolean overwrite, ICriteria... conditions);
	public void markTriggerAsCompleted(boolean overwrite, ITrigger... researches);

	public Set<ICriteria> getCompletedCriteria();
	public Set<ITrigger> getCompletedTriggers();

	public void syncToClient(EntityPlayerMP player);
}
