package joshie.progression.criteria.filters.location;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

@ProgressionRule(name="playerBroken", color=0xFFBBBBBB)
public class FilterPlayerLastBroken extends FilterLocationBase implements IHasEventBus {
    private static final HashMap<UUID, BlockPos> cache = new HashMap();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null) {
            cache.put(PlayerHelper.getUUIDForPlayer(event.getPlayer()), event.getPos());
        }
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        UUID uuid = PlayerHelper.getUUIDForPlayer(player);
        if (cache.get(uuid) == null) return null;
        else return new WorldLocation(player.dimension, cache.get(uuid));
    }

    @Override
    public boolean matches(WorldLocation location) {
        return true;
    }

    public static void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (UUID uuid: cache.keySet()) {
            BlockPos pos = cache.get(uuid);
            if (pos != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("UUID", uuid.toString());
                tag.setInteger("X", pos.getX());
                tag.setInteger("Y", pos.getY());
                tag.setInteger("Z", pos.getZ());
                list.appendTag(tag);
            }
        }

        nbt.setTag("LastBroken", list);
    }

    public static void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("LastBroken", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            try {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                UUID uuid = UUID.fromString(tag.getString("UUID"));
                BlockPos pos = new BlockPos(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"));
                if (uuid != null) {
                    cache.put(uuid, pos);
                }
            } catch (Exception e) {}
        }
    }
}
