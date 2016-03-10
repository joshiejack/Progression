package joshie.progression.gui.base;

import joshie.progression.Progression;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketReload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class SaveTicker {
    public static int LAST_TICK = 0;
    public static int OVERRIDE_TICK = 0;

    @SubscribeEvent
    public void onClickTick(ClientTickEvent event) {
        if (OVERRIDE_TICK > 0) {
            OVERRIDE_TICK--;
                        
            if (OVERRIDE_TICK == 0) {
                MCClientHelper.getPlayer().openGui(Progression.instance, 1, null, 0, 0, 0);
            }
        }
        
        if (LAST_TICK > 0) {
            LAST_TICK--;
            
            if (LAST_TICK == 0) {
                
            }
        }
    }
}
