package joshie.progression.criteria.filters.block;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseBlock extends FilterBase {
    @Override
    public ItemStack getRandom(EntityPlayer player) {
        return ItemHelper.getRandomItem(this.getProvider());
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

    @Override
    public boolean matches(Object object) {
        if (!(object instanceof ItemStack)) return false;
        ItemStack check = (ItemStack) object;
        Block block = getBlock(check);
        int meta = 0;
        
        try {
            meta = block.getMetaFromState(block.getStateFromMeta(check.getItemDamage()));
        } catch (Exception e) { return false; }
        
        return block == null ? false : matches(block, meta);
    }

    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getBlockFilter();
    }

    protected abstract boolean matches(Block block, int metadata);
}
