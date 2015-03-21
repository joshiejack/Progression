package joshie.crafting;

import static joshie.crafting.lib.CraftingInfo.JAVAPATH;
import static joshie.crafting.lib.CraftingInfo.MODID;
import static joshie.crafting.lib.CraftingInfo.MODNAME;
import static joshie.crafting.lib.CraftingInfo.VERSION;

import java.io.File;
import java.util.Map;

import joshie.crafting.asm.CraftingTransformer;
import joshie.crafting.data.SaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class CraftingMod implements IFMLLoadingPlugin {
	public static final Logger logger = LogManager.getLogger(MODNAME);
	
	@SidedProxy(clientSide = JAVAPATH + "CraftingClient", serverSide = JAVAPATH + "CraftingCommon")
    public static CraftingCommon proxy;
	
	@Instance(MODID)
    public static CraftingMod instance;
	public static SaveData saveData;
	public static File configDir;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}
	
	/* Set up the save data */
	@EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
	      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;
	      World world = MinecraftServer.getServer().worldServers[0];
	      saveData = (SaveData) world.loadItemData(SaveData.class, MODNAME);
	      if (saveData == null) {
	    	  saveData = new SaveData(MODNAME);
	    	  world.setItemData(MODNAME, saveData);
	      }
	   }
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { CraftingTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return "";
	}
}
