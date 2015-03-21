package joshie.crafting.lib;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SafeStack {
	public String item;
    public int damage;
	
	private SafeStack(ItemStack stack) {
		this.item = Item.itemRegistry.getNameForObject(stack.getItem());
        this.damage = stack.getItemDamage();
	}

	public static SafeStack newInstance(ItemStack stack) {
		return new SafeStack(stack);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + damage;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SafeStack other = (SafeStack) obj;
		if (damage != other.damage)
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}
}
