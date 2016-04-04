package joshie.progression.helpers;

import joshie.progression.api.special.DisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class MCClientHelper {
    public static boolean FORCE_EDIT = false;

    public static EntityPlayer getPlayer() {
		return getMinecraft().thePlayer;
	}

	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

    public static World getWorld() {
        return getPlayer().worldObj;
    }
    
    public static boolean isInEditMode() {
        return FORCE_EDIT ? true: getPlayer().capabilities.isCreativeMode;
    }

    public static boolean isShiftPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static DisplayMode getMode() {
        return isInEditMode() ? DisplayMode.EDIT : DisplayMode.DISPLAY;
    }
}
