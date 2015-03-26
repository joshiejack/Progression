package joshie.crafting.player;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import joshie.crafting.helpers.NBTHelper;
import joshie.crafting.player.nbt.PointsNBT;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;

public class DataStats {
    private HashMap<String, Integer> points = new HashMap();
	private float speed;
	private int fallDamage;

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
	
	public int getPoints(String name) {
	    int amount = 0;
	    if (points.containsKey(name)) {
	        amount = points.get(name);
	    } else points.put(name, amount);
	    
	    return amount;
	}
	
	void setResearchPoints(String name, int points) {
	    this.points.put(name, points);
	}

	public void readFromNBT(NBTTagCompound tag) {
		this.speed = tag.getFloat("Speed");
		this.fallDamage = tag.getInteger("Fall Damage");
		NBTHelper.readMap(tag, "Points", PointsNBT.INSTANCE.setMap(points));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setFloat("Speed", speed);
		tag.setInteger("Fall Damage", fallDamage);
		NBTHelper.writeMap(tag, "Points", PointsNBT.INSTANCE.setMap(points));
		return tag;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeFloat(speed);
		buf.writeInt(fallDamage);
		buf.writeInt(points.size());
		for (String key: points.keySet()) {
		    buf.writeInt(points.get(key));
		    ByteBufUtils.writeUTF8String(buf, key);
		}
	}

	public void fromBytes(ByteBuf buf) {
		speed = buf.readFloat();
		fallDamage = buf.readInt();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
		    int value = buf.readInt();
		    String key = ByteBufUtils.readUTF8String(buf);
		    points.put(key, value);
		}
	}
}
