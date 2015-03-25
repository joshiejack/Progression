package joshie.crafting.player.nbt;

import java.util.Map;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;

public class ConditionNBT implements IMapHelper {
	public static final ConditionNBT INSTANCE = new ConditionNBT();
	
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
		return CraftingAPI.registry.getCriteriaFromName(name);
	}

	@Override
	public Object readValue(NBTTagCompound tag) {
		return (Integer)tag.getInteger("Repeated");
	}

	@Override
	public void writeKey(NBTTagCompound tag, Object o) {
		String name = ((ICriteria)o).getUniqueName();
		tag.setString("name", name);
	}

	@Override
	public void writeValue(NBTTagCompound tag, Object o) {
		tag.setInteger("Repeated", (Integer)o);
	}
}
