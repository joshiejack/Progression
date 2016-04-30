package joshie.progression.helpers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class BlockActionHelper {
    private static HashMap<Block, Item> block_item_mappings = new HashMap();
    static {
        block_item_mappings.put(Blocks.CAULDRON, Items.CAULDRON);
        block_item_mappings.put(Blocks.REEDS, Items.SUGAR);
        block_item_mappings.put(Blocks.WHEAT, Items.WHEAT);
        block_item_mappings.put(Blocks.STANDING_SIGN, Items.SIGN);
        block_item_mappings.put(Blocks.CAKE, Items.CAKE);
        block_item_mappings.put(Blocks.FLOWER_POT, Items.FLOWER_POT);
        block_item_mappings.put(Blocks.OAK_DOOR, Items.OAK_DOOR);
        block_item_mappings.put(Blocks.SPRUCE_DOOR, Items.SPRUCE_DOOR);
        block_item_mappings.put(Blocks.BIRCH_DOOR, Items.BIRCH_DOOR);
        block_item_mappings.put(Blocks.JUNGLE_DOOR, Items.JUNGLE_DOOR);
        block_item_mappings.put(Blocks.DARK_OAK_DOOR, Items.DARK_OAK_DOOR);
        block_item_mappings.put(Blocks.ACACIA_DOOR, Items.ACACIA_DOOR);
        block_item_mappings.put(Blocks.IRON_DOOR, Items.IRON_DOOR);
    }

    public static ItemStack getStackFromBlockData(Block block, int meta) {
        Item item = Item.getItemFromBlock(block);
        if (item == null) {
            item = block_item_mappings.get(block);
        }

        return item == null ? null : new ItemStack(item, 1, meta);
    }
}
