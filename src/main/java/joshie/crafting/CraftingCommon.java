package joshie.crafting;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.commands.CommandHelp;
import joshie.crafting.commands.CommandManager;
import joshie.crafting.commands.CommandReload;
import joshie.crafting.commands.CommandReset;
import joshie.crafting.conditions.ConditionBiomeType;
import joshie.crafting.conditions.ConditionDaytime;
import joshie.crafting.crafting.CraftingRegistry;
import joshie.crafting.json.JSONLoader;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketReload;
import joshie.crafting.network.PacketReset;
import joshie.crafting.network.PacketSyncConditions;
import joshie.crafting.network.PacketSyncSpeed;
import joshie.crafting.network.PacketSyncTriggers;
import joshie.crafting.player.PlayerTracker;
import joshie.crafting.rewards.RewardCrafting;
import joshie.crafting.rewards.RewardExperience;
import joshie.crafting.rewards.RewardItem;
import joshie.crafting.rewards.RewardSpeed;
import joshie.crafting.trigger.TriggerBreakBlock;
import joshie.crafting.trigger.TriggerCrafting;
import joshie.crafting.trigger.TriggerKill;
import joshie.crafting.trigger.TriggerLogin;
import joshie.crafting.trigger.TriggerResearch;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CraftingCommon {
	public static boolean NEI_LOADED = false;	
	public static Item tech;
	
	public void preInit() {
		NEI_LOADED = Loader.isModLoaded("NotEnoughItems");
		CraftingAPI.registry = new CraftAPIRegistry();
		CraftingAPI.players = new PlayerTracker();
		CraftingAPI.crafting = new CraftingRegistry().init();
		CraftingAPI.commands = CommandManager.INSTANCE;
		MinecraftForge.EVENT_BUS.register(CraftingAPI.players);
		MinecraftForge.EVENT_BUS.register(CraftingAPI.commands);
		MinecraftForge.EVENT_BUS.register(new CraftingEventsHandler());
		FMLCommonHandler.instance().bus().register(new CraftingEventsHandler());
		
		CraftingAPI.registry.registerConditionType(new ConditionBiomeType());
		CraftingAPI.registry.registerConditionType(new ConditionDaytime());
		CraftingAPI.registry.registerRewardType(new RewardCrafting());
		CraftingAPI.registry.registerRewardType(new RewardExperience());
		CraftingAPI.registry.registerRewardType(new RewardItem());
		CraftingAPI.registry.registerRewardType(new RewardSpeed());
		CraftingAPI.registry.registerTriggerType(new TriggerBreakBlock());
		CraftingAPI.registry.registerTriggerType(new TriggerCrafting());
		CraftingAPI.registry.registerTriggerType(new TriggerKill());
		CraftingAPI.registry.registerTriggerType(new TriggerLogin());
		CraftingAPI.registry.registerTriggerType(new TriggerResearch());
		
		tech = new ItemTechnology().setUnlocalizedName("technology").setCreativeTab(CreativeTabs.tabRedstone);
		GameRegistry.registerItem(tech, "technology");
		
		CraftingAPI.commands.registerCommand(new CommandHelp());
		CraftingAPI.commands.registerCommand(new CommandReload());
		CraftingAPI.commands.registerCommand(new CommandReset());
		
		PacketHandler.registerPacket(PacketSyncTriggers.class, Side.CLIENT);
		PacketHandler.registerPacket(PacketSyncConditions.class, Side.CLIENT);
		PacketHandler.registerPacket(PacketSyncSpeed.class, Side.CLIENT);
		PacketHandler.registerPacket(PacketReload.class);
		PacketHandler.registerPacket(PacketReset.class);
		
		JSONLoader.loadJSON();
		
		if (Loader.isModLoaded("MineTweaker3")) {
			CraftingAPI.registry.loadMineTweaker3();
		}
	}
}
