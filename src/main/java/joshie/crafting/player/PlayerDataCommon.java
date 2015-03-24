package joshie.crafting.player;

import java.util.HashMap;

import joshie.crafting.CraftingMappings;
import joshie.crafting.CraftingMod;
import joshie.crafting.api.ICraftingMappings;
import joshie.crafting.api.IPlayerData;
import joshie.crafting.lib.SafeStack;

public abstract class PlayerDataCommon implements IPlayerData {
	protected DataAbilities abilities = new DataAbilities();
	protected CraftingMappings mappings = new CraftingMappings();
	protected HashMap<SafeStack, Integer> crafts = new HashMap();
	
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
