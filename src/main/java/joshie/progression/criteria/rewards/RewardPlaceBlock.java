package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.Progression;
import joshie.progression.api.IBlocksOnly;
import joshie.progression.api.IField;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class RewardPlaceBlock extends RewardBaseItemFilter implements ISpecialFieldProvider, IBlocksOnly {
    public int dimension = 0;
    public int x = 0;
    public int y = 64;
    public int z = 0;

    public RewardPlaceBlock() {
        super("placeBlock", 0xFF00680A);
    }

    @Override
    public void addSpecialFields(List<IField> fields) {
        fields.add(new ItemFilterFieldPreview("filters", this, 25, 30, 26, 70, 25, 75, 2.8F));
    }

    @Override
    public void reward(UUID uuid) {
        World world = DimensionManager.getWorld(dimension);

        ItemStack stack = ItemHelper.getRandomBlock(filters);
        Block block = ItemHelper.getBlock(stack);
        int damage = stack.getItemDamage();
        world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(damage));
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + Progression.translate("block.place"));
        list.add(getIcon().getDisplayName());
    }
}
