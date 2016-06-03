package joshie.progression.helpers;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {
    public static List<EntityLivingBase> newArrayList(EntityLivingBase entity) {
        return Lists.newArrayList(entity);
    }

    public static List<EntityLivingBase> newArrayList(List<EntityPlayerMP> list) {
        List<EntityLivingBase> ret = new ArrayList<EntityLivingBase>();
        ret.addAll(list);
        return ret;
    }
}
