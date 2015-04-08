package joshie.crafting.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

	private static HashMap<Block, Item> block_item_mappings = new HashMap();
	static {
		block_item_mappings.put(Blocks.cauldron, Items.cauldron);
		block_item_mappings.put(Blocks.reeds, Items.sugar);
		block_item_mappings.put(Blocks.wheat, Items.wheat_seeds);
		block_item_mappings.put(Blocks.standing_sign, Items.sign);
		block_item_mappings.put(Blocks.cake, Items.cake);
		block_item_mappings.put(Blocks.flower_pot, Items.flower_pot);
		block_item_mappings.put(Blocks.wooden_door, Items.wooden_door);
		block_item_mappings.put(Blocks.iron_door, Items.iron_door);
	}

	public static ItemStack getStackFromBlockData(Block block, int meta) {
		Item item = Item.getItemFromBlock(block);
		if (item == null) {
			item = block_item_mappings.get(block);
		}

		return item == null ? null : new ItemStack(item, 1, meta);
	}
}
