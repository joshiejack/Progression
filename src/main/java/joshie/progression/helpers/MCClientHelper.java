package joshie.progression.helpers;

import joshie.progression.api.special.DisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCClientHelper {
    public static boolean FORCE_EDIT = false;

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @SideOnly(Side.CLIENT)
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    private static boolean isInEditMode() {
        return FORCE_EDIT;
    }

    public static DisplayMode getMode() {
        return isInEditMode() ? DisplayMode.EDIT : DisplayMode.DISPLAY;
    }
}
