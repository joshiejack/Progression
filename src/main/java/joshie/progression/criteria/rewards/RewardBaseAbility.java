package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.api.special.IHasEventBus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

import java.util.List;

public abstract class RewardBaseAbility extends RewardBaseSingular implements ICustomTooltip, IHasEventBus {
    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.GOLD + Progression.translate("ability"));
        addAbilityTooltip(list);
    }

    public abstract void addAbilityTooltip(List list);
}
