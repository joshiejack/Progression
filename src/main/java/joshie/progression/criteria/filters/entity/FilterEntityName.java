package joshie.progression.criteria.filters.entity;

import com.google.common.collect.HashMultimap;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IInit;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;

import java.util.Set;

@ProgressionRule(name="entityName", color=0xFFB25900)
public class FilterEntityName extends FilterBaseEntity implements IInit {
    private static HashMultimap<Integer, String> cache = HashMultimap.create();
    private String checkName = "Pig";
    private boolean matchBoth;
    private boolean matchFront;
    private boolean matchBack;

    public String entityName = "Pig";

    @Override
    public void init() {
        if (entityName.startsWith("*")) matchFront = true;
        else matchFront = false;
        if (entityName.endsWith("*")) matchBack = true;
        else matchBack = false;
        matchBoth = matchFront && matchBack;
        checkName = entityName.replaceAll("\\*", "");
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
}
