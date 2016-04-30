package joshie.progression.criteria.filters.potion;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@ProgressionRule(name="potionitem", color=0xFFFF73FF)
public class FilterPotionItem extends FilterPotionBase implements IInit, ISpecialFieldProvider {
    public ItemStack stack = new ItemStack(Items.POTIONITEM, 1, 16385); //Splash Potion of Regen, 33 seconds
    private Set<Potion> ids;

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "stack", 25, 25, 3F));
    }

    @Override
    public void init(boolean isClient) {
        ids = getIds(getEffects(stack.getTagCompound()));
    }

    @Override
    public boolean matches(PotionEffect effect) {
        return ids == null? false: (ids.contains(effect.getPotion()));
    }

    @Override
    public Collection<PotionEffect> getRandomEffects() {
        return getEffects(stack.getTagCompound());
    }
}
