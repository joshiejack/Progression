package joshie.progression.criteria.filters.entity;

import com.google.gson.JsonObject;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IEnum;
import joshie.progression.api.special.ISpecialJSON;
import joshie.progression.helpers.ListHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

@ProgressionRule(name="witherskeleton", color=0xFFB25900)
public class FilterSkeletonType extends FilterBaseEntity implements ISpecialJSON, IEnum {
    public SkeletonType type = SkeletonType.WITHER;

    @Override
    public List<EntityLivingBase> getRandom(EntityPlayer player) {
        return ListHelper.newArrayList(new EntitySkeleton(player.worldObj));
    }

    @Override
    public void apply(EntityLivingBase entity) {
        if (entity instanceof EntitySkeleton) {
            EntitySkeleton skeleton = ((EntitySkeleton) entity);
            skeleton.func_189768_a(type);
        }
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        if (!(entity instanceof EntitySkeleton)) return false;
        return ((EntitySkeleton) entity).func_189771_df() == type;
    }

    @Override
    public Enum next(String name) {
        int id = type.ordinal() + 1;
        if (id < SkeletonType.values().length) {
            return SkeletonType.values()[id];
        }

        return SkeletonType.values()[0];
    }

    @Override
    public boolean isEnum(String name) {
        return name.equals("type");
    }

    @Override
    public boolean onlySpecial() {
        return false;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        if (data.has("wither")) {
            if (data.get("wither").getAsBoolean()) {
                type = SkeletonType.WITHER;
            } else type = SkeletonType.NORMAL;
        }
    }

    @Override
    public void writeToJSON(JsonObject object) {

    }
}