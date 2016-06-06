package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ProgressionRule(name="attack", color=0xFF3C3F41)
public class TriggerAttack extends TriggerBaseEntity {
    @Override
    public ITrigger copy() {
        return copyEntity(new TriggerAttack());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(AttackEntityEvent event) {
        if (event.target instanceof EntityLivingBase) {
            ProgressionAPI.registry.fireTrigger(event.entityPlayer, getProvider().getUnlocalisedName(), event.target);
        }
    }
}