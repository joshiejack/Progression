package joshie.progression.criteria.filters.potion;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

import java.util.*;

public abstract class FilterPotionBase implements IFilter<ItemStack, ItemStack> {
    protected static final Random rand = new Random();
    protected static final List<PotionEffect> EMPTY = new ArrayList();

    private IFilterProvider provider;

    @Override
    public IFilterProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(IFilterProvider provider) {
        this.provider = provider;
    }

    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getPotionFilter();
    }

    @Override
    public ItemStack getRandom(EntityPlayer player) {
        return ItemHelper.getRandomItem(this.getProvider());
    }

    @Override
    public void apply(ItemStack stack) {}

    protected Set<Potion> getIds(Collection<PotionEffect> list) {
        Set<Potion> ids = new HashSet();
        for (PotionEffect check : list)
            ids.add(check.getPotion());
        return ids;
    }

    protected List<PotionEffect> getEffects(NBTTagCompound tagCompound) {
        List<PotionEffect> effects = PotionUtils.getEffectsFromTag(tagCompound);
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
        } else if (object instanceof ItemStack) {
            return matches(getEffects(((ItemStack)object).getTagCompound()));
        } else if (object instanceof PotionEffect) {
            return matches(((PotionEffect)object));
        }

        return false;
    }

    public abstract Collection<PotionEffect> getRandomEffects();

    public static Collection<PotionEffect> getRandomEffects(List<IFilterProvider> filters) {
        int size = filters.size();
        if (size == 0) return null;
        if (size == 1) return ((FilterPotionBase)filters.get(0).getProvided()).getRandomEffects();
        else {
            return ((FilterPotionBase)filters.get(rand.nextInt(size)).getProvided()).getRandomEffects();
        }
    }
}