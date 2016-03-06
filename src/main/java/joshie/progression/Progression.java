package joshie.progression;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.commands.*;
import joshie.progression.criteria.conditions.*;
import joshie.progression.criteria.filters.FilterItem;
import joshie.progression.criteria.rewards.*;
import joshie.progression.criteria.triggers.*;
import joshie.progression.handlers.*;
import joshie.progression.helpers.ModLogHelper;
import joshie.progression.items.ItemCriteria;
import joshie.progression.json.Options;
import joshie.progression.lib.ProgressionInfo;
import joshie.progression.network.*;
import joshie.progression.player.PlayerSavedData;
import joshie.progression.player.PlayerTracker;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static joshie.progression.lib.ProgressionInfo.*;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class Progression {
    public static final Logger logger = LogManager.getLogger(MODNAME);

    @SidedProxy(clientSide = JAVAPATH + "ClientProxy", serverSide = JAVAPATH + "CommonProxy")
    public static CommonProxy proxy;

    @Instance(MODID)
    public static Progression instance;

    public static PlayerSavedData data = new PlayerSavedData(MODNAME);
    public static boolean JEI_LOADED = false;
    public static Item item;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModLogHelper.log("Enchiridion2", "The more that you read, the more things you will know. The more that you learn, the more places you'll go.");
        ModLogHelper.log("Mariculture", "Just Keep Swimming...");
        
        try {
            Class.forName(JAVAPATH + "crafting.ActionType");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /** Create the config directory **/
        File dir = new File("config" + File.separator + ProgressionInfo.MODPATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        
        Options.init(new Configuration(new File(dir, "options.cfg")));

        RemappingHandler.resetRegistries();
        JEI_LOADED = Loader.isModLoaded("NotEnoughItems");
        ProgressionAPI.registry = new APIHandler();
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
        MinecraftForge.EVENT_BUS.register(new CraftingEvents());
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
        
        item = new ItemCriteria().setUnlocalizedName("item");
        GameRegistry.registerItem(item, "item");
        
        if (Options.tileClaimerRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(item, 1, ItemCriteria.CLAIM), new Object[] { "F", "P", 'F', Items.flint, 'P', "plankWood" }));
        }
        
        ProgressionAPI.registry.registerItemFilter(new FilterItem());

        ProgressionAPI.registry.registerConditionType(new ConditionBiomeType());
        ProgressionAPI.registry.registerConditionType(new ConditionRandom());
        ProgressionAPI.registry.registerConditionType(new ConditionCoordinates());
        ProgressionAPI.registry.registerConditionType(new ConditionDaytime());
        ProgressionAPI.registry.registerConditionType(new ConditionInInventory());
        
        ProgressionAPI.registry.registerRewardType(new RewardCommand());
        ProgressionAPI.registry.registerRewardType(new RewardCriteria());
        ProgressionAPI.registry.registerRewardType(new RewardFallDamage());
        ProgressionAPI.registry.registerRewardType(new RewardItem());
        ProgressionAPI.registry.registerRewardType(new RewardResearch());
        ProgressionAPI.registry.registerRewardType(new RewardPoints());
        ProgressionAPI.registry.registerRewardType(new RewardSpeed());
        ProgressionAPI.registry.registerRewardType(new RewardTime()); 
        ProgressionAPI.registry.registerRewardType(new RewardBreakBlock()); 
        ProgressionAPI.registry.registerRewardType(new RewardCrafting());
        ProgressionAPI.registry.registerRewardType(new RewardFurnace());
        ProgressionAPI.registry.registerRewardType(new RewardHarvestDrop());
        ProgressionAPI.registry.registerRewardType(new RewardLivingDrop());
        ProgressionAPI.registry.registerRewardType(new RewardClear());

        ProgressionAPI.registry.registerTriggerType(new TriggerBreakBlock());
        ProgressionAPI.registry.registerTriggerType(new TriggerCrafting());
        ProgressionAPI.registry.registerTriggerType(new TriggerItemEaten());
        ProgressionAPI.registry.registerTriggerType(new TriggerKill());
        ProgressionAPI.registry.registerTriggerType(new TriggerLogin());
        ProgressionAPI.registry.registerTriggerType(new TriggerObtain());
        ProgressionAPI.registry.registerTriggerType(new TriggerResearch());
        ProgressionAPI.registry.registerTriggerType(new TriggerClickBlock());
        ProgressionAPI.registry.registerTriggerType(new TriggerPoints());
        ProgressionAPI.registry.registerTriggerType(new TriggerChangeDimension());
        ProgressionAPI.registry.registerTriggerType(new TriggerAchievement());
        ProgressionAPI.registry.registerTriggerType(new TriggerTick());

        CommandManager.INSTANCE.registerCommand(new CommandHelp());
        CommandManager.INSTANCE.registerCommand(new CommandEdit());
        CommandManager.INSTANCE.registerCommand(new CommandReload());
        CommandManager.INSTANCE.registerCommand(new CommandReset());

        PacketHandler.registerPacket(PacketSyncTriggers.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCriteria.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncAbilities.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketRewardItem.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketClaimed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketCompleted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketOpenEditor.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketReload.class);
        PacketHandler.registerPacket(PacketReset.class);
        PacketHandler.registerPacket(PacketSyncJSON.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
        proxy.initClient();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ICommandManager manager = event.getServer().getCommandManager();
        if (manager instanceof ServerCommandManager) {
            ((ServerCommandManager) manager).registerCommand(CommandManager.INSTANCE);
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;

        
        //Remap all relevant data
        RemappingHandler.reloadServerData();
        
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
}
