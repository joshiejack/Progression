package joshie.crafting.player.nbt;

import joshie.crafting.api.CraftingAPI;
import net.minecraft.nbt.NBTTagList;

public class ResearchNBT extends AbstractUniqueNBT {
	public static final ResearchNBT INSTANCE = new ResearchNBT();

	@Override
	public Object read(NBTTagList list, int i) {
		String name = list.getStringTagAt(i);
		return CraftingAPI.registry.getResearchFromName(name);
	}
}
