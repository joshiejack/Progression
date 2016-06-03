package joshie.progression.criteria.filters.entity;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;

@ProgressionRule(name="entityMod", color=0xFF2791C1)
public class FilterEntityMod extends FilterBaseEntity {
    public String modid = "minecraft";

    @Override
    public boolean matches(EntityLivingBase check) {
        if (modid.equals("*")) return true;
        return EntityHelper.getModFromEntity(check).equals(modid);
    }
}