package joshie.crafting;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.crafting.CraftingRegistry;
import joshie.crafting.json.JSONLoader;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketSyncConditions;
import joshie.crafting.network.PacketSyncKillings;
import joshie.crafting.network.PacketSyncResearch;
import joshie.crafting.network.PacketSyncSpeed;
import joshie.crafting.player.PlayerTracker;
import joshie.crafting.rewards.RewardSpeed;
import joshie.crafting.trigger.TriggerKill;
import joshie.crafting.trigger.TriggerResearch;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CraftingCommon {
	public static ITrigger triggerKill;
	public static ITrigger triggerResearch;
	
	public static Item tech;
	
	public void preInit() {
		CraftingAPI.registry = new CraftAPIRegistry();
		CraftingAPI.players = new PlayerTracker();
		CraftingAPI.crafting = new CraftingRegistry().init();
		MinecraftForge.EVENT_BUS.register(CraftingAPI.players);
		MinecraftForge.EVENT_BUS.register(new CraftingEventsHandler());
		
		CraftingAPI.registry.registerRewardType(new RewardSpeed());
		triggerKill = CraftingAPI.registry.registerTriggerType(new TriggerKill());
		triggerResearch = CraftingAPI.registry.registerTriggerType(new TriggerResearch());
		
		tech = new ItemTechnology().setUnlocalizedName("technology").setCreativeTab(CreativeTabs.tabRedstone);
		GameRegistry.registerItem(tech, "technology");
		
		PacketHandler.registerPacket(PacketSyncKillings.class, Side.CLIENT);
		PacketHandler.registerPacket(PacketSyncResearch.class, Side.CLIENT);
		PacketHandler.registerPacket(PacketSyncConditions.class, Side.CLIENT);
		PacketHandler.registerPacket(PacketSyncSpeed.class, Side.CLIENT);
		
		JSONLoader.loadJSON();
	}
}
