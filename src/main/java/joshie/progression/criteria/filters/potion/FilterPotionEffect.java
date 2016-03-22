package joshie.progression.criteria.filters.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.criteria.filters.item.FilterBaseItem;
import joshie.progression.gui.editors.FeatureItemSelector.Type;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.filters.FilterSelectorPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class FilterPotionEffect extends FilterBaseItem implements IInit, ISpecialFieldProvider {
    private static final List<PotionEffect> EMPTY = new ArrayList();
    public ItemStack stack = new ItemStack(Items.potionitem, 1, 16385); //Splash Potion of Regen, 33 seconds
    private List<PotionEffect> effects;
    private Set<Integer> ids;

    public FilterPotionEffect() {
        super("potioneffect", 0xFFFF73FF);
    }

    @Override
    public FilterType getType() {
        return FilterType.POTIONEFFECT;
    }

    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("stack");
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(new ItemField("stack", this, 25, 25, 3F, Type.TRIGGER, FilterSelectorPotion.INSTANCE));
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
