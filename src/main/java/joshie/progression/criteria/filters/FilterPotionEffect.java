package joshie.progression.criteria.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import joshie.progression.api.IField;
import joshie.progression.api.IInitAfterRead;
import joshie.progression.api.ISetterCallback;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.PotionFilter;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class FilterPotionEffect extends FilterBaseItem implements ISetterCallback, IInitAfterRead, ISpecialFieldProvider {
    private static final List<PotionEffect> EMPTY = new ArrayList();
    public int potionid = 16385; //Splash Potion of Regen, 33 seconds
    public ItemStack item;
    private List<PotionEffect> effects;
    private Set<Integer> ids;

    public FilterPotionEffect() {
        super("potioneffect", 0xFFFF73FF);

    }

    @Override
    public void addSpecialFields(List<IField> fields) {
        fields.add(new ItemField("item", this, 25, 25, 3F, 26, 70, 25, 75, Type.TRIGGER, PotionFilter.INSTANCE));
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
        effects = getEffects(potionid);
        ids = getIds(effects);
        item = new ItemStack(Items.potionitem, 1, potionid);
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
        if (effects == null) setupEffectsItemsIDs();
        for (Integer id : getIds(effects)) {
            if (checkids.contains(id)) return true;
        }

        return false;
    }

    @Override
    public boolean matches(ItemStack check) {
        if (check.getItem() != Items.potionitem) return false;
        return matches(getEffects(check.getItemDamage()));
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        if (fieldName.equals("item")) {
            ItemStack stack = (ItemStack) object;
            potionid = stack.getItemDamage();
            setupEffectsItemsIDs();
            return true;
        }

        return false;
    }
}
