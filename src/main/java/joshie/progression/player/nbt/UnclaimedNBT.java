package joshie.progression.player.nbt;

import com.google.common.collect.Multimap;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.NBTHelper.IMultimapHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class UnclaimedNBT implements IMultimapHelper<UUID, IRewardProvider> {
    public static final UnclaimedNBT INSTANCE = new UnclaimedNBT();

    public Multimap<UUID, IRewardProvider> map;

    public IMultimapHelper setMap(Multimap map) {
        this.map = map;
        return this;
    }

    @Override
    public Multimap<UUID, IRewardProvider> getMap() {
        return map;
    }

    @Override
    public UUID readKey(NBTTagCompound tag) {
        return UUID.fromString(tag.getString("PlayerUUID"));
    }

    @Override
    public void writeKey(NBTTagCompound tag, UUID uuid) {
        tag.setString("PlayerUUID", uuid.toString());
    }

    @Override
    public IRewardProvider readValue(NBTTagCompound tag) {
        UUID uuid = UUID.fromString(tag.getString("RewardUUID"));
        return APIHandler.getCache(false).getRewardFromUUID(uuid);
    }

    @Override
    public void writeValue(NBTTagCompound tag, IRewardProvider rewards) {
        tag.setString("RewardUUID", rewards.getUniqueID().toString());
    }
}
