package joshie.crafting.player.nbt;

import joshie.crafting.api.CraftingAPI;
import net.minecraft.nbt.NBTTagList;

public class ConditionNBT extends AbstractUniqueNBT {
	public static final ConditionNBT INSTANCE = new ConditionNBT();

	@Override
	public Object read(NBTTagList list, int i) {
		String name = list.getStringTagAt(i);
		return CraftingAPI.registry.getConditionFromName(name);
	}
}
