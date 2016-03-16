package joshie.progression.criteria.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.IFilter;
import joshie.progression.api.ISpecialItemFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ConditionHasPotionEffect extends ConditionBase implements ISpecialItemFilter {
    public List<IFilter> filters = new ArrayList();

    public ConditionHasPotionEffect() {
        super("potioneffect", 0xFFFFFF00);
    }

    @Override
    public String[] getSpecialFilters() {
        return new String[] { "potioneffect" };
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        for (IFilter filter : filters) {
            if (filter.matches(player.getActivePotionEffects())) return true;
        }

        return false;
    }
}
