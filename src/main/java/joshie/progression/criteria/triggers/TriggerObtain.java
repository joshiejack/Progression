package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerObtain extends TriggerBaseBoolean {
    public TriggerObtain() {
        super("openContainer", 0xFFFFFF00);
    }

    private boolean fired = false;

    @SubscribeEvent
    public void onEvent(PlayerOpenContainerEvent event) {
        long time = event.entityPlayer.worldObj.getTotalWorldTime();
        if (time % 30 == 0) {
            if (fired) {
                for (int i = 0; i < event.entityPlayer.inventory.mainInventory.length; i++) {
                    ItemStack stack = event.entityPlayer.inventory.mainInventory[i];
                    if (stack == null) continue;
                    ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), stack, event.entityPlayer, i);
                }
            }

            fired = !fired;
        }
    }

    @Override
    protected boolean isTrue(Object... data) {
        return true;
    }
}
