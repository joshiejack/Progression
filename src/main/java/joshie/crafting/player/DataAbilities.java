package joshie.crafting.player;

import net.minecraft.nbt.NBTTagCompound;

public class DataAbilities {
	private float speed;

	public float getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void readFromNBT(NBTTagCompound tag) {
		this.speed = tag.getFloat("Speed");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setFloat("Speed", speed);
		return tag;
	}
}
