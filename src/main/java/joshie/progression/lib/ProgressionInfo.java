package joshie.progression.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProgressionInfo {
	public static final String MODID = "Progression";
    public static final String MODNAME = "Progression";
    public static final String MODPATH = "progression";
    public static final String JAVAPATH = "joshie.progression.";
    public static final String ASMPATH = "joshie/progression/";
    public static final String VERSION = "@VERSION@";
    public static final String BOOKPATH = "progression:textures/books/";
    public static final String ITEMFILTER = "joshie.progression.api.IItemFilter";
    public static final String ENTITYFILTER = "joshie.progression.api.IEntityFilter";
    
    @SideOnly(Side.CLIENT)
    public static final ResourceLocation textures = new ResourceLocation(MODPATH, "textures/gui/textures.png");
}
