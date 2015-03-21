package joshie.crafting.data.player;

import java.util.HashSet;
import java.util.Set;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.tech.ITechnology;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class DataTechnology {
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
	
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList techList = nbt.getTagList("TechList", 10);
		for (int j = 0; j < techList.tagCount(); j++) {
			String name = techList.getStringTagAt(j);
			ITechnology tech = CraftingAPI.tech.getTechnologyFromName(name);
			if (tech != null) {
				technologies.add(tech);
			}
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList techList = new NBTTagList();
		for (ITechnology tech: technologies) {
			NBTTagString name = new NBTTagString(tech.getName());
			techList.appendTag(name);
		}
			
		nbt.setTag("TechList", techList);
		return nbt;
	}
}
