package joshie.progression.criteria.rewards;

import joshie.progression.api.special.IHasEventBus;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class RewardBaseAbility extends RewardBaseSingular implements IHasEventBus {
    public RewardBaseAbility(ItemStack stack, String name, int color) {
        super(stack, name, color);
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }
}
