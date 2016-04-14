package joshie.progression.helpers;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockActionHelper {
    private static HashMap<Block, Item> block_item_mappings = new HashMap();
    static {
        block_item_mappings.put(Blocks.cauldron, Items.cauldron);
        block_item_mappings.put(Blocks.reeds, Items.sugar);
        block_item_mappings.put(Blocks.wheat, Items.wheat_seeds);
        block_item_mappings.put(Blocks.standing_sign, Items.sign);
        block_item_mappings.put(Blocks.cake, Items.cake);
        block_item_mappings.put(Blocks.flower_pot, Items.flower_pot);
        block_item_mappings.put(Blocks.oak_door, Items.oak_door);
        block_item_mappings.put(Blocks.spruce_door, Items.spruce_door);
        block_item_mappings.put(Blocks.birch_door, Items.birch_door);
        block_item_mappings.put(Blocks.jungle_door, Items.jungle_door);
        block_item_mappings.put(Blocks.dark_oak_door, Items.dark_oak_door);
        block_item_mappings.put(Blocks.acacia_door, Items.acacia_door);
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
