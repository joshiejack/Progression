package joshie.crafting.player;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class DataAbilities {
	private float speed;
	private int fallDamage;

	public float getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getFallDamagePrevention() {
		return fallDamage;
	}
	
	public void setFallDamagePrevention(int fallDamage) {
		this.fallDamage = fallDamage;
	}

	public void readFromNBT(NBTTagCompound tag) {
		this.speed = tag.getFloat("Speed");
		this.fallDamage = tag.getInteger("Fall Damage");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setFloat("Speed", speed);
		tag.setInteger("Fall Damage", fallDamage);
		return tag;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeFloat(speed);
		buf.writeInt(fallDamage);
	}

	public void fromBytes(ByteBuf buf) {
		speed = buf.readFloat();
		fallDamage = buf.readInt();
	}
}
