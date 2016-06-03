package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.*;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ProgressionRule(name="hurt", color=0xFFE599FF, meta="attackPlayer")
public class RewardHurt extends RewardBase implements IInit, ICustomDescription, ICustomWidth, IGetterCallback, IHasFilters, ISpecialFieldProvider {
    public static final HashMap<String, DamageSource> sources = new HashMap();
    public List<IFilterProvider> targets = new ArrayList();
    private DamageSource source;
    public String damageSource = "magic";
    public float damage = 1F;
    public boolean defaultToPlayer = true;

    @Override
    public void init(boolean isClient) {
        if (damageSource.equals("magic")) source = DamageSource.magic;
        else {
            source = sources.get(damageSource);
        }
    }

    @Override
    public String getDescription() {
        if (source != null) {
            String s = TextFormatting.GREEN + WordUtils.capitalizeFully(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(source.damageType), ' '));
            return Progression.format(getProvider().getUnlocalisedName() + ".description", TextFormatting.RED + "" + damage + TextFormatting.WHITE, s);
        } else return "Incorrectly setup damage source";
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 85 : 80;
    }

    @Override
    public String getField(String fieldName) {
        return fieldName.equals("damageSource") ? (source != null ? TextFormatting.GREEN + damageSource : TextFormatting.RED + damageSource) : "" + damage;
    }

    @Override
    public List<IFilterProvider> getAllFilters() {
        return targets;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return FilterTypeEntity.INSTANCE;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        fields.add(new ItemFilterField("targets", this));
    }

    @Override
    public void reward(EntityPlayerMP thePlayer) {
        if (source == null) return;
        IFilter filter = EntityHelper.getFilter(targets, thePlayer);
        if (filter != null) {
            List<EntityLivingBase> entities = (List<EntityLivingBase>) filter.getRandom(thePlayer);
            if (entities.size() == 0 && defaultToPlayer) entities.add(thePlayer);
            for (EntityLivingBase entity : entities) {
                entity.attackEntityFrom(source, damage);
            }
        }
    }
}