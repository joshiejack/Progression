package joshie.progression.criteria.filters.entity;

import java.util.Set;

import com.google.common.collect.HashMultimap;

import joshie.progression.api.IInitAfterRead;
import joshie.progression.api.ISetterCallback;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;

public class FilterEntityName extends FilterBaseEntity implements ISetterCallback, IInitAfterRead {
    private static HashMultimap<Integer, String> cache = HashMultimap.create();
    private String checkName = "Pig";
    private boolean matchBoth;
    private boolean matchFront;
    private boolean matchBack;

    public String entityName = "Pig";

    public FilterEntityName() {
        super("entityName", 0xFFB25900);
    }

    @Override
    public void init() {
        setField("entityName", entityName);
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        int key = entity.getEntityId();
        Set<String> names = null;
        if (cache.containsKey(key)) names = cache.get(key);
        else {
            cache.get(key).add(EntityHelper.getNameForEntity(entity));
            names = cache.get(key);
        }

        for (String itemName : names) {
            if (matchBoth && itemName.toLowerCase().contains(checkName.toLowerCase())) return true;
            else if (matchFront && !matchBack && itemName.toLowerCase().endsWith(checkName.toLowerCase())) return true;
            else if (!matchFront && matchBack && itemName.toLowerCase().startsWith(checkName.toLowerCase())) return true;
            else if (itemName.toLowerCase().equals(checkName.toLowerCase())) return true;
        }

        return false;
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        String fieldValue = (String) object;
        entityName = fieldValue;
        if (entityName.startsWith("*")) matchFront = true;
        else matchFront = false;
        if (entityName.endsWith("*")) matchBack = true;
        else matchBack = false;
        matchBoth = matchFront && matchBack;
        checkName = entityName.replaceAll("\\*", "");
        return true;
    }
}
