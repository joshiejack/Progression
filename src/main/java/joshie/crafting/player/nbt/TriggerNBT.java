package joshie.crafting.player.nbt;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class TriggerNBT extends AbstractUniqueNBT {
	public static final TriggerNBT INSTANCE = new TriggerNBT();
	
	@Override
	public NBTBase write(Object s) {
		return new NBTTagString(((ITrigger)s).getUniqueName());
	}

	@Override
	public Object read(NBTTagList list, int i) {
		String name = list.getStringTagAt(i);
		return CraftingAPI.registry.getTrigger(null, name, null);
	}
}
