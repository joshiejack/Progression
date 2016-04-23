package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import joshie.progression.criteria.filters.potion.FilterPotionBase;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.filters.FilterTypePotion;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;
import java.util.List;

@ProgressionRule(name="potioneffect", color=0xFF2C7373)
public class RewardPotion extends RewardBaseItemFilter implements ICustomDescription, ICustomWidth, ICustomTooltip, ISpecialFieldProvider {
    public boolean randomVanilla = false;
    public int customid = -1;
    public int duration = 200;
    public int amplifier = 0;
    public boolean particles = false;

    public RewardPotion() {
        BROKEN = new ItemStack(Items.potionitem, 1, 0);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 55;
    }

    @Override
    public void addTooltip(List list) {
        ItemStack stack = preview == null ? BROKEN : preview;
        Items.potionitem.addInformation(stack, MCClientHelper.getPlayer(), list, false);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.DISPLAY) fields.add(new ItemFilterFieldPreview("filters", this, 5, 25, 2.8F));
        else fields.add(new ItemFilterField("filters", this));
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return FilterTypePotion.INSTANCE;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (player != null) {
            Collection<PotionEffect> effects = FilterPotionBase.getRandomEffects(filters);
            if (effects != null && effects.size() > 0) {
                for (PotionEffect effect : effects) {
                    if (randomVanilla) player.addPotionEffect(new PotionEffect(effect));
                    else {
                        int id = customid >= 0 ? customid : effect.getPotionID();
                        player.addPotionEffect(new PotionEffect(id, duration, amplifier, false, particles));
                    }
                }
            }
        }
    }
}
