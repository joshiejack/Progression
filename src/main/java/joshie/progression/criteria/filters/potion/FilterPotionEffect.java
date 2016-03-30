package joshie.progression.criteria.filters.potion;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.criteria.filters.item.FilterBaseItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.*;

public class FilterPotionEffect extends FilterBaseItem implements IInit, ISpecialFieldProvider {
    private static final List<PotionEffect> EMPTY = new ArrayList();
    public ItemStack stack = new ItemStack(Items.potionitem, 1, 16385); //Splash Potion of Regen, 33 seconds
    private List<PotionEffect> effects;
    private Set<Integer> ids;

    public FilterPotionEffect() {
        super("potioneffect", 0xFFFF73FF);
    }

    @Override
    public IProgressionFilterSelector getType() {
        return ProgressionAPI.filters.getPotionFilter();
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "stack", 25, 25, 3F));
    }

    @Override
    public void init() {
        setupEffectsItemsIDs();
    }

    private List<PotionEffect> getEffects(int metadata) {
        List<PotionEffect> effects = Items.potionitem.getEffects(metadata);
        return effects != null ? effects : EMPTY;
    }

    private Set<Integer> getIds(Collection<PotionEffect> list) {
        Set<Integer> ids = new HashSet();
        for (PotionEffect check : list)
            ids.add(check.getPotionID());
        return ids;
    }

    private void setupEffectsItemsIDs() {
        effects = getEffects(stack.getItemDamage());
        ids = getIds(effects);
    }

    @Override
    public boolean matches(Object object) {
        if (object instanceof Collection) {
            return matches((Collection<PotionEffect>) object);
        }

        return super.matches(object);
    }

    public boolean matches(Collection<PotionEffect> effects) {
        Set<Integer> checkids = getIds(effects);
        if (this.effects == null) setupEffectsItemsIDs();
        for (Integer id : ids) {
            if (checkids.contains(id)) return true;
        }

        return false;
    }

    @Override
    public boolean matches(ItemStack check) {
        if (check.getItem() != Items.potionitem) return false;
        return matches(getEffects(check.getItemDamage()));
    }
}
