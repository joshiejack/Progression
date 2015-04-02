package joshie.crafting.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientHelper {	
	public static EntityPlayer getPlayer() {
		return getMinecraft().thePlayer;
	}

	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static boolean isForwardPressed() {
        return GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward);
    }

    public static World getWorld() {
        return getPlayer().worldObj;
    }
    
    public static boolean canEdit() {
        return getPlayer().capabilities.isCreativeMode;
    }
}
