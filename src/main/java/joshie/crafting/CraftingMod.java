package joshie.crafting;

import static joshie.crafting.lib.CraftingInfo.JAVAPATH;
import static joshie.crafting.lib.CraftingInfo.MODID;
import static joshie.crafting.lib.CraftingInfo.MODNAME;
import static joshie.crafting.lib.CraftingInfo.VERSION;

import java.io.File;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.commands.CommandEdit;
import joshie.crafting.commands.CommandHelp;
import joshie.crafting.commands.CommandManager;
import joshie.crafting.commands.CommandReload;
import joshie.crafting.commands.CommandReset;
import joshie.crafting.conditions.ConditionBiomeType;
import joshie.crafting.conditions.ConditionCoordinates;
import joshie.crafting.conditions.ConditionDaytime;
import joshie.crafting.conditions.ConditionInInventory;
import joshie.crafting.conditions.ConditionRandom;
import joshie.crafting.crafting.CraftingRegistry;
import joshie.crafting.json.Options;
import joshie.crafting.lib.CraftingInfo;
import joshie.crafting.network.PacketClaimed;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketReload;
import joshie.crafting.network.PacketReset;
import joshie.crafting.network.PacketRewardItem;
import joshie.crafting.network.PacketSyncAbilities;
import joshie.crafting.network.PacketSyncCriteria;
import joshie.crafting.network.PacketSyncJSON;
import joshie.crafting.network.PacketSyncTriggers;
import joshie.crafting.player.PlayerSavedData;
import joshie.crafting.player.PlayerTracker;
import joshie.crafting.rewards.RewardCommand;
import joshie.crafting.rewards.RewardCrafting;
import joshie.crafting.rewards.RewardCriteria;
import joshie.crafting.rewards.RewardFallDamage;
import joshie.crafting.rewards.RewardItem;
import joshie.crafting.rewards.RewardPoints;
import joshie.crafting.rewards.RewardResearch;
import joshie.crafting.rewards.RewardSpeed;
import joshie.crafting.rewards.RewardTime;
import joshie.crafting.trigger.TriggerBreakBlock;
import joshie.crafting.trigger.TriggerChangeDimension;
import joshie.crafting.trigger.TriggerClickBlock;
import joshie.crafting.trigger.TriggerCrafting;
import joshie.crafting.trigger.TriggerKill;
import joshie.crafting.trigger.TriggerLogin;
import joshie.crafting.trigger.TriggerObtain;
import joshie.crafting.trigger.TriggerPoints;
import joshie.crafting.trigger.TriggerResearch;
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
public class CraftingMod {
    public static final Logger logger = LogManager.getLogger(MODNAME);

    @SidedProxy(clientSide = JAVAPATH + "CraftingClient", serverSide = JAVAPATH + "CraftingCommon")
    public static CraftingCommon proxy;

    @Instance(MODID)
    public static CraftingMod instance;
    public static File configDir;

    public static PlayerSavedData data = new PlayerSavedData(MODNAME);
    public static boolean NEI_LOADED = false;
    public static Item item;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Initialise the CraftingEvent
        try {
            Class.forName("joshie.crafting.api.crafting.CraftingEvent$CraftingType");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /** Create the config directory **/
        File dir = new File("config" + File.separator + CraftingInfo.MODPATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        
        Options.init(new Configuration(new File(dir, "options.cfg")));

        CraftingRemapper.resetRegistries();
        NEI_LOADED = Loader.isModLoaded("NotEnoughItems");
        CraftingAPI.registry = new CraftAPIRegistry();
        CraftingAPI.crafting = new CraftingRegistry();
        CraftingAPI.commands = CommandManager.INSTANCE;

        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(CraftingAPI.commands);
        MinecraftForge.EVENT_BUS.register(new CraftingEventsHandler());
        FMLCommonHandler.instance().bus().register(new CraftingEventsHandler());
        
        item = new ItemCriteria().setUnlocalizedName("item");
        GameRegistry.registerItem(item, "item");
        
        if (Options.tileClaimerRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(item, 1, ItemCriteria.CLAIM), new Object[] { "F", "P", 'F', Items.flint, 'P', "plankWood" }));
        }

        CraftingAPI.registry.registerConditionType(new ConditionBiomeType());
        CraftingAPI.registry.registerConditionType(new ConditionRandom());
        CraftingAPI.registry.registerConditionType(new ConditionCoordinates());
        CraftingAPI.registry.registerConditionType(new ConditionDaytime());
        CraftingAPI.registry.registerConditionType(new ConditionInInventory());
        
        CraftingAPI.registry.registerRewardType(new RewardCommand());
        CraftingAPI.registry.registerRewardType(new RewardCrafting());
        CraftingAPI.registry.registerRewardType(new RewardCriteria());
        CraftingAPI.registry.registerRewardType(new RewardFallDamage());
        CraftingAPI.registry.registerRewardType(new RewardItem());
        CraftingAPI.registry.registerRewardType(new RewardResearch());
        CraftingAPI.registry.registerRewardType(new RewardPoints());
        CraftingAPI.registry.registerRewardType(new RewardSpeed());
        CraftingAPI.registry.registerRewardType(new RewardTime()); 

        CraftingAPI.registry.registerTriggerType(new TriggerBreakBlock());
        CraftingAPI.registry.registerTriggerType(new TriggerCrafting());
        CraftingAPI.registry.registerTriggerType(new TriggerKill());
        CraftingAPI.registry.registerTriggerType(new TriggerLogin());
        CraftingAPI.registry.registerTriggerType(new TriggerObtain());
        CraftingAPI.registry.registerTriggerType(new TriggerResearch());
        CraftingAPI.registry.registerTriggerType(new TriggerClickBlock());
        CraftingAPI.registry.registerTriggerType(new TriggerPoints());
        CraftingAPI.registry.registerTriggerType(new TriggerChangeDimension());

        CraftingAPI.commands.registerCommand(new CommandHelp());
        CraftingAPI.commands.registerCommand(new CommandEdit());
        CraftingAPI.commands.registerCommand(new CommandReload());
        CraftingAPI.commands.registerCommand(new CommandReset());

        PacketHandler.registerPacket(PacketSyncTriggers.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCriteria.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncAbilities.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketRewardItem.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketClaimed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncJSON.class);
        PacketHandler.registerPacket(PacketReload.class);
        PacketHandler.registerPacket(PacketReset.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new CraftingGUIHandler());
        proxy.initClient();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ICommandManager manager = event.getServer().getCommandManager();
        if (manager instanceof ServerCommandManager) {
            ((ServerCommandManager) manager).registerCommand(CraftingAPI.commands);
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;

        //Remap all relevant data
        CraftingRemapper.reloadServerData();
        
        World world = MinecraftServer.getServer().worldServers[0];
        data = (PlayerSavedData) world.loadItemData(PlayerSavedData.class, MODNAME);
        if (data == null) {
            createWorldData();
        }
    }
    
    @EventHandler
    public void onServerStarting(FMLServerStoppedEvent event) {
        CraftingRemapper.resetRegistries();
    }

    public void createWorldData() {
        World world = MinecraftServer.getServer().worldServers[0];
        data = new PlayerSavedData(MODNAME);
        world.setItemData(MODNAME, data);
    }
}
