package joshie.crafting.lib;

import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CraftingInfo {
	public static final String MODID = "Progression";
    public static final String MODNAME = "Progression";
    public static final String MODPATH = "progression";
    public static final String JAVAPATH = "joshie.crafting.";
    public static final String ASMPATH = "joshie/crafting/";
    public static final String VERSION = "@VERSION@";
    
    @SideOnly(Side.CLIENT)
    public static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
}
