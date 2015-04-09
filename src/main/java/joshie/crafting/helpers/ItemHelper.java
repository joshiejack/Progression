package joshie.crafting.helpers;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHelper {
	private static final ArrayList<ItemStack> items = new ArrayList();

	static {
		Iterator iterator = Item.itemRegistry.iterator();
		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();

			if (item == null) {
				continue;
			}

			if (item.getCreativeTabs() != null
					&& item.getCreativeTabs().length > 0) {
				for (CreativeTabs tab : item.getCreativeTabs()) {
					try {
						item.getSubItems(item, tab, items);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public static ArrayList<ItemStack> getItems() {
		return items;
	}

	public static void addInventory() {
		for (ItemStack stack : ClientHelper.getPlayer().inventory.mainInventory) {
			if (stack != null) {
				if (!items.contains(stack)) {
					items.add(stack);
				}
			}
		}
	}
}
