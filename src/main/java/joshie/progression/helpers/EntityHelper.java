package joshie.progression.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.IFilterProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.concurrent.Callable;

public class EntityHelper {
    private static ArrayList<EntityLivingBase> shuffledEntityCache = new ArrayList();
    private static final HashMap<String, Integer> scalings = new HashMap();
    private static final HashMap<String, Integer> offsetY = new HashMap();
    private static final Cache<EntityLivingBase, Integer> scaleCache = CacheBuilder.newBuilder().maximumSize(1024).build();
    private static final Cache<EntityLivingBase, Integer> offsetCache = CacheBuilder.newBuilder().maximumSize(1024).build();
    private static void register(String entity, int scale, int offset) {
        scalings.put(entity, scale);
        offsetY.put(entity, offset);
    }

    static {
        offsetY.put("Thaumcraft.TaintacleTiny", -15);
        offsetY.put("Thaumcraft.Taintacle", -45);
        scalings.put("Giant", 3);
        scalings.put("WitherBoss", 10);
        scalings.put("Thaumcraft.EldritchGolem", 11);
        register("EnderDragon", 4, -4);
        register("Ghast", 5, -30);
        register("Thaumcraft.EldritchWarden", 4, 11);
    }

    private static int getSizeForString(String name) {
        if (scalings.containsKey(name)) {
            return scalings.get(name);
        } else return 15;
    }

    private static int getOffsetForString(String name) {
        if (offsetY.containsKey(name)) {
            return offsetY.get(name);
        } else return 0;
    }

    public static int getSizeForEntity(final EntityLivingBase entity) {
        try {
            return offsetCache.get(entity, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return getSizeForString(EntityList.getEntityString(entity));
                }
            });
        } catch (Exception e) { return  15; }
    }

    public static int getOffsetForEntity(final EntityLivingBase entity) {
        try {
            return scaleCache.get(entity, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return getOffsetForString(EntityList.getEntityString(entity));
                }
            });
        } catch (Exception e) { return  0; }
    }

    private static final ArrayList<EntityLivingBase> entities = new ArrayList();

    @SideOnly(Side.CLIENT)
    private static World getWorld() {
        return FMLClientHandler.instance().getWorldClient();
    }

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

    public static EntityLivingBase getRandomEntityForFilters(List<IFilterProvider> filters) {
        ArrayList<IFilterProvider> shuffledFilters = new ArrayList(filters);
        Collections.shuffle(shuffledEntityCache);
        Collections.shuffle(shuffledFilters);
        for (EntityLivingBase entity : shuffledEntityCache) {
            for (IFilterProvider filter : shuffledFilters) {
                if (filter.getProvided().matches(entity)) return entity;
            }
        }

        //In theory if set up correctly this should be no issue
        return null;
    }

    public static String getNameForEntity(EntityLivingBase living) {
        return EntityList.getEntityString(living);
    }

    public static EntityLivingBase getRandomEntity(IFilterProvider filter) {
        Collections.shuffle(shuffledEntityCache);
        for (EntityLivingBase entity : shuffledEntityCache) {
            if (filter.getProvided().matches(entity)) return entity;
        }

        //In theory if set up correctly this should be no issue
        return null;
    }
}
