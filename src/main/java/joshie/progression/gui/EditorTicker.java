package joshie.progression.gui;

import joshie.progression.Progression;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketReload;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class EditorTicker {
    public static int LAST_TICK = 0;
    public static int OVERRIDE_TICK = 0;

    @SubscribeEvent
    public void onClickTick(ClientTickEvent event) {
        if (OVERRIDE_TICK > 0) {
            OVERRIDE_TICK--;
                        
            if (OVERRIDE_TICK == 0) {
                ClientHelper.getPlayer().openGui(Progression.instance, 1, null, 0, 0, 0);
            }
        }
        
        if (LAST_TICK > 0) {
            LAST_TICK--;
            
            if (LAST_TICK == 0) {
                PacketHandler.sendToServer(new PacketReload());
            }
        }
    }
}
