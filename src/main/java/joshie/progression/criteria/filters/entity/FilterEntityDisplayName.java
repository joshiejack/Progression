package joshie.progression.criteria.filters.entity;

import joshie.progression.api.special.IInit;
import net.minecraft.entity.EntityLivingBase;

public class FilterEntityDisplayName extends FilterBaseEntity implements IInit {
    private String checkName = "Girafi";
    private boolean matchBoth;
    private boolean matchFront;
    private boolean matchBack;

    public String entityName = "Girafi";

    public FilterEntityDisplayName() {
        super("displayName", 0xFFB25900);
    }

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
        String name = entity.getDisplayName().toString();
        if (matchBoth && name.toLowerCase().contains(checkName.toLowerCase())) return true;
        else if (matchFront && !matchBack && name.toLowerCase().endsWith(checkName.toLowerCase())) return true;
        else if (!matchFront && matchBack && name.toLowerCase().startsWith(checkName.toLowerCase())) return true;
        else if (name.toLowerCase().equals(checkName.toLowerCase())) return true;
        else return false;
    }
}
