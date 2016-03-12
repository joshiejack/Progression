package joshie.progression.api;

import net.minecraft.entity.player.EntityPlayer;

public interface ICustomDataBuilder {
    /** Called when handling the packet fire trigger after all the data has been passed
     *  You can use this to build any objects you want to. */
    public Object[] getObjects(EntityPlayer player, String type, Object... data);
}
