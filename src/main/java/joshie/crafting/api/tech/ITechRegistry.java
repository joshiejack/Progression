package joshie.crafting.api.tech;

import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;


public interface ITechRegistry {
	/** Returns the technology from the name **/
	public ITechnology getTechnologyFromName(String name);

	/** Registers the technology **/
	public void register(ITechnology tech);

	/** Returns a list of all registered techs **/
	public Collection<ITechnology> getTechnologies();

	/** Adds this research to the players data **/
	public void research(EntityPlayer player, ITechnology tech);
}
