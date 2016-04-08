package joshie.progression.criteria.filters.potion;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@ProgressionRule(name="potionitem", color=0xFFFF73FF)
public class FilterPotionItem extends FilterPotionBase implements IInit, ISpecialFieldProvider {
    private static final List<PotionEffect> EMPTY = new ArrayList();
    public ItemStack stack = new ItemStack(Items.potionitem, 1, 16385); //Splash Potion of Regen, 33 seconds
    private Set<Integer> ids;

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "stack", 25, 25, 3F));
    }

    @Override
    public void init(boolean isClient) {
        ids = getIds(getEffects(stack.getItemDamage()));
    }

    @Override
    public boolean matches(PotionEffect effect) {
        return (ids.contains(effect.getPotionID()));
    }

    @Override
    public Collection<PotionEffect> getRandom(EntityPlayer player) {
        return getEffects(stack.getItemDamage());
    }
}
