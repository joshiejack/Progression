package joshie.crafting;

import java.util.List;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.tech.ITechnology;
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
		return stack.hasTagCompound()? stack.getTagCompound().getString("Technology"): "Corrupt";
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode && stack.hasTagCompound()) {
			stack.stackSize--;
		}
		
		if (stack.hasTagCompound()) {
			ITechnology tech = CraftingAPI.tech.getTechnologyFromName(stack.getTagCompound().getString("Technology"));
			System.out.println("Added Research for " + tech.getName());
			CraftingAPI.tech.research(player, tech);
		}
		
		return stack;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (ITechnology tech: CraftingAPI.tech.getTechnologies()) {
			ItemStack stack = new ItemStack(item);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("Technology", tech.getName());
			list.add(stack);
		}
    }
}
