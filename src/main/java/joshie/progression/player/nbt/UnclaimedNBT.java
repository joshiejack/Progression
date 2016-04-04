package joshie.progression.player.nbt;

import com.google.common.collect.Multimap;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UnclaimedNBT implements IMapHelper<UUID, Set<IRewardProvider>> {
    public static final UnclaimedNBT INSTANCE = new UnclaimedNBT();

    public Multimap map;

    public IMapHelper setMap(Multimap map) {
        this.map = map;
        return this;
    }

    @Override
    public Map<UUID, Set<IRewardProvider>> getMap() {
        return map.asMap();
    }

    @Override
    public UUID readKey(NBTTagCompound tag) {
        return UUID.fromString(tag.getString("UUID"));
    }

    @Override
    public void writeKey(NBTTagCompound tag, UUID uuid) {
        tag.setString("UUID", uuid.toString());
    }

    @Override
    public Set<IRewardProvider> readValue(NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("RewardList", 8);
        Set<IRewardProvider> rewards = new HashSet();
        for (int i = 0; i < list.tagCount(); i++) {
            String s = list.getStringTagAt(i);
            UUID uuid = UUID.fromString(s);
            IRewardProvider reward = APIHandler.getCache().getRewardFromUUID(uuid);
            rewards.add(reward);
        }


        return rewards;
    }

    @Override
    public void writeValue(NBTTagCompound tag, Set<IRewardProvider> rewards) {
        NBTTagList list = new NBTTagList();
        for (IRewardProvider reward: rewards) {
            NBTTagString string = new NBTTagString(reward.getUniqueID().toString());
            list.appendTag(string);
        }

        tag.setTag("RewardList", list);
    }
}
