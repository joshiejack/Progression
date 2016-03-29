package joshie.progression.helpers;

import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.gui.filters.FilterSelectorBlock;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
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
        Iterator iterator = Item.itemRegistry.iterator();
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

    public static Block getBlock(ItemStack check) {
        return isBlock(check) ? Block.getBlockFromItem(check.getItem()) : null;
    }

    private static boolean isBlock(ItemStack stack) {
        Block block = null;
        int meta = 0;
        try {
            block = Block.getBlockFromItem(stack.getItem());
            meta = stack.getItemDamage();
        } catch (Exception e) {}

        return block != null;
    }

    public static ItemStack getRandomItem(IProgressionFilter filter) {
        if (shuffledItemsCache == null) shuffledItemsCache = new ArrayList(getCreativeItems());
        Collections.shuffle(shuffledItemsCache);
        for (ItemStack stack : shuffledItemsCache) {
            if (filter.getType() != null && !filter.getType().isAcceptable(stack)) continue;
            if (filter.matches(stack)) return stack;
        }

        //In theory if set up correctly this should be no issue
        return null;
    }

    public static ItemStack getRandomItem(List<IProgressionFilter> filters) {
        return getRandomItem(filters, null);
    }

    public static ItemStack getRandomItemOfSize(List<IProgressionFilter> filters, int stackSize) {
        ItemStack item = getRandomItem(filters, null);
        if (item == null) return null;
        else item = item.copy();
        item.stackSize = stackSize;
        return item;
    }

    public static ItemStack getRandomBlock(List<IProgressionFilter> filters) {
        return getRandomItem(filters, FilterSelectorBlock.INSTANCE);
    }

    public static ItemStack getRandomItem(List<IProgressionFilter> filters, IProgressionFilterSelector selector) {
        ArrayList<IProgressionFilter> shuffledFilters = new ArrayList(filters);
        if (shuffledItemsCache == null) shuffledItemsCache = new ArrayList(getCreativeItems());
        Collections.shuffle(shuffledItemsCache);
        Collections.shuffle(shuffledFilters);
        for (ItemStack stack : shuffledItemsCache) {
            if (selector != null && !selector.isAcceptable(stack)) continue;
            for (IProgressionFilter filter : shuffledFilters) {
                if (filter.matches(stack)) return stack;
            }
        }

        //In theory if set up correctly this should be no issue
        return null;
    }

    public static List<ItemStack> getAllMatchingItems(IProgressionFilter filter) {
        ArrayList<ItemStack> stacks = new ArrayList();
        for (ItemStack stack : getAllItems()) {
            if (filter.matches(stack)) {
                stacks.add(stack.copy());
            }
        }

        return stacks;
    }

    public static List<ItemStack> getMatches(IProgressionFilter filter) {
        return ItemHelper.getAllMatchingItems(filter);
    }

    public static List<ItemStack> getAllMatchingItems(List<IProgressionFilter> filters) {
        ArrayList<ItemStack> stacks = new ArrayList();
        for (IProgressionFilter filter : filters) {
            stacks.addAll(getMatches(filter));
        }

        return stacks;
    }
}
