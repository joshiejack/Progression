package joshie.progression.player;

import static joshie.progression.player.DataStats.SpeedType.AIR;
import static joshie.progression.player.DataStats.SpeedType.LAND;
import static joshie.progression.player.DataStats.SpeedType.WATER;

import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.NBTHelper;
import joshie.progression.player.nbt.CustomNBT;
import joshie.progression.player.nbt.PointsNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DataStats {
    public static enum SpeedType {
        LAND, AIR, WATER;
    }

    private HashMap<String, NBTTagCompound> customData = new HashMap();
    private HashMap<String, Double> points = new HashMap();
    private float airSpeed = 1F;
    private float landSpeed = 1F;
    private float waterSpeed = 1F;

    private int fallDamage;
    
    public NBTTagCompound getCustomData(String key) {
        return customData.get(key);
    }

    public float getSpeed(SpeedType type) {
        return type == LAND ? landSpeed : type == AIR ? airSpeed : waterSpeed;
    }
    
    void setCustomData(String key, NBTTagCompound tag) {
        if (key == null || tag == null) return; //Don't add nulls
        customData.put(key, tag);
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

    public double getPoints(String name) {
        double amount = 0;
        if (points.containsKey(name)) {
            amount = points.get(name);
        } else points.put(name, amount);

        return amount;
    }

    void setResearchPoints(String name, double points) {
        this.points.put(name, points);
    }

    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Speed")) landSpeed = tag.getFloat("Speed");
        else landSpeed = tag.getFloat("LandSpeed");
        airSpeed = tag.getFloat("AirSpeed");
        waterSpeed = tag.getFloat("WaterSpeed");
        fallDamage = tag.getInteger("Fall Damage");
        NBTHelper.readMap(tag, "Points", PointsNBT.INSTANCE.setMap(points));
        NBTHelper.readMap(tag, "Custom", CustomNBT.INSTANCE.setMap(customData));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setFloat("LandSpeed", landSpeed);
        tag.setFloat("AirSpeed", airSpeed);
        tag.setFloat("WaterSpeed", waterSpeed);
        tag.setInteger("Fall Damage", fallDamage);
        NBTHelper.writeMap(tag, "Points", PointsNBT.INSTANCE.setMap(points));
        NBTHelper.writeMap(tag, "Custom", CustomNBT.INSTANCE.setMap(customData));
        return tag;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeFloat(landSpeed);
        buf.writeFloat(airSpeed);
        buf.writeFloat(waterSpeed);
        buf.writeInt(fallDamage);
        buf.writeInt(points.size());
        for (String key : points.keySet()) {
            buf.writeDouble(points.get(key));
            ByteBufUtils.writeUTF8String(buf, key);
        }

        //Hello Other map!!
        buf.writeInt(customData.size());
        for (String key : customData.keySet()) {
            ByteBufUtils.writeUTF8String(buf, key);
            ByteBufUtils.writeTag(buf, customData.get(key));
        }
    }

    public void fromBytes(ByteBuf buf) {
        landSpeed = buf.readFloat();
        airSpeed = buf.readFloat();
        waterSpeed = buf.readFloat();
        fallDamage = buf.readInt();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            double value = buf.readDouble();
            String key = ByteBufUtils.readUTF8String(buf);
            points.put(key, value);
        }

        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            String key = ByteBufUtils.readUTF8String(buf);
            NBTTagCompound tag = ByteBufUtils.readTag(buf);
            customData.put(key, tag);
        }
    }
}
