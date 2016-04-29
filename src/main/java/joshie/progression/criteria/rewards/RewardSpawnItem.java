package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.gui.filters.FilterTypeLocation;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.DARK_GREEN;

@ProgressionRule(name="spawnItem", color=0xFFE599FF)
public class RewardSpawnItem extends RewardBaseItemFilter implements ICustomDescription, ICustomWidth, ICustomTooltip, IHasFilters, ISpecialFieldProvider, IRequestItem {
    public List<IFilterProvider> locations = new ArrayList();
    public int stackSize = 1;

    protected transient IField field;

    public RewardSpawnItem() {
        field = new ItemFilterField("locations", this);
    }

    @Override
    public String getDescription() {
        return Progression.format(getProvider().getUnlocalisedName() + ".description", stackSize) + " \n" + field.getField();
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 111: 100;
    }

    @Override
    public void addTooltip(List list) {
        list.add(DARK_GREEN + format(stackSize));
        list.addAll(Arrays.asList(WordUtils.wrap((String)field.getField(), 28).split("\r\n")));
        ItemStack stack = getIcon();
        if (stack != null) {
            list.add("---");
            list.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
        }
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
            fields.add(new ItemFilterField("filters", this));
        } else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 42, 1.9F));
    }
    
    @Override
    public List<IFilterProvider> getAllFilters() {
        ArrayList<IFilterProvider> all = new ArrayList();
        all.addAll(locations);
        all.addAll(filters);
        return all;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return FilterTypeLocation.INSTANCE;
        if (fieldName.equals("filters")) return FilterTypeItem.INSTANCE;

        return null;
    }

    @Override
    public ItemStack getRequestedStack(EntityPlayer player) {
        return ItemHelper.getRandomItemOfSize(filters, player, stackSize);
    }

    @Override
    public void reward(EntityPlayer player, ItemStack stack) {
        if (stack != null) {
            for (IFilterProvider filter: filters) {
                if (filter.getProvided().matches(stack)) {
                    for (int j = 0; j < 10; j++) {
                        WorldLocation location = WorldLocation.getRandomLocationFromFilters(locations, player);
                        if (location != null) {
                            BlockPos pos = new BlockPos(location.pos);
                            if (player.worldObj.isBlockLoaded(pos)) {
                                if (isValidLocation(player.worldObj, pos)) {
                                    pos = pos.up();
                                    SpawnItemHelper.spawnItem(player.worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void reward(EntityPlayerMP player) {
        ProgressionAPI.registry.requestItem(this, player);
    }

    //Helper Methods
    private boolean isValidLocation(World world, BlockPos pos) {
        Material floor = world.getBlockState(pos).getBlock().getMaterial();
        Material feet = world.getBlockState(pos.up()).getBlock().getMaterial();
        Material head = world.getBlockState(pos.up(2)).getBlock().getMaterial();
        if (feet.blocksMovement()) return false;
        if (head.blocksMovement()) return false;
        if (floor.isLiquid() || feet.isLiquid() || head.isLiquid()) return false;
        return floor.blocksMovement();
    }
}
