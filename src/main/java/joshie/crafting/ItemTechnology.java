package joshie.crafting;

import java.util.List;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.trigger.TriggerResearch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTechnology extends Item {
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return stack.hasTagCompound()? stack.getTagCompound().getString("Research"): "Corrupt";
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {		
		if (stack.hasTagCompound()) {
			if (!world.isRemote) {
				if(CraftingAPI.players.getServerPlayer(PlayerHelper.getUUIDForPlayer(player)).getMappings().fireAllTriggers("research", stack.getTagCompound().getString("Research"))) {
					if (!player.capabilities.isCreativeMode) {
						stack.stackSize--;
					}
				}
			}
		}
		
		return stack;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (ITrigger research: CraftingAPI.registry.getTechnology()) {
			if (research instanceof TriggerResearch) {
				ItemStack stack = new ItemStack(item);
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setString("Research", ((TriggerResearch)research).getResearchName());
				list.add(stack);
			}
		}
    }
}
