package joshie.progression.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MCClientHelper {	
	public static EntityPlayer getPlayer() {
		return getMinecraft().thePlayer;
	}

	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static boolean isMovementPressed() {
        return GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward);
    }

    public static World getWorld() {
        return getPlayer().worldObj;
    }
    
    public static boolean isInEditMode() {
        return getPlayer().capabilities.isCreativeMode;
    }
}
