package joshie.progression.criteria.filters.potion;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.criteria.filters.FilterBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.*;

public class FilterPotionEffectID extends FilterBase {
    private static final List<PotionEffect> EMPTY = new ArrayList();
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

    private List<PotionEffect> getEffects(int metadata) {
        List<PotionEffect> effects = Items.potionitem.getEffects(metadata);
        return effects != null ? effects : EMPTY;
    }

    @Override
    public boolean matches(Object object) {
        if (object instanceof Collection) {
            return getIds((Collection<PotionEffect>)object).contains(effectID);
        } else if (object instanceof ItemStack && ((ItemStack)object).getItem() == Items.potionitem) {
            matches(getEffects(((ItemStack)object).getItemDamage()));
        }

        return false;
    }

    @Override
    public Object getRandom(EntityPlayer player) {
        return null;
    }
}
