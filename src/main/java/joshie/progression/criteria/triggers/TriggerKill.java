package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ProgressionRule(name="kill", color=0xFF000000)
public class TriggerKill extends TriggerBaseEntity {
    @Override
    public ITrigger copy() {
        return copyEntity(new TriggerKill());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(LivingDeathEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (!(source instanceof EntityPlayer)) {
            source = event.source.getEntity();
        }

        if (source instanceof EntityPlayer) {
            ProgressionAPI.registry.fireTrigger((EntityPlayer) source, getProvider().getUnlocalisedName(), event.entityLiving);
        }
    }
}