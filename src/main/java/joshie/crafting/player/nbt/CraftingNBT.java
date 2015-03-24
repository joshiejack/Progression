package joshie.crafting.player.nbt;

import java.util.Map;

import joshie.crafting.helpers.NBTHelper.IMapHelper;
import joshie.crafting.lib.SafeStack;
import joshie.crafting.lib.SafeStack.SafeStackDamage;
import joshie.crafting.lib.SafeStack.SafeStackNBT;
import joshie.crafting.lib.SafeStack.SafeStackNBTDamage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CraftingNBT implements IMapHelper {
	private Map map;
	
	public CraftingNBT(Map map) {
		this.map = map;
	}
	
	@Override
	public Map getMap() {
		return map;
	}

	@Override
	public Object readKey(NBTTagCompound tag) {
		ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
		boolean matchDamage = tag.getBoolean("MatchDamage");
		boolean matchNBT = tag.getBoolean("MatchNBT");
		return SafeStack.newInstance(stack, matchDamage, matchNBT);
	}

	@Override
	public Object readValue(NBTTagCompound tag) {
		return (Integer) tag.getInteger("Value");
	}

	@Override
	public void writeKey(NBTTagCompound tag, Object o) {
		ItemStack key = ((SafeStack) o).original;
		boolean matchDamage = o instanceof SafeStackDamage;
		boolean matchNBT = o instanceof SafeStackNBTDamage || o instanceof SafeStackNBT;
		tag.setBoolean("MatchDamage", matchDamage);
		tag.setBoolean("MatchNBT", matchNBT);
		key.writeToNBT(tag);
	}

	@Override
	public void writeValue(NBTTagCompound tag, Object o) {
		Integer value = (Integer) o;
		tag.setInteger("Value", value);
	}
}
