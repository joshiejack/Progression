package joshie.progression.criteria.filters.entity;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IEnum;
import joshie.progression.helpers.ListHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.ZombieType;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

@ProgressionRule(name="zombietype", color=0xFFB25900)
public class FilterZombieType extends FilterBaseEntity implements IEnum {
    public ZombieType type = ZombieType.HUSK;

    @Override
    public List<EntityLivingBase> getRandom(EntityPlayer player) {
        return ListHelper.newArrayList(new EntityZombie(player.worldObj));
    }

    @Override
    public void apply(EntityLivingBase entity) {
        if (entity instanceof EntityZombie) {
            EntityZombie zombie = ((EntityZombie) entity);
            zombie.func_189778_a(type);
        }
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        if (!(entity instanceof EntityZombie)) return false;
        return ((EntityZombie) entity).func_189777_di() == type;
    }

    @Override
    public Enum next(String name) {
        int id = type.ordinal() + 1;
        if (id < ZombieType.values().length) {
            return ZombieType.values()[id];
        }

        return ZombieType.values()[0];
    }

    @Override
    public boolean isEnum(String name) {
        return name.equals("type");
    }
}