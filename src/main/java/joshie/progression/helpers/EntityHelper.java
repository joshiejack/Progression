package joshie.progression.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.IFilterProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.concurrent.Callable;

public class EntityHelper {
    private static final ArrayList<EntityLivingBase> entities = new ArrayList();
    private static final HashMap<String, Integer> scalings = new HashMap();
    private static final HashMap<String, Integer> offsetY = new HashMap();
    private static ArrayList<EntityLivingBase> shuffledEntityCache = new ArrayList();

    //Clientside Caches
    @SideOnly(Side.CLIENT)
    private static final Cache<EntityLivingBase, Integer> scaleCache = CacheBuilder.newBuilder().maximumSize(1024).build();
    @SideOnly(Side.CLIENT)
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

    public static int getSizeForEntity(final EntityLivingBase entity) {
        try {
            return offsetCache.get(entity, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    String name = (EntityList.getEntityString(entity));
                    return scalings.containsKey(name) ? scalings.get(name) : 15;
                }
            });
        } catch (Exception e) { return  15; }
    }

    public static int getOffsetForEntity(final EntityLivingBase entity) {
        try {
            return scaleCache.get(entity, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    String name = (EntityList.getEntityString(entity));
                    return offsetY.containsKey(name) ? offsetY.get(name) : 0;
                }
            });
        } catch (Exception e) { return  0; }
    }



    @SideOnly(Side.CLIENT)
    private static World getWorld() {
        return FMLClientHandler.instance().getWorldClient();
    }

    public static ArrayList<EntityLivingBase> getEntities() {
        return entities;
    }

    public static EntityLivingBase getRandomEntityFromFilters(List<IFilterProvider> locality, EntityPlayer player) {
        if (player == null) return null;
        int size = locality.size();
        if (size == 0) return null;
        if (size == 1) return (EntityLivingBase) locality.get(0).getProvided().getRandom(player);
        else {
            return (EntityLivingBase) locality.get(player.worldObj.rand.nextInt(size)).getProvided().getRandom(player);
        }
    }

    public static String getNameForEntity(EntityLivingBase living) {
        return EntityList.getEntityString(living);
    }

    public static boolean isInit = false;

    public static EntityLivingBase getRandomEntity(World world, IFilterProvider filter) {
        if (!isInit) {
            isInit = true;

            for (String name : (Set<String>) EntityList.stringToClassMapping.keySet()) {
                if (name.equals("Mob") || name.equals("Monster")) continue;
                Entity entity = EntityList.createEntityByName(name, world);
                if (entity instanceof EntityLivingBase) {
                    entities.add((EntityLivingBase) entity);
                    shuffledEntityCache.add((EntityLivingBase) entity);
                    //Special case addition of entities
                    //Wither Skeleton, Add to list
                    if (entity.getClass() == EntitySkeleton.class) {
                        entity = EntityList.createEntityByName(name, world);
                        ((EntitySkeleton) entity).setSkeletonType(1);
                        entities.add((EntityLivingBase) entity);
                        shuffledEntityCache.add((EntityLivingBase) entity);
                    }


                    if (entity instanceof EntityRabbit) {
                        for (int i = 0; i < 6; i++) {
                            entity = EntityList.createEntityByName(name, world);
                            ((EntityRabbit)entity).setRabbitType(i);
                            entities.add((EntityLivingBase) entity);
                            shuffledEntityCache.add((EntityLivingBase) entity);
                        }
                    }
                }
            }
        }

        Collections.shuffle(shuffledEntityCache);
        for (EntityLivingBase entity : shuffledEntityCache) {
            if (filter.getProvided().matches(entity)) return entity;
        }

        //In theory if set up correctly this should be no issue
        return null;
    }
}
