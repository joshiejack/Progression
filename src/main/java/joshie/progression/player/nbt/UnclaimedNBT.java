package joshie.progression.player.nbt;

import com.google.common.collect.Multimap;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UnclaimedNBT implements IMapHelper {
    public static final UnclaimedNBT INSTANCE = new UnclaimedNBT();

    public Multimap map;

    public IMapHelper setMap(Multimap map) {
        this.map = map;
        return this;
    }

    @Override
    public Map getMap() {
        return map.asMap();
    }

    @Override
    public Object readKey(NBTTagCompound tag) {
        String name = tag.getString("UUID");
        return UUID.fromString(name);
    }

    @Override
    public Object readValue(NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("RewardList", 8);
        Set<IProgressionReward> rewards = new HashSet();
        for (int i = 0; i < list.tagCount(); i++) {
            String s = list.getStringTagAt(i);
            UUID uuid = UUID.fromString(s);
            IProgressionReward reward = APIHandler.getCache().getRewardFromUUID(uuid);
            rewards.add(reward);
        }


        return rewards;
    }

    @Override
    public void writeKey(NBTTagCompound tag, Object o) {
        String name = ((UUID)o).toString();
        tag.setString("UUID", name);
    }

    @Override
    public void writeValue(NBTTagCompound tag, Object o) {
        HashSet<IProgressionReward> rewards = (HashSet<IProgressionReward>) o;
        NBTTagList list = new NBTTagList();
        for (IProgressionReward reward: rewards) {
            NBTTagString string = new NBTTagString(reward.getUniqueID().toString());
            list.appendTag(string);
        }

        tag.setTag("RewardList", list);
    }
}
