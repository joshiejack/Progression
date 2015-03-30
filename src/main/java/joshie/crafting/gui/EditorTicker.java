package joshie.crafting.gui;

import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketReload;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class EditorTicker {
    public static int LAST_TICK = 0;

    @SubscribeEvent
    public void onClickTick(ClientTickEvent event) {
        if (LAST_TICK > 0) {
            LAST_TICK--;

            if (LAST_TICK == 0) {
                PacketHandler.sendToServer(new PacketReload());
            }
        }
    }
}
