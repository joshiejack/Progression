package joshie.progression.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

import java.util.UUID;

@Cancelable
public class TabVisibleEvent extends PlayerEvent {
    public final UUID unique;

    public TabVisibleEvent(EntityPlayer player, UUID unique) {
        super(player);
        this.unique = unique;
    }
}
