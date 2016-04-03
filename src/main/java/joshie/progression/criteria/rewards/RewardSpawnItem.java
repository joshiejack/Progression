package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.gui.filters.FilterTypeLocation;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RewardSpawnItem extends RewardBaseItemFilter implements ISpecialFilters, IHasFilters, ISpecialFieldProvider {
    public List<IFilter> locations = new ArrayList();
    public int stackSize = 1;

    protected transient IFilter locationpreview;
    protected transient int locationticker;

    public RewardSpawnItem() {
        super("spawnItem", 0xFFE599FF);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
            fields.add(new ItemFilterField("filters", this));
        } else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 42, 1.9F));
    }
    
    @Override
    public List<IFilter> getAllFilters() {
        ArrayList<IFilter> all = new ArrayList();
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

    public String getFilter() {
        if (locationticker == 0 || locationticker >= 200) {
            locationpreview = FilterBase.getRandomFilterFromFilters(locations);
            locationticker = 1;
        }

        if (!GuiScreen.isShiftKeyDown()) locationticker++;

        return locationpreview == null ? "Nowhere": locationpreview.getDescription();
    }

    @Override
    public String getDescription() {
        return Progression.format("reward.spawnItem.description", stackSize) + " \n" + getFilter();
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 111: super.getWidth(mode);
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return FilterTypeLocation.INSTANCE;
        if (fieldName.equals("filters")) return FilterTypeItem.INSTANCE;

        return null;
    }
}
