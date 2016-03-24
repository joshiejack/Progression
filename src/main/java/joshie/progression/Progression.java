package joshie.progression;

import joshie.progression.commands.CommandManager;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.ModLogHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.lib.ProgressionInfo;
import joshie.progression.player.PlayerSavedData;
import joshie.progression.plugins.enchiridion.EnchiridionSupport;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static joshie.progression.lib.ProgressionInfo.*;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class Progression {
    public static final Logger logger = LogManager.getLogger(MODNAME);

    @SidedProxy(clientSide = JAVAPATH + "PClientProxy", serverSide = JAVAPATH + "PCommonProxy")
    public static PCommonProxy proxy;

    @Instance(MODID)
    public static Progression instance;

    public static PlayerSavedData data = new PlayerSavedData(MODNAME);
    public static Item item;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModLogHelper.log("enchiridion", "The more that you read, the more things you will know. The more that you learn, the more places you'll go.");
        ModLogHelper.log("Mariculture", "Just Keep Swimming...");
        
        //Init the action types
        try {
            Class.forName(JAVAPATH + "crafting.ActionType");
        } catch (ClassNotFoundException e) {}

        /** Create the config directory **/
        File root = new File("config" + File.separator + ProgressionInfo.MODPATH);
        if (!root.exists()) {
            root.mkdir();
        }

        Options.init(new Configuration(new File(root, "options.cfg")));
        
        proxy.preInit();
        proxy.initClient();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("enchiridion")) {
            try {
                EnchiridionSupport.init();
            } catch (Exception e) { logger.log(Level.ERROR, "Failed to load the Enchiridion Support"); }
        }
        
    	proxy.registerRendering();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ICommandManager manager = event.getServer().getCommandManager();
        if (manager instanceof ServerCommandManager) {
            ((ServerCommandManager) manager).registerCommand(CommandManager.INSTANCE);
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;

        
        //Remap all relevant data
        RemappingHandler.reloadServerData(JSONLoader.getTabs());
        
        World world = MinecraftServer.getServer().worldServers[0];
        data = (PlayerSavedData) world.loadItemData(PlayerSavedData.class, MODNAME);
        if (data == null) {
            createWorldData();
        }
    }
    
    @EventHandler
    public void onServerStarting(FMLServerStoppedEvent event) {
        RemappingHandler.resetRegistries();
    }

    public void createWorldData() {
        World world = MinecraftServer.getServer().worldServers[0];
        data = new PlayerSavedData(MODNAME);
        world.setItemData(MODNAME, data);
    }

	public static String translate(String string) {
		return StatCollector.translateToLocal("progression." + string);
	}

    public static String format(String string, Object... object) {
        return StatCollector.translateToLocalFormatted("progression." + string, object);
    }
}
