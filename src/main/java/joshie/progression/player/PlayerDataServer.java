package joshie.progression.player;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncAbilities;
import joshie.progression.network.PacketSyncCustomData;
import joshie.progression.network.PacketSyncPoints;
import joshie.progression.player.data.AbilityStats.SpeedType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;
import java.util.concurrent.Callable;

public class PlayerDataServer extends PlayerDataCommon {
    private final UUID uuid;

    public PlayerDataServer(PlayerTeam team) {
        this.team = team;
        this.uuid = team.getOwner();
        this.mappings.setMaster(this);
    }

    @Override
    public void setTeam(PlayerTeam team) {
        NBTTagCompound data = new NBTTagCompound();
        team.writeToNBT(data);
        this.team.readFromNBT(data);
        this.team.syncChanges(Side.SERVER);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void saveAndSyncPoints() {
        PacketHandler.sendToTeam(new PacketSyncPoints(points), team);
        markDirty();
    }

    public void saveAndSyncAbiliities() {
        PacketHandler.sendToTeam(new PacketSyncAbilities(abilities), team);
        markDirty();
    }

    public void saveAndSyncCustomData() {
        PacketHandler.sendToTeam(new PacketSyncCustomData(custom), team);
        markDirty();
    }

    public void setCustomData(String key, NBTTagCompound tag) {
        custom.setCustomData(key, tag);
        saveAndSyncCustomData();
    }

    public void addSpeed(SpeedType type, float speed) {
        float newStat = abilities.getSpeed(type) + speed;
        abilities.setSpeed(type, newStat);
        saveAndSyncAbiliities();
    }

    public void addStepAssist(float steps) {
        float newStat = abilities.getStepAssist() + steps;
        abilities.addStepAssist(newStat);
        saveAndSyncAbiliities();
    }

    public void addFallDamagePrevention(int maxAbsorbed) {
        int newStat = abilities.getFallDamagePrevention() + maxAbsorbed;
        abilities.setFallDamagePrevention(newStat);
        saveAndSyncAbiliities();
    }

    public void addDouble(final String name, final double amount) {
        final double newStat = points.getDouble(name) + amount;
        points.setDouble(name, newStat);
        saveAndSyncPoints();
        getMappings().todo.add(new Callable() {
            @Override
            public Object call() throws Exception {
                return ProgressionAPI.registry.fireTrigger(uuid, "trigger.points", name, newStat);
            }
        });
    }

    public void setBoolean(final String name, final boolean value) {
        points.setBoolean(name, value);
        saveAndSyncPoints();
        getMappings().todo.add(new Callable() {
            @Override
            public Object call() throws Exception {
                return ProgressionAPI.registry.fireTrigger(uuid, "trigger.boolean", name, points.getBoolean(name));
            }
        });
    }

    public void readFromNBT(NBTTagCompound tag) {
        abilities.readFromNBT(tag.getCompoundTag("Abilities"));
        custom.readFromNBT(tag.getCompoundTag("CustomData"));
        mappings.readFromNBT(tag.getCompoundTag("CriteriaData"));
        points.readFromNBT(tag.getCompoundTag("Points"));
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setTag("Abilities", abilities.writeToNBT(new NBTTagCompound()));
        tag.setTag("CustomData", custom.writeToNBT(new NBTTagCompound()));
        tag.setTag("CriteriaData", mappings.writeToNBT(new NBTTagCompound()));
        tag.setTag("Points", points.writeToNBT(new NBTTagCompound()));
    }
}
