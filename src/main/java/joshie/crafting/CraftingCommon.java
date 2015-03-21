package joshie.crafting;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.implementation.OwnerTracker;
import joshie.crafting.implementation.conditions.ConditionTechnology;
import joshie.crafting.implementation.conditions.Conditions;
import joshie.crafting.implementation.tech.TechRegistry;
import joshie.crafting.json.JSONLoader;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketSyncTechnology;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CraftingCommon {
	public static Item tech;
	
	public void preInit() {
		CraftingAPI.tracker = new OwnerTracker();
		CraftingAPI.conditions = new Conditions();
		CraftingAPI.tech = new TechRegistry();
		MinecraftForge.EVENT_BUS.register(CraftingAPI.tracker);
		
		CraftingAPI.conditions.registerCondition(new ConditionTechnology());
		
		tech = new ItemTechnology().setUnlocalizedName("technology").setCreativeTab(CreativeTabs.tabRedstone);
		GameRegistry.registerItem(tech, "technology");
		
		PacketHandler.registerPacket(PacketSyncTechnology.class, Side.CLIENT);
		
		JSONLoader.loadDataFromJSON();
	}
}
