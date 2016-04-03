package joshie.progression.criteria.filters.potion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collection;

public class FilterPotionEffectID extends FilterPotionBase {
    public int effectID = 1;

    public FilterPotionEffectID() {
        super("potioneffect", 0xFFFF73FF);
    }

    @Override
    public boolean matches(PotionEffect effect) {
        return effect.getPotionID() == effectID;
    }

    @Override
    public Collection<PotionEffect> getRandom(EntityPlayer player) {
        return Arrays.asList(new PotionEffect(effectID, 200, 0));
    }
}
