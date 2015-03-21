package joshie.crafting.data.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.tech.ITechnology;
import joshie.crafting.helpers.NBTHelper;
import joshie.crafting.helpers.NBTHelper.ICollectionHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class DataTechnology implements ICollectionHelper {
	public Set<ITechnology> technologies = new HashSet();
	
	public boolean hasUnlocked(ITechnology technology) {
		return technologies.contains(technology);
	}

	public boolean unlockResearch(ITechnology technology) {
		for (ITechnology tech: technology.getConflicts()) {
			if (tech.getConflicts().contains(technology)) return false;
		}
		
		for (ITechnology tech: technology.getPrereqs()) {
			if (!technologies.contains(tech)) return false; 
		}
		
		return technologies.add(technology);
	}
	
	@Override
	public Collection getSet() {
		return technologies;
	}

	@Override
	public NBTBase write(Object s) {
		return new NBTTagString(((ITechnology)s).getName());
	}
	
	@Override
	public Object read(NBTTagList list, int i) {
		String name = list.getStringTagAt(i);
		return CraftingAPI.tech.getTechnologyFromName(name);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		NBTHelper.readCollection(nbt, "TechList", this);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return NBTHelper.writeCollection(nbt, "TechList", this);
	}
}
