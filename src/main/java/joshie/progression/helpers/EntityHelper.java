package joshie.progression.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class EntityHelper {
    private static final Cache<EntityLivingBase, Pair<Integer, Integer>> cacheScaleOffset = CacheBuilder.newBuilder().maximumSize(16).build();
    private static final Cache<EntityLivingBase, ItemStack> eggCache = CacheBuilder.newBuilder().maximumSize(256).build();
    private static final HashMap<String, Pair<Integer, Integer>> scaleOffset = new HashMap<String, Pair<Integer, Integer>>();
    private static List<EntityLivingBase> clientList;
    private static List<EntityLivingBase> serverList;

    private static void register(String entity, int scale, int offset) {
        scaleOffset.put(entity, Pair.of(scale, offset));
    }

    static {
        register("Thaumcraft.TaintacleTiny", 15, -15);
        register("Thaumcraft.Taintacle", 15, -45);
        register("Giant", 3, 0);
        register("WitherBoss", 10, 0);
        register("Thaumcraft.EldritchGolem", 11, 0);
        register("EnderDragon", 4, -4);
        register("Ghast", 5, -30);
        register("Thaumcraft.EldritchWarden", 4, 11);
    }

    public static int getSizeForEntity(final EntityLivingBase entity) {
        try {
            return cacheScaleOffset.get(entity, new Callable<Pair<Integer, Integer>>() {
                @Override
                public Pair<Integer, Integer> call() throws Exception {
                    String name = (EntityList.getEntityString(entity));
                    return scaleOffset.containsKey(name) ? scaleOffset.get(name) : Pair.of(15, 0);
                }
            }).getKey();
        } catch (Exception e) { return  15; }
    }

    public static int getOffsetForEntity(final EntityLivingBase entity) {
        try {
            return cacheScaleOffset.get(entity, new Callable<Pair<Integer, Integer>>() {
                @Override
                public Pair<Integer, Integer> call() throws Exception {
                    String name = (EntityList.getEntityString(entity));
                    return scaleOffset.containsKey(name) ? scaleOffset.get(name) : Pair.of(15, 0);
                }
            }).getValue();
        } catch (Exception e) { return  0; }
    }

    @SideOnly(Side.CLIENT)
    public static List<EntityLivingBase> getEntities() {
        if (clientList == null) clientList = init(MCClientHelper.getMinecraft().theWorld);

        return clientList;
    }

    public static IFilter getFilter(List<IFilterProvider> filters, EntityPlayer player) {
        if (player == null) return null;
        int size = filters.size();
        if (size == 0) return null;
        if (size == 1) return filters.get(0).getProvided();
        else {
            return filters.get(player.worldObj.rand.nextInt(size)).getProvided();
        }
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

    private static List<EntityLivingBase> init(World world) {
        List<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
        for (String name : EntityList.NAME_TO_CLASS.keySet()) {
            if (name.equals("Mob") || name.equals("Monster")) continue;
            Entity entity = EntityList.createEntityByName(name, world);
            if (entity instanceof EntityLivingBase) {
                list.add((EntityLivingBase) entity);
                //Special case addition of entities
                //Wither Skeleton, Add to list
                if (entity.getClass() == EntitySkeleton.class) {
                    entity = EntityList.createEntityByName(name, world);
                    ((EntitySkeleton) entity).setSkeletonType(1);
                    list.add((EntityLivingBase) entity);
                }

                //Rabbit Variants
                if (entity instanceof EntityRabbit) {
                    for (int i = 0; i < 6; i++) {
                        entity = EntityList.createEntityByName(name, world);
                        ((EntityRabbit)entity).setRabbitType(i);
                        list.add((EntityLivingBase) entity);
                    }
                }
            }
        }

        return list;
    }

    public static EntityLivingBase getRandomEntity(World world, IFilterProvider filter) {
        if (serverList == null) serverList = init(world);
        Collections.shuffle(serverList);
        for (EntityLivingBase entity : serverList) {
            if (filter == null) return entity; //If we passed a null filter, return anything
            if (filter.getProvided().matches(entity)) return entity;
        }

        //In theory if set up correctly this should be no issue
        return null;
    }

    public static ItemStack getItemForEntity(final EntityLivingBase entity) {
        try {
            return eggCache.get(entity, new Callable<ItemStack>() {
                @Override
                public ItemStack call() throws Exception {
                    int id = EntityList.getEntityID(entity);
                    if (id != 0) {
                        return new ItemStack(Items.SPAWN_EGG, 1, id);
                    }

                    String name = EntityList.getEntityString(entity);
                    ItemStack stack = new ItemStack(Items.SPAWN_EGG);
                    net.minecraft.nbt.NBTTagCompound nbt = new net.minecraft.nbt.NBTTagCompound();
                    nbt.setString("entity_name", name);
                    stack.setTagCompound(nbt);
                    return stack;
                }
            });
        } catch (Exception e) {}

        return new ItemStack(Items.SPAWN_EGG);
    }
}
