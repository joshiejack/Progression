package joshie.progression.criteria.conditions;

import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.items.ItemCriteria;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConditionLocation extends ConditionBase {
    public List<IProgressionFilter> locations = new ArrayList();
    protected transient IProgressionFilter preview;
    protected transient int ticker;

    public ConditionLocation() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifIsAtCoordinates), "location", 0xFF000000);
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (player == null) return false;
        WorldLocation location = new WorldLocation(player.dimension, new BlockPos(player));
        location.setPlayer(player);

        for (IProgressionFilter filter : locations) {
            if (filter.matches(location)) return true;
        }

        return  false;
    }
}
