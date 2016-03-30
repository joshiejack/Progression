package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterSelectorItem;
import joshie.progression.gui.filters.FilterSelectorLocation;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RewardSpawnItem extends RewardBaseItemFilter implements ISpecialFilters, IHasFilters, ISpecialFieldProvider {
    public List<IProgressionFilter> locations = new ArrayList();
    public int stackSize = 1;

    public RewardSpawnItem() {
        super("spawnItem", 0xFFE599FF);
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
            fields.add(new ItemFilterField("filters", this));
        }
    }
    
    @Override
    public List<IProgressionFilter> getAllFilters() {
        ArrayList<IProgressionFilter> all = new ArrayList();
        all.addAll(locations);
        all.addAll(filters);
        return all;
    }

    private boolean isValidLocation(World world, BlockPos pos) {
        Material posfloor = world.getBlockState(pos).getBlock().getMaterial();
        Material posfeet = world.getBlockState(pos.up()).getBlock().getMaterial();
        Material poshead = world.getBlockState(pos.up(2)).getBlock().getMaterial();
        if (posfeet.blocksMovement()) return false;
        if (poshead.blocksMovement()) return false;
        if (posfloor.isLiquid() || posfeet.isLiquid() || poshead.isLiquid()) return false;
        return posfloor.blocksMovement();
    }
    
    @Override
    public void reward(EntityPlayerMP player) {
        boolean notspawned = true;
        for (int j = 0; j < 10 && notspawned; j++) {
            WorldLocation location = WorldLocation.getRandomLocationFromFilters(locations, player);
            if (location != null) {
                BlockPos pos = new BlockPos(location.pos);
                if (player.worldObj.isBlockLoaded(pos)) {
                    if (isValidLocation(player.worldObj, pos)) {
                        notspawned = false;
                        SpawnItemHelper.spawnItem(player.worldObj, pos.getX(), pos.getY(), pos.getZ(), ItemHelper.getRandomItemOfSize(filters, stackSize));
                    }
                }
            }
        }
    }

    @Override
    public void addTooltip(List list) {
        // list.add(EnumChatFormatting.WHITE + Progression.translate("item.free"));
        // list.add(getIcon().getDisplayName() + " x" + spawnNumber);
    }

    @Override
    public IProgressionFilterSelector getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return FilterSelectorLocation.INSTANCE;
        if (fieldName.equals("filters")) return FilterSelectorItem.INSTANCE;

        return null;
    }
}
