package joshie.crafting.trigger;

import joshie.crafting.api.CraftingAPI;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerClickBlock extends TriggerBaseBlock {
    public TriggerClickBlock() {
        super("clickBlock", 0xFF69008C);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerInteractEvent event) {
        Block block = event.world.getBlock(event.x, event.y, event.z);
        int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
        CraftingAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), block, meta);
    }
}
