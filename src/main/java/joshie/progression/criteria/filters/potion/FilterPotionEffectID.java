package joshie.progression.criteria.filters.potion;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.criteria.filters.FilterBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FilterPotionEffectID extends FilterBase {
    public int effectID = 1;

    public FilterPotionEffectID() {
        super("potioneffect", 0xFFFF73FF);
    }

    @Override
    public IProgressionFilterSelector getType() {
        return ProgressionAPI.filters.getPotionFilter();
    }

    private Set<Integer> getIds(Collection<PotionEffect> list) {
        Set<Integer> ids = new HashSet();
        for (PotionEffect check : list)
            ids.add(check.getPotionID());
        return ids;
    }

    @Override
    public boolean matches(Object object) {
        if (object instanceof Collection) {
            return getIds((Collection<PotionEffect>)object).contains(effectID);
        }

        return false;
    }

    @Override
    public Object getRandom(EntityPlayer player) {
        return null;
    }
}
