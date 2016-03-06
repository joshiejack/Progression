package joshie.progression.player;
import static joshie.progression.player.DataStats.SpeedType.AIR;
import static joshie.progression.player.DataStats.SpeedType.LAND;
import static joshie.progression.player.DataStats.SpeedType.WATER;

import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.NBTHelper;
import joshie.progression.player.nbt.PointsNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DataStats {
    public static enum SpeedType {      
        LAND, AIR, WATER;
    }
    
    private HashMap<String, Integer> points = new HashMap();
	private float airSpeed = 1F;
	private float landSpeed = 1F;
	private float waterSpeed = 1F;
	
	private int fallDamage;

	public float getSpeed(SpeedType type) {
	    return type == LAND ? landSpeed : type == AIR ? airSpeed: waterSpeed;
	}
	
	void setSpeed(SpeedType type, float speed) {
	    if (type == LAND) landSpeed = speed;
	    if (type == AIR) airSpeed = speed;
	    if (type == WATER) waterSpeed = speed;
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
	    if (tag.hasKey("Speed")) landSpeed = tag.getFloat("Speed");
	    else landSpeed = tag.getFloat("LandSpeed");
		airSpeed = tag.getFloat("AirSpeed");
		waterSpeed = tag.getFloat("WaterSpeed");
		fallDamage = tag.getInteger("Fall Damage");
		NBTHelper.readMap(tag, "Points", PointsNBT.INSTANCE.setMap(points));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setFloat("LandSpeed", landSpeed);
		tag.setFloat("AirSpeed", airSpeed);
		tag.setFloat("WaterSpeed", waterSpeed);
		tag.setInteger("Fall Damage", fallDamage);
		NBTHelper.writeMap(tag, "Points", PointsNBT.INSTANCE.setMap(points));
		return tag;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeFloat(landSpeed);
		buf.writeFloat(airSpeed);
		buf.writeFloat(waterSpeed);
		buf.writeInt(fallDamage);
		buf.writeInt(points.size());
		for (String key: points.keySet()) {
		    buf.writeInt(points.get(key));
		    ByteBufUtils.writeUTF8String(buf, key);
		}
	}

	public void fromBytes(ByteBuf buf) {
	    landSpeed = buf.readFloat();
	    airSpeed = buf.readFloat();
	    waterSpeed = buf.readFloat();
		fallDamage = buf.readInt();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
		    int value = buf.readInt();
		    String key = ByteBufUtils.readUTF8String(buf);
		    points.put(key, value);
		}
	}
}
