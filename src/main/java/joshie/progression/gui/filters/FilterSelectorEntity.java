package joshie.progression.gui.filters;

import java.util.List;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.criteria.IProgressionFilter.FilterType;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;

public class FilterSelectorEntity extends FilterSelectorBase {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorEntity();
    
    @Override
    public int getChange() {
        return 1;
    }
    
    @Override
    public double getScale() {
        return 1D;
    }

    @Override
    public List<EntityLivingBase> getAllItems() {
        return EntityHelper.getEntities();
    }

    @Override
    public boolean searchMatches(Object object, String search) {
        EntityLivingBase entity = (EntityLivingBase) object;
        try {
            if (EntityList.getEntityString(entity) != null) {
                if (EntityList.getEntityString(entity).toLowerCase().contains(search)) {
                    return true;
                }
            }
        } catch (Exception e) {}

        return false;
    }
    
    @Override
    public void draw(DrawHelper offset, Object object, int offsetX, int j, int yOffset, int k, int mouseX, int mouseY) {
        try {
            GuiInventory.drawEntityOnScreen(offsetX + 24 + (j * 32), GuiCore.INSTANCE.screenTop + 105 + (k * 32) + yOffset, EntityHelper.getSizeForEntity((Entity) object), 25F, -5F, (EntityLivingBase) object);
            BossStatus.bossName = null; //Reset boss
        } catch (Exception e) {}
    }

    @Override
    public FilterType getType() {
        return FilterType.ENTITY;
    }

    @Override
    public boolean isAcceptable(Object object) {
        return object instanceof EntityLivingBase;
    }
}
