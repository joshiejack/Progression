package joshie.crafting.trigger;

import java.util.List;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerClickBlock extends TriggerBaseBlock {
    public TriggerClickBlock() {
        super("Click Block", 0xFF69008C, "clickBlock");
    }

    @Override
    public ITrigger newInstance() {
        return new TriggerClickBlock();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerInteractEvent event) {
        Block block = event.world.getBlock(event.x, event.y, event.z);
        int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
        CraftingAPI.registry.fireTrigger(event.entityPlayer, getTypeName(), block, meta);
    }

    @Override
    public void addTooltip(List<String> toolTip) {
        if (oreDictionary.equals("IGNORE")) {
            toolTip.add("  Click " + amount + " " + stack.getDisplayName());
        } else toolTip.add("  Click " + amount + " " + oreDictionary);
    }
}
