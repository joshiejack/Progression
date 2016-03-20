package joshie.progression.player.data;

import static joshie.progression.player.data.AbilityStats.SpeedType.AIR;
import static joshie.progression.player.data.AbilityStats.SpeedType.LAND;
import static joshie.progression.player.data.AbilityStats.SpeedType.WATER;

import joshie.progression.network.core.PacketNBT.INBTWritable;
import net.minecraft.nbt.NBTTagCompound;

public class AbilityStats implements INBTWritable<AbilityStats> {
    public static enum SpeedType {
        LAND, AIR, WATER;
    }

    private float airSpeed = 1F;
    private float landSpeed = 1F;
    private float waterSpeed = 1F;
    private float stepAssist = 0.5F;

    private int fallDamage;

    public float getSpeed(SpeedType type) {
        return type == LAND ? landSpeed : type == AIR ? airSpeed : waterSpeed;
    }

    public float getStepAssist() {
        return stepAssist;
    }

    public void addStepAssist(float newStat) {
        this.stepAssist = newStat;
    }

    public void setSpeed(SpeedType type, float speed) {
        if (type == LAND) landSpeed = speed;
        if (type == AIR) airSpeed = speed;
        if (type == WATER) waterSpeed = speed;
    }

    public int getFallDamagePrevention() {
        return fallDamage;
    }

    public void setFallDamagePrevention(int fallDamage) {
        this.fallDamage = fallDamage;
    }

    @Override
    public AbilityStats readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Speed")) landSpeed = tag.getFloat("Speed");
        else landSpeed = tag.getFloat("LandSpeed");
        airSpeed = tag.getFloat("AirSpeed");
        waterSpeed = tag.getFloat("WaterSpeed");
        stepAssist = tag.getFloat("StepAssist");
        fallDamage = tag.getInteger("Fall Damage");
        return this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setFloat("LandSpeed", landSpeed);
        tag.setFloat("AirSpeed", airSpeed);
        tag.setFloat("WaterSpeed", waterSpeed);
        tag.setFloat("StepAssist", stepAssist);
        tag.setInteger("Fall Damage", fallDamage);
        return tag;
    }
}
