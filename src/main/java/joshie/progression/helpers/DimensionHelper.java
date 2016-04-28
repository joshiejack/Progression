package joshie.progression.helpers;

import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncDimensions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;

public class DimensionHelper {
    public static HashMap<Integer, String> dimensionNames;

    public static void sendPacket(EntityPlayerMP player) {
        if (dimensionNames == null) {
            dimensionNames = new HashMap<Integer, String>();
            for (int i = -128; i < 128; i++) {
                try {
                    String name = DimensionManager.getWorld(i).provider.getDimensionName();
                    if (name != null) {
                        dimensionNames.put(i, name);
                    }
                } catch (Exception e) {}
            }
        }

        PacketHandler.sendToClient(new PacketSyncDimensions(dimensionNames), player);
    }

    public static String getDimensionNameFromID(final int id) {
        return dimensionNames.get(id);
    }

    public static void setData(int[] ids, String[] dimensions) {
        for (int i = 0; i < ids.length; i++) {
            dimensionNames.put(ids[i], dimensions[i]);
        }
    }
}
