package joshie.crafting.player;

import joshie.crafting.CraftingMappings;
import joshie.crafting.CraftingMod;

public abstract class PlayerDataCommon {
	protected DataStats abilities = new DataStats();
	protected CraftingMappings mappings = new CraftingMappings();
	
	public CraftingMappings getMappings() {
		return mappings;
	}
	
	public DataStats getAbilities() {
		return abilities;
	}
	
	protected void markDirty() {
		CraftingMod.data.markDirty();
	}
}
