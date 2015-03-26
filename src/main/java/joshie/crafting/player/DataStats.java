package joshie.crafting.player;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class DataStats {
	private float speed;
	private int fallDamage;
	private int researchPoints;

	public float getSpeed() {
		return this.speed;
	}
	
	void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getFallDamagePrevention() {
		return fallDamage;
	}
	
	void setFallDamagePrevention(int fallDamage) {
		this.fallDamage = fallDamage;
	}
	
	public int getResearchPoints() {
	    return researchPoints;
	}
	
	void setResearchPoints(int points) {
	    this.researchPoints = points;
	}

	public void readFromNBT(NBTTagCompound tag) {
		this.speed = tag.getFloat("Speed");
		this.fallDamage = tag.getInteger("Fall Damage");
		this.researchPoints = tag.getInteger("Research Points");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setFloat("Speed", speed);
		tag.setInteger("Fall Damage", fallDamage);
		tag.setInteger("Research Points", researchPoints);
		return tag;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeFloat(speed);
		buf.writeInt(fallDamage);
		buf.writeInt(researchPoints);
	}

	public void fromBytes(ByteBuf buf) {
		speed = buf.readFloat();
		fallDamage = buf.readInt();
		researchPoints = buf.readInt();
	}
}
