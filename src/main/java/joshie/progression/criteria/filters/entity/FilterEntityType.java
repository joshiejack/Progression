package joshie.progression.criteria.filters.entity;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IEnum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.INpc;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;

@ProgressionRule(name="entitytype", color=0xFFB25900)
public class FilterEntityType extends FilterBaseEntity implements IEnum {
    public EntityType type = EntityType.ANIMAL;

    @Override
    protected boolean matches(EntityLivingBase entity) {
        switch (type) {
            case ANIMAL:    return entity instanceof EntityAnimal;
            case MONSTER:   return entity instanceof IMob;
            case TAMEABLE:  return entity instanceof IEntityOwnable;
            case BOSS:      return entity instanceof IBossDisplayData;
            case PLAYER:    return entity instanceof EntityPlayer;
            case WATER:     return entity instanceof EntityWaterMob || entity instanceof EntityGuardian;
            case NPC:       return entity instanceof INpc;
            case GOLEM:     return entity instanceof EntityGolem;
            default:        return false;
        }
    }

    @Override
    public Enum next(String name) {
        int id = type.ordinal() + 1;
        if (id < EntityType.values().length) {
            return EntityType.values()[id];
        }

        return EntityType.values()[0];
    }

    @Override
    public boolean isEnum(String name) {
        return name.equals("type");
    }

    public static enum EntityType {
        ANIMAL, MONSTER, WATER, TAMEABLE, BOSS, PLAYER, NPC, GOLEM;
    }
}
