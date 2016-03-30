package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.filters.FilterSelectorBlock;
import joshie.progression.gui.filters.FilterSelectorLocation;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class RewardPlaceBlock extends RewardBaseItemFilter implements ISpecialFieldProvider, ISpecialFilters {
    public List<IProgressionFilter> locations = new ArrayList();

    public RewardPlaceBlock() {
        super("placeBlock", 0xFF00680A);
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
            fields.add(new ItemFilterFieldPreview("filters", this, 25, 30, 2.8F));
        }
    }

    @Override
    public IProgressionFilterSelector getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return FilterSelectorLocation.INSTANCE;
        if (fieldName.equals("filters")) return FilterSelectorBlock.INSTANCE;

        return null;
    }

    @Override
    public List<IProgressionFilter> getAllFilters() {
        ArrayList<IProgressionFilter> all = new ArrayList();
        all.addAll(locations);
        all.addAll(filters);
        return all;
    }

    @Override
    public boolean shouldRunOnce() {
        return true;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        WorldLocation location = WorldLocation.getRandomLocationFromFilters(locations, player);
        if (location != null) {
            World world = DimensionManager.getWorld(location.dimension);

            ItemStack stack = ItemHelper.getRandomBlock(filters);
            Block block = ItemHelper.getBlock(stack);
            int damage = stack.getItemDamage();
            world.setBlockState(location.pos, block.getStateFromMeta(damage));
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + Progression.translate("block.place"));
        list.add(getIcon().getDisplayName());
    }
}
