package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.filters.FilterTypeBlock;
import joshie.progression.gui.filters.FilterTypeLocation;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class RewardPlaceBlock extends RewardBaseItemFilter implements ISpecialFieldProvider, ISpecialFilters {
    public List<IFilter> locations = new ArrayList();
    protected transient IFilter locationpreview;
    protected transient int locationticker;

    public RewardPlaceBlock() {
        super("placeBlock", 0xFF00680A);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
            fields.add(new ItemFilterFieldPreview("filters", this, 25, 40, 2F));
        } else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 42, 1.9F));
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return FilterTypeLocation.INSTANCE;
        if (fieldName.equals("filters")) return FilterTypeBlock.INSTANCE;

        return null;
    }

    @Override
    public List<IFilter> getAllFilters() {
        ArrayList<IFilter> all = new ArrayList();
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
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 116: super.getWidth(mode);
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + Progression.translate("block.place"));
        list.add(getIcon().getDisplayName());
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
        return Progression.translate("reward.placeBlock.description") + " \n" + getFilter();
    }
}
