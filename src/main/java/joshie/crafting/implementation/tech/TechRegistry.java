package joshie.crafting.implementation.tech;

import java.util.Collection;
import java.util.HashMap;

import joshie.crafting.api.tech.ITechRegistry;
import joshie.crafting.api.tech.ITechnology;
import joshie.crafting.helpers.DataHelper;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.collect.Maps;

public class TechRegistry implements ITechRegistry {
	private static HashMap<String, ITechnology> technologies = Maps.newHashMap();
	
	@Override
	public ITechnology getTechnologyFromName(String name) {
		return technologies.get(name);
	}

	@Override
	public void register(ITechnology tech) {
		technologies.put(tech.getName(), tech);
	}

	@Override
	public Collection<ITechnology> getTechnologies() {
		return technologies.values();
	}

	@Override
	public void research(EntityPlayer player, ITechnology tech) {
		DataHelper.getData().unlockResearch(PlayerHelper.getUUIDForPlayer(player), tech);
	}
}
