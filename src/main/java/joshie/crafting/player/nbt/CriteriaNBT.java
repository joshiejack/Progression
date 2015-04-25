package joshie.crafting.player.nbt;

import java.util.Map;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.Criteria;
import joshie.crafting.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;

public class CriteriaNBT implements IMapHelper {
	public static final CriteriaNBT INSTANCE = new CriteriaNBT();
	
	public Map map;
	
	public IMapHelper setMap(Map map) {
		this.map = map;
		return this;
	}

	@Override
	public Map getMap() {
		return map;
	}

	@Override
	public Object readKey(NBTTagCompound tag) {
		String name = tag.getString("Name");
		return CraftAPIRegistry.getCriteriaFromName(name);
	}

	@Override
	public Object readValue(NBTTagCompound tag) {
		return (Integer)tag.getInteger("Repeated");
	}

	@Override
	public void writeKey(NBTTagCompound tag, Object o) {
		String name = ((Criteria)o).getUniqueName();
		tag.setString("Name", name);
	}

	@Override
	public void writeValue(NBTTagCompound tag, Object o) {
		tag.setInteger("Repeated", (Integer)o);
	}
}
