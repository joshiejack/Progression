package joshie.progression.criteria.filters.block;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseBlock implements IFilter<ItemStack, ItemStack> {
    private IFilterProvider provider;

    @Override
    public IFilterProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(IFilterProvider provider) {
        this.provider = provider;
    }

    @Override
    public ItemStack getRandom(EntityPlayer player) {
        return ItemHelper.getRandomItem(this.getProvider());
    }

    @Override
    public void apply(ItemStack stack) {}

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