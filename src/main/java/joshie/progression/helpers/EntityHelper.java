package joshie.progression.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import joshie.progression.api.criteria.IProgressionFilter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityRabbit;

public class EntityHelper {
    private static ArrayList<EntityLivingBase> shuffledEntityCache = new ArrayList();
    private static final HashMap<String, Integer> scalings = new HashMap();

    static {
        scalings.put("EnderDragon", 5);
        scalings.put("Giant", 3);
        scalings.put("WitherBoss", 10);
        scalings.put("Ghast", 10);
    }

    public static int getSizeForString(String name) {
        if (scalings.containsKey(name)) {
            return scalings.get(name);
        } else return 15;
    }

    public static int getSizeForEntity(Entity entity) {
        return getSizeForString(EntityList.getEntityString(entity));
    }

    private static final ArrayList<EntityLivingBase> entities = new ArrayList();

    static {
        for (String name : (Set<String>) EntityList.stringToClassMapping.keySet()) {
            if (name.equals("Mob") || name.equals("Monster")) continue;
            Entity entity = EntityList.createEntityByName(name, MCClientHelper.getWorld());
            if (entity instanceof EntityLivingBase) {
                entities.add((EntityLivingBase) entity);
                shuffledEntityCache.add((EntityLivingBase) entity);
                //Special case addition of entities
                //Wither Skeleton
                if (entity.getClass() == EntitySkeleton.class) {
                    entity = EntityList.createEntityByName(name, MCClientHelper.getWorld());
                    ((EntitySkeleton) entity).setSkeletonType(1);
                    entities.add((EntityLivingBase) entity);
                    shuffledEntityCache.add((EntityLivingBase) entity);
                }
                
                if (entity instanceof EntityRabbit) {
                    for (int i = 0; i < 6; i++) {
                        entity = EntityList.createEntityByName(name, MCClientHelper.getWorld());
                        ((EntityRabbit)entity).setRabbitType(i);
                        entities.add((EntityLivingBase) entity);
                        shuffledEntityCache.add((EntityLivingBase) entity);
                    }
                }
            }
        }
    }

    public static ArrayList<EntityLivingBase> getEntities() {
        return entities;
    }

    public static EntityLivingBase getRandomEntityForFilters(List<IProgressionFilter> filters) {
        ArrayList<IProgressionFilter> shuffledFilters = new ArrayList(filters);
        Collections.shuffle(shuffledEntityCache);
        Collections.shuffle(shuffledFilters);
        for (EntityLivingBase entity : shuffledEntityCache) {
            for (IProgressionFilter filter : shuffledFilters) {
                if (filter.matches(entity)) return entity;
            }
        }

        //In theory if set up correctly this should be no issue
        return null;
    }

    public static String getNameForEntity(EntityLivingBase living) {
        return EntityList.getEntityString(living);
    }
}
