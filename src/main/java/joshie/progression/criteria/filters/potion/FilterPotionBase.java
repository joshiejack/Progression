package joshie.progression.criteria.filters.potion;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.criteria.filters.FilterBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.*;

public abstract class FilterPotionBase extends FilterBase {
    protected static final Random rand = new Random();
    protected static final List<PotionEffect> EMPTY = new ArrayList();

    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getPotionFilter();
    }

    protected Set<Integer> getIds(Collection<PotionEffect> list) {
        Set<Integer> ids = new HashSet();
        for (PotionEffect check : list)
            ids.add(check.getPotionID());
        return ids;
    }

    protected List<PotionEffect> getEffects(int metadata) {
        List<PotionEffect> effects = Items.potionitem.getEffects(metadata);
        return effects != null ? effects : EMPTY;
    }

    public abstract boolean matches(PotionEffect effect);

    private boolean matches(Collection<PotionEffect> collection) {
        for (PotionEffect effect: collection) {
            if (matches(effect)) return true;
        }

        return false;
    }

    @Override
    public boolean matches(Object object) {
        if (object instanceof Collection) {
            return matches((Collection<PotionEffect>)object);
        } else if (object instanceof ItemStack && ((ItemStack)object).getItem() == Items.potionitem) {
            return matches(getEffects(((ItemStack)object).getItemDamage()));
        } else if (object instanceof PotionEffect) {
            return matches(((PotionEffect)object));
        }

        return false;
    }

    public static Collection<PotionEffect> getRandomEffects(List<IFilterProvider> filters) {
        int size = filters.size();
        if (size == 0) return null;
        if (size == 1) return (Collection<PotionEffect>) filters.get(0).getProvided().getRandom(null);
        else {
            return (Collection<PotionEffect>) filters.get(rand.nextInt(size));
        }
    }
}
