package joshie.progression.player.data;

import joshie.progression.network.core.PacketNBT.INBTWritable;
import net.minecraft.nbt.NBTTagCompound;

import static joshie.progression.player.data.AbilityStats.SpeedType.*;

public class AbilityStats implements INBTWritable<AbilityStats> {
    public enum SpeedType {
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
        else landSpeed = tag.getFloat("SpeedLand");
        airSpeed = tag.getFloat("SpeedAir");
        waterSpeed = tag.getFloat("SpeedWater");
        stepAssist = tag.getFloat("StepAssist");
        fallDamage = tag.getInteger("FallDamage");
        return this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setFloat("SpeedLand", landSpeed);
        tag.setFloat("SpeedAir", airSpeed);
        tag.setFloat("SpeedWater", waterSpeed);
        tag.setFloat("StepAssist", stepAssist);
        tag.setInteger("FallDamage", fallDamage);
        return tag;
    }
}
