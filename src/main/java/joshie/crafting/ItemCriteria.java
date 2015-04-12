package joshie.crafting;

import java.util.List;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCriteria extends Item {
    public static CreativeTabs tab = new CreativeTabs("progression") {
        @Override
        public Item getTabIconItem() {
            return Items.book;
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    };

    public ItemCriteria() {
        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(tab);
    }

    public static ICriteria getCriteriaFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        return CraftingAPI.registry.getCriteriaFromName(stack.getTagCompound().getString("Criteria"));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ICriteria criteria = getCriteriaFromStack(stack);
        return criteria == null ? "BROKEN ITEM" : criteria.getDisplayName();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            ICriteria criteria = getCriteriaFromStack(stack);
            if (criteria != null) {
                boolean completed = CraftingAPI.players.getServerPlayer(PlayerHelper.getUUIDForPlayer(player)).getMappings().fireAllTriggers("forced-complete", criteria);
                if (!player.capabilities.isCreativeMode && completed) {
                    stack.stackSize--;
                }
            }
        }

        return stack;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(Items.book));
        for (ICriteria c : CraftingAPI.registry.getCriteria()) {
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("Criteria", c.getUniqueName());
            list.add(stack);
        }
    }
}
