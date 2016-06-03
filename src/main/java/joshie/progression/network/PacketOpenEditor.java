package joshie.progression.network;

import joshie.progression.Progression;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.GuiIDs;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@Packet(isSided = true, side = Side.CLIENT)
public class PacketOpenEditor extends PenguinPacket {
    @Override
    public void handlePacket(EntityPlayer player) {
        MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.EDITOR, null, 0, 0, 0);
    }
}
