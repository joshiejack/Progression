package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.PClientProxy;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.progression.gui.core.GuiList.CORE;
import static joshie.progression.network.core.PacketPart.SEND_SIZE;

public class PacketLockUnlockSaving extends PenguinPacket {
    private boolean lock;

    public PacketLockUnlockSaving() {}
    public PacketLockUnlockSaving(boolean lock) {
        this.lock = lock;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeBoolean(lock);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        lock = from.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.worldObj.isRemote) PacketHandler.sendToEveryone(new PacketLockUnlockSaving(true)); //If we're server, tell everyone they CANNOT EDIT
        else { //If we're client lets check some stuff
            PClientProxy.bookLocked = lock; //Lock the book
            if (PClientProxy.isSaver && lock) { //If we were the person saving
                //Now save everything :)
                JSONLoader.saveData(true); //Save the data clientside
                String json = JSONLoader.getClientTabJsonData();
                int length = SplitHelper.splitStringEvery(json, 5000).length;
                //Send the packet to the server about the new json
                PacketHandler.sendToServer(new PacketSyncJSONToServer(SEND_SIZE, "", length, System.currentTimeMillis()));
                CORE.clearEditors();
            }
        }
    }
}
