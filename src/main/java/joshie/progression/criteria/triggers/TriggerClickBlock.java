package joshie.progression.criteria.triggers;

import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.ISpecialFieldProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerClickBlock extends TriggerBaseBlock implements ISpecialFieldProvider {
    public TriggerClickBlock() {
        super("clickBlock", 0xFF69008C);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerInteractEvent event) {
        if (event.pos != null) {
        	IBlockState state = event.world.getBlockState(event.pos);
        	Block block = state.getBlock();
        	int meta = block.getMetaFromState(state);
    
            if (ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), block, meta) == Result.DENY) {
                event.setCanceled(true);
            }
        }
    }
    
    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("filters");
    }
    
    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
    }
    
    @Override
    public String getDescription() {
        if (cancel) {
            return Progression.translate("trigger.clickBlock.cancel");
        }
        
        return Progression.format("trigger.clickBlock.description", amount);
    }
}
