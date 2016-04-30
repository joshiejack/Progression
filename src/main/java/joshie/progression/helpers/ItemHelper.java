package joshie.progression.helpers;

import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ItemHelper {
    private static final ArrayList<ItemStack> itemsWithoutInventory = new ArrayList();
    private static final ArrayList<ItemStack> itemsWithInventory = new ArrayList();
    private static ArrayList<ItemStack> shuffledItemsCache;


    static {
        Iterator iterator = Item.REGISTRY.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item == null) {
                continue;
            }

            if (item.getCreativeTabs() != null && item.getCreativeTabs().length > 0) {
                for (CreativeTabs tab : item.getCreativeTabs()) {
                    try {
                        item.getSubItems(item, tab, itemsWithInventory);
                        item.getSubItems(item, tab, itemsWithoutInventory);
                    } catch (Exception e) {}
                }
            }
        }
    }

    public static ArrayList<ItemStack> getAllItems() {
        return itemsWithInventory;
    }

    public static ArrayList<ItemStack> getCreativeItems() {
        return itemsWithoutInventory;
    }

    public static void addInventory() {
        for (ItemStack stack : MCClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                if (!itemsWithInventory.contains(stack)) {
                    itemsWithInventory.add(stack);
                }
            }
        }
    }

    public static ItemStack getRandomItem(IFilterProvider filter) {
        return getRandomItem(filter, filter.getProvided().getType());
    }

    private static ItemStack getRandomItem(IFilterProvider filter, IFilterType selector) {
        if (shuffledItemsCache == null) shuffledItemsCache = new ArrayList(getCreativeItems());
        Collections.shuffle(shuffledItemsCache);
        for (ItemStack stack : shuffledItemsCache) {
            if (selector != null && !selector.isAcceptable(stack)) continue;
            if (filter.getProvided().matches(stack)) return stack;
        }

        //In theory if set up correctly this should be no issue
        return null;
    }

    public static ItemStack getRandomItemFromFilters(List<IFilterProvider> filters, EntityPlayer player) {
        int size = filters.size();
        if (size == 0) return null;
        if (size == 1) return (ItemStack) filters.get(0).getProvided().getRandom(player);
        else {
            return (ItemStack) filters.get(player.worldObj.rand.nextInt(size)).getProvided().getRandom(player);
        }
    }

    public static ItemStack getRandomItemOfSize(List<IFilterProvider> filters, EntityPlayer player, int stackSize) {
        ItemStack item = getRandomItemFromFilters(filters, player).copy();
        if (item == null) return null;
        else item = item.copy();
        item.stackSize = stackSize;
        return item;
    }
}