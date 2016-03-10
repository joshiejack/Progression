package joshie.progression.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import joshie.progression.api.IEntityFilter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;

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
            }
        }
    }

    public static ArrayList<EntityLivingBase> getEntities() {
        return entities;
    }
    
    public static EntityLivingBase getRandomEntityForFilters(Set<IEntityFilter> filters) {
    	ArrayList<IEntityFilter> shuffledFilters = new ArrayList(filters);
    	Collections.shuffle(shuffledEntityCache);
    	Collections.shuffle(shuffledFilters);
    	for (EntityLivingBase entity: getEntities()) {
    		for (IEntityFilter filter: shuffledFilters) {
    			if (filter.matches(entity)) return entity;
    		}
    	}
    	
    	//In theory if set up correctly this should be no issue
    	return null;
    }
}
