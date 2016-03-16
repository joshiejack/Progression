package joshie.progression;

import static joshie.progression.lib.ProgressionInfo.JAVAPATH;
import static joshie.progression.lib.ProgressionInfo.MODID;
import static joshie.progression.lib.ProgressionInfo.MODNAME;
import static joshie.progression.lib.ProgressionInfo.VERSION;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.commands.CommandEdit;
import joshie.progression.commands.CommandHelp;
import joshie.progression.commands.CommandManager;
import joshie.progression.commands.CommandReload;
import joshie.progression.commands.CommandReset;
import joshie.progression.criteria.conditions.ConditionBiomeType;
import joshie.progression.criteria.conditions.ConditionBoolean;
import joshie.progression.criteria.conditions.ConditionCoordinates;
import joshie.progression.criteria.conditions.ConditionDaytime;
import joshie.progression.criteria.conditions.ConditionHasCriteria;
import joshie.progression.criteria.conditions.ConditionHasPotionEffect;
import joshie.progression.criteria.conditions.ConditionInInventory;
import joshie.progression.criteria.conditions.ConditionPoints;
import joshie.progression.criteria.conditions.ConditionRandom;
import joshie.progression.criteria.filters.FilterBlock;
import joshie.progression.criteria.filters.FilterBlockMod;
import joshie.progression.criteria.filters.FilterBlockOre;
import joshie.progression.criteria.filters.FilterBlockStack;
import joshie.progression.criteria.filters.FilterBlockState;
import joshie.progression.criteria.filters.FilterItem;
import joshie.progression.criteria.filters.FilterItemMeta;
import joshie.progression.criteria.filters.FilterItemMod;
import joshie.progression.criteria.filters.FilterItemNBT;
import joshie.progression.criteria.filters.FilterItemOre;
import joshie.progression.criteria.filters.FilterItemStack;
import joshie.progression.criteria.filters.FilterPotionEffect;
import joshie.progression.criteria.rewards.RewardBoolean;
import joshie.progression.criteria.rewards.RewardBreakBlock;
import joshie.progression.criteria.rewards.RewardClear;
import joshie.progression.criteria.rewards.RewardCommand;
import joshie.progression.criteria.rewards.RewardCrafting;
import joshie.progression.criteria.rewards.RewardCriteria;
import joshie.progression.criteria.rewards.RewardFallDamage;
import joshie.progression.criteria.rewards.RewardFurnace;
import joshie.progression.criteria.rewards.RewardHarvestDrop;
import joshie.progression.criteria.rewards.RewardItem;
import joshie.progression.criteria.rewards.RewardLivingDrop;
import joshie.progression.criteria.rewards.RewardPlaceBlock;
import joshie.progression.criteria.rewards.RewardPoints;
import joshie.progression.criteria.rewards.RewardPotion;
import joshie.progression.criteria.rewards.RewardSpeed;
import joshie.progression.criteria.rewards.RewardTeleport;
import joshie.progression.criteria.rewards.RewardTime;
import joshie.progression.criteria.triggers.TriggerAchievement;
import joshie.progression.criteria.triggers.TriggerBoolean;
import joshie.progression.criteria.triggers.TriggerBreakBlock;
import joshie.progression.criteria.triggers.TriggerChangeDimension;
import joshie.progression.criteria.triggers.TriggerChat;
import joshie.progression.criteria.triggers.TriggerClickBlock;
import joshie.progression.criteria.triggers.TriggerCrafting;
import joshie.progression.criteria.triggers.TriggerItemEaten;
import joshie.progression.criteria.triggers.TriggerKill;
import joshie.progression.criteria.triggers.TriggerLogin;
import joshie.progression.criteria.triggers.TriggerObtain;
import joshie.progression.criteria.triggers.TriggerPoints;
import joshie.progression.criteria.triggers.TriggerTick;
import joshie.progression.enchiridion.EnchiridionSupport;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.CraftingEvents;
import joshie.progression.handlers.EventsHandler;
import joshie.progression.handlers.GUIHandler;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.ModLogHelper;
import joshie.progression.items.ItemCriteria;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.lib.ProgressionInfo;
import joshie.progression.network.PacketClaimed;
import joshie.progression.network.PacketCompleted;
import joshie.progression.network.PacketFireTrigger;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketOpenEditor;
import joshie.progression.network.PacketReload;
import joshie.progression.network.PacketReset;
import joshie.progression.network.PacketRewardItem;
import joshie.progression.network.PacketSyncAbilities;
import joshie.progression.network.PacketSyncCriteria;
import joshie.progression.network.PacketSyncJSONToClient;
import joshie.progression.network.PacketSyncJSONToServer;
import joshie.progression.network.PacketSyncTriggers;
import joshie.progression.player.PlayerHandler;
import joshie.progression.player.PlayerSavedData;
import joshie.progression.player.PlayerTracker;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
        ModLogHelper.log("enchiridion", "The more that you read, the more things you will know. The more that you learn, the more places you'll go.");
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
        JEI_LOADED = Loader.isModLoaded("JEI");
        ProgressionAPI.registry = new APIHandler();
        ProgressionAPI.player = new PlayerHandler();
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
        MinecraftForge.EVENT_BUS.register(new CraftingEvents());
        
        item = new ItemCriteria().setUnlocalizedName("item");
        GameRegistry.registerItem(item, "item");
        
        if (Options.tileClaimerRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(item, 1, ItemCriteria.CLAIM), new Object[] { "F", "P", 'F', Items.flint, 'P', "plankWood" }));
        }
        
        ProgressionAPI.registry.registerItemFilter(new FilterItemStack());
        ProgressionAPI.registry.registerItemFilter(new FilterItem());
        ProgressionAPI.registry.registerItemFilter(new FilterItemMeta());
        ProgressionAPI.registry.registerItemFilter(new FilterItemNBT());
        ProgressionAPI.registry.registerItemFilter(new FilterItemMod());
        ProgressionAPI.registry.registerItemFilter(new FilterItemOre());
        ProgressionAPI.registry.registerItemFilter(new FilterBlockStack());
        ProgressionAPI.registry.registerItemFilter(new FilterBlock());
        ProgressionAPI.registry.registerItemFilter(new FilterBlockState());
        ProgressionAPI.registry.registerItemFilter(new FilterBlockMod());
        ProgressionAPI.registry.registerItemFilter(new FilterBlockOre());
        ProgressionAPI.registry.registerItemFilter(new FilterPotionEffect());

        ProgressionAPI.registry.registerConditionType(new ConditionBiomeType());
        ProgressionAPI.registry.registerConditionType(new ConditionRandom());
        ProgressionAPI.registry.registerConditionType(new ConditionCoordinates());
        ProgressionAPI.registry.registerConditionType(new ConditionDaytime());
        ProgressionAPI.registry.registerConditionType(new ConditionInInventory());
        ProgressionAPI.registry.registerConditionType(new ConditionHasPotionEffect());
        ProgressionAPI.registry.registerConditionType(new ConditionHasCriteria());
        ProgressionAPI.registry.registerConditionType(new ConditionBoolean());
        ProgressionAPI.registry.registerConditionType(new ConditionPoints());
        
        ProgressionAPI.registry.registerRewardType(new RewardCommand());
        ProgressionAPI.registry.registerRewardType(new RewardCriteria());
        ProgressionAPI.registry.registerRewardType(new RewardFallDamage());
        ProgressionAPI.registry.registerRewardType(new RewardItem());
        ProgressionAPI.registry.registerRewardType(new RewardBoolean());
        ProgressionAPI.registry.registerRewardType(new RewardPoints());
        ProgressionAPI.registry.registerRewardType(new RewardSpeed());
        ProgressionAPI.registry.registerRewardType(new RewardTime()); 
        ProgressionAPI.registry.registerRewardType(new RewardBreakBlock()); 
        ProgressionAPI.registry.registerRewardType(new RewardCrafting());
        ProgressionAPI.registry.registerRewardType(new RewardFurnace());
        ProgressionAPI.registry.registerRewardType(new RewardHarvestDrop());
        ProgressionAPI.registry.registerRewardType(new RewardLivingDrop());
        ProgressionAPI.registry.registerRewardType(new RewardClear());
        ProgressionAPI.registry.registerRewardType(new RewardPotion());
        ProgressionAPI.registry.registerRewardType(new RewardPlaceBlock());
        ProgressionAPI.registry.registerRewardType(new RewardTeleport());

        ProgressionAPI.registry.registerTriggerType(new TriggerBreakBlock());
        ProgressionAPI.registry.registerTriggerType(new TriggerCrafting());
        ProgressionAPI.registry.registerTriggerType(new TriggerItemEaten());
        ProgressionAPI.registry.registerTriggerType(new TriggerKill());
        ProgressionAPI.registry.registerTriggerType(new TriggerLogin());
        ProgressionAPI.registry.registerTriggerType(new TriggerObtain());
        ProgressionAPI.registry.registerTriggerType(new TriggerBoolean());
        ProgressionAPI.registry.registerTriggerType(new TriggerClickBlock());
        ProgressionAPI.registry.registerTriggerType(new TriggerPoints());
        ProgressionAPI.registry.registerTriggerType(new TriggerChangeDimension());
        ProgressionAPI.registry.registerTriggerType(new TriggerAchievement());
        ProgressionAPI.registry.registerTriggerType(new TriggerTick());
        ProgressionAPI.registry.registerTriggerType(new TriggerChat());

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
        PacketHandler.registerPacket(PacketFireTrigger.class, Side.SERVER);
        PacketHandler.registerPacket(PacketReload.class);
        PacketHandler.registerPacket(PacketReset.class);
        PacketHandler.registerPacket(PacketSyncJSONToClient.class);
        PacketHandler.registerPacket(PacketSyncJSONToServer.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
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
}
