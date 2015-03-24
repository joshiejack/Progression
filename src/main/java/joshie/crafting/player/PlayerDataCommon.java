package joshie.crafting.player;

import joshie.crafting.CraftingMappings;
import joshie.crafting.CraftingMod;
import joshie.crafting.api.ICraftingMappings;
import joshie.crafting.api.IPlayerData;

public abstract class PlayerDataCommon implements IPlayerData {
	protected DataAbilities abilities = new DataAbilities();
	protected CraftingMappings mappings = new CraftingMappings();
	
	@Override
	public ICraftingMappings getMappings() {
		return mappings;
	}
	
	@Override
	public float getSpeed() {
		return abilities.getSpeed();
	}
	
	protected void markDirty() {
		CraftingMod.data.markDirty();
	}
}
