package joshie.crafting.lib;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SafeStack {
	public String item;
	
	protected SafeStack(ItemStack stack) {
		this.item = Item.itemRegistry.getNameForObject(stack.getItem());
	}

	public static SafeStack[] allInstances(ItemStack stack) {
		SafeStack basic = new SafeStack(stack);
		SafeStack damage = new SafeStackDamage(stack);
		SafeStack nbt = new SafeStackNBT(stack);
		SafeStack both = new SafeStackNBTDamage(stack);
		return new SafeStack[] { basic, damage, nbt, both };
	}

	public static SafeStack newInstance(ItemStack stack, boolean matchDamage, boolean matchNBT) {
		if (matchNBT && matchDamage) {
			return new SafeStackNBTDamage(stack);
		} else if (matchNBT) {
			return new SafeStackNBT(stack);
		} else if (matchDamage) {
			return new SafeStackDamage(stack);
		} else return new SafeStack(stack);
	}
	
	public static class SafeStackDamage extends SafeStack {
		public int damage;
		
		protected SafeStackDamage(ItemStack stack) {
			super(stack);
			this.damage = stack.getItemDamage();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + damage;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			SafeStackDamage other = (SafeStackDamage) obj;
			if (damage != other.damage)
				return false;
			return true;
		}
	}
	
	public static class SafeStackNBT extends SafeStack {
		public NBTTagCompound tag;
		
		protected SafeStackNBT(ItemStack stack) {
			super(stack);
			this.tag = stack.getTagCompound();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((tag == null) ? 0 : tag.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			SafeStackNBT other = (SafeStackNBT) obj;
			if (tag == null) {
				if (other.tag != null)
					return false;
			} else if (!tag.equals(other.tag))
				return false;
			return true;
		}
	}
	
	public static class SafeStackNBTDamage extends SafeStackDamage {
		public NBTTagCompound tag;
		
		protected SafeStackNBTDamage(ItemStack stack) {
			super(stack);
			this.tag = stack.getTagCompound();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((tag == null) ? 0 : tag.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			SafeStackNBTDamage other = (SafeStackNBTDamage) obj;
			if (tag == null) {
				if (other.tag != null)
					return false;
			} else if (!tag.equals(other.tag))
				return false;
			return true;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}
}
