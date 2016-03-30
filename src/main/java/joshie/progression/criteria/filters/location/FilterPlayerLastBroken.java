package joshie.progression.criteria.filters.location;

import joshie.progression.api.special.IHasEventBus;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class FilterPlayerLastBroken extends FilterLocationBase implements IHasEventBus {
    public FilterPlayerLastBroken() {
        super("playerBroken", 0xFFBBBBBB);
    }

    private static final HashMap<EntityPlayer, BlockPos> cache = new HashMap();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null) {
            cache.put(event.getPlayer(), event.pos);
        }
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        if (cache.get(player) == null) return null;
        else return new WorldLocation(player.dimension, cache.get(player));
    }

    @Override
    public boolean matches(WorldLocation location) {
        return true;
    }
}
