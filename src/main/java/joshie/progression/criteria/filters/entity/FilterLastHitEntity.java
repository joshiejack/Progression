package joshie.progression.criteria.filters.entity;

import com.google.common.collect.Lists;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.helpers.ListHelper;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@ProgressionRule(name="lastHit", color=0xFFB25900)
public class FilterLastHitEntity extends FilterBaseEntity implements IHasEventBus {
    private static final HashMap<UUID, EntityLivingBase> cache = new HashMap();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onEvent(AttackEntityEvent event) {
        if (!event.getTarget().getEntityWorld().isRemote) {
            if (event.getTarget() instanceof EntityLivingBase) {
                cache.put(PlayerHelper.getUUIDForPlayer(event.getEntityPlayer()), (EntityLivingBase) event.getTarget());
            }
        }
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }

    @Override
    public List<EntityLivingBase> getRandom(EntityPlayer player) {
        if (cache.get(PlayerHelper.getUUIDForPlayer(player)) != null) {
            EntityLivingBase existing = cache.get(PlayerHelper.getUUIDForPlayer(player));
            return ListHelper.newArrayList(existing);
        }

        return Lists.newArrayList();
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        return true;
    }
}