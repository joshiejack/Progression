package joshie.progression.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class TabVisibleEvent extends PlayerEvent {
    public final String unique;

    public TabVisibleEvent(EntityPlayer player, String unique) {
        super(player);
        this.unique = unique;
    }
}
