package joshie.progression;

import static joshie.progression.lib.ProgressionInfo.JAVAPATH;
import static joshie.progression.lib.ProgressionInfo.MODID;
import static joshie.progression.lib.ProgressionInfo.MODNAME;
import static joshie.progression.lib.ProgressionInfo.VERSION;

import java.io.File;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.commands.CommandEdit;
import joshie.progression.commands.CommandHelp;
import joshie.progression.commands.CommandManager;
import joshie.progression.commands.CommandReload;
import joshie.progression.commands.CommandReset;
import joshie.progression.crafting.ActionType;
import joshie.progression.criteria.conditions.ConditionBiomeType;
import joshie.progression.criteria.conditions.ConditionCoordinates;
import joshie.progression.criteria.conditions.ConditionDaytime;
import joshie.progression.criteria.conditions.ConditionInInventory;
import joshie.progression.criteria.conditions.ConditionRandom;
import joshie.progression.criteria.rewards.RewardCommand;
import joshie.progression.criteria.rewards.RewardCrafting;
import joshie.progression.criteria.rewards.RewardCriteria;
import joshie.progression.criteria.rewards.RewardFallDamage;
import joshie.progression.criteria.rewards.RewardItem;
import joshie.progression.criteria.rewards.RewardPoints;
import joshie.progression.criteria.rewards.RewardResearch;
import joshie.progression.criteria.rewards.RewardSpeed;
import joshie.progression.criteria.rewards.RewardTime;
import joshie.progression.criteria.triggers.TriggerBreakBlock;
import joshie.progression.criteria.triggers.TriggerChangeDimension;
import joshie.progression.criteria.triggers.TriggerClickBlock;
import joshie.progression.criteria.triggers.TriggerCrafting;
import joshie.progression.criteria.triggers.TriggerItemEaten;
import joshie.progression.criteria.triggers.TriggerKill;
import joshie.progression.criteria.triggers.TriggerLogin;
import joshie.progression.criteria.triggers.TriggerObtain;
import joshie.progression.criteria.triggers.TriggerPoints;
import joshie.progression.criteria.triggers.TriggerResearch;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.EventsHandler;
import joshie.progression.handlers.GUIHandler;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.ModLogHelper;
import joshie.progression.items.ItemCriteria;
import joshie.progression.json.Options;
import joshie.progression.lib.ProgressionInfo;
import joshie.progression.network.PacketClaimed;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketReload;
import joshie.progression.network.PacketReset;
import joshie.progression.network.PacketRewardItem;
import joshie.progression.network.PacketSyncAbilities;
import joshie.progression.network.PacketSyncCriteria;
import joshie.progression.network.PacketSyncJSON;
import joshie.progression.network.PacketSyncTriggers;
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
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class Progression {
    public static final Logger logger = LogManager.getLogger(MODNAME);

    @SidedProxy(clientSide = JAVAPATH + "ClientProxy", serverSide = JAVAPATH + "CommonProxy")
    public static CommonProxy proxy;

    @Instance(MODID)
    public static Progression instance;

    public static PlayerSavedData data = new PlayerSavedData(MODNAME);
    public static boolean NEI_LOADED = false;
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
        NEI_LOADED = Loader.isModLoaded("NotEnoughItems");
        ProgressionAPI.registry = new APIHandler();
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
        FMLCommonHandler.instance().bus().register(new EventsHandler());
        
        item = new ItemCriteria().setUnlocalizedName("item");
        GameRegistry.registerItem(item, "item");
        
        if (Options.tileClaimerRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(item, 1, ItemCriteria.CLAIM), new Object[] { "F", "P", 'F', Items.flint, 'P', "plankWood" }));
        }

        ProgressionAPI.registry.registerConditionType(new ConditionBiomeType());
        ProgressionAPI.registry.registerConditionType(new ConditionRandom());
        ProgressionAPI.registry.registerConditionType(new ConditionCoordinates());
        ProgressionAPI.registry.registerConditionType(new ConditionDaytime());
        ProgressionAPI.registry.registerConditionType(new ConditionInInventory());
        
        ProgressionAPI.registry.registerRewardType(new RewardCommand());
        ProgressionAPI.registry.registerRewardType(new RewardCrafting());
        ProgressionAPI.registry.registerRewardType(new RewardCriteria());
        ProgressionAPI.registry.registerRewardType(new RewardFallDamage());
        ProgressionAPI.registry.registerRewardType(new RewardItem());
        ProgressionAPI.registry.registerRewardType(new RewardResearch());
        ProgressionAPI.registry.registerRewardType(new RewardPoints());
        ProgressionAPI.registry.registerRewardType(new RewardSpeed());
        ProgressionAPI.registry.registerRewardType(new RewardTime()); 

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

        CommandManager.INSTANCE.registerCommand(new CommandHelp());
        CommandManager.INSTANCE.registerCommand(new CommandEdit());
        CommandManager.INSTANCE.registerCommand(new CommandReload());
        CommandManager.INSTANCE.registerCommand(new CommandReset());

        PacketHandler.registerPacket(PacketSyncTriggers.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCriteria.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncAbilities.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketRewardItem.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketClaimed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncJSON.class);
        PacketHandler.registerPacket(PacketReload.class);
        PacketHandler.registerPacket(PacketReset.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
        proxy.initClient();
        
        ProgressionAPI.registry.registerActionType("TEST");
        for (ActionType type: ActionType.values()) {
            System.out.println(type.name());
        }
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
