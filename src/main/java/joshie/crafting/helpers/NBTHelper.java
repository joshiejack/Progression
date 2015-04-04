package joshie.crafting.helpers;

import java.util.Collection;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {
	public static void readTagCollection(NBTTagCompound nbt, String string, ICollectionHelper something) {
		readCollection(nbt, string, something, 10);
	}
	
	public static void readStringCollection(NBTTagCompound nbt, String string, ICollectionHelper something) {
		readCollection(nbt, string, something, 8);
	}
	
	private static void readCollection(NBTTagCompound nbt, String string, ICollectionHelper something, int num) {
		NBTTagList list = nbt.getTagList(string, num);
		for (int i = 0; i < list.tagCount(); i++) {
			Object o = something.read(list, i);
			if (o != null) {
				something.getSet().add(o);
			}
		}
	}
	
	public static NBTTagCompound writeCollection(NBTTagCompound nbt, String name, ICollectionHelper something) {
		NBTTagList list = new NBTTagList();
		for (Object s: something.getSet()) {
		    if (s == null) continue;
			list.appendTag(something.write(s));
		}
		
		nbt.setTag(name, list);
		return nbt;
	}
	
	public static void readMap(NBTTagCompound nbt, String string, IMapHelper something) {
		NBTTagList list = nbt.getTagList(string, 10);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			Object key = something.readKey(tag);
			Object data = something.readValue(tag);
			if (key == null || data == null) continue;
			something.getMap().put(key, data);
		}
	}
	
	public static NBTTagCompound writeMap(NBTTagCompound nbt, String name, IMapHelper something) {
		NBTTagList list = new NBTTagList();
		for (Object o: something.getMap().keySet()) {
			Object key = o;
			Object value = something.getMap().get(key);
	         if (key == null || value == null) continue;
			NBTTagCompound tag = new NBTTagCompound();
			something.writeKey(tag, key);
			something.writeValue(tag, value);
			list.appendTag(tag);
		}
		
		nbt.setTag(name, list);
		return nbt;
	}
	
	public static interface ICollectionHelper {
		Collection getSet();
		Object read(NBTTagList list, int i);
		NBTBase write(Object s);
	}
	
	public static interface IMapHelper {
		Map getMap();
		Object readKey(NBTTagCompound tag);
		Object readValue(NBTTagCompound tag);
		void writeKey(NBTTagCompound tag, Object o);
		void writeValue(NBTTagCompound tag, Object o);
	}
}
