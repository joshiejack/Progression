package joshie.progression.player.nbt;

import java.util.Map;

import joshie.progression.api.ICriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.NBTHelper.IMapHelper;
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
		return APIHandler.getCriteriaFromName(name);
	}

	@Override
	public Object readValue(NBTTagCompound tag) {
		return (Integer)tag.getInteger("Repeated");
	}

	@Override
	public void writeKey(NBTTagCompound tag, Object o) {
		String name = ((ICriteria)o).getUniqueName();
		tag.setString("Name", name);
	}

	@Override
	public void writeValue(NBTTagCompound tag, Object o) {
		tag.setInteger("Repeated", (Integer)o);
	}
}
