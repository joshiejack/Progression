package joshie.crafting.player.nbt;

import java.util.Map;

import joshie.crafting.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;

public class KillingsNBT implements IMapHelper {
	public static final KillingsNBT INSTANCE = new KillingsNBT();
	private Map map;
	
	public KillingsNBT setMap(Map map) {
		this.map = map;
		return this;
	}

	@Override
	public Map getMap() {
		return map;
	}

	@Override
	public Object readKey(NBTTagCompound tag) {
		return tag.getString("Key");
	}

	@Override
	public Object readValue(NBTTagCompound tag) {
		return tag.getInteger("Value");
	}

	@Override
	public void writeKey(NBTTagCompound tag, Object o) {
		tag.setString("Key",  (String) o);
	}

	@Override
	public void writeValue(NBTTagCompound tag, Object o) {
		tag.setInteger("Value",  (Integer) o);
	}
}
