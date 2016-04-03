package joshie.progression.gui.filters;

import joshie.progression.api.criteria.IFilterType;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class FilterTypeEntity extends FilterTypeBase {
    public static final IFilterType INSTANCE = new FilterTypeEntity();

    @Override
    public String getName() {
        return "entity";
    }

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
            EntityLivingBase entity = ((EntityLivingBase) object);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F); //Using state manager doesn't fix this
            boolean hovered = (mouseX >= 10 + (j * 32) && mouseX <= 9 + ((j + 1) * 32) && mouseY >= 40 && mouseY <= 120);
            if (hovered) {
                FeatureTooltip.INSTANCE.addTooltip("Localised: " + entity.getName());
                FeatureTooltip.INSTANCE.addTooltip("Name: " + EntityHelper.getNameForEntity(entity));
            }

            int entitySize = (EntityHelper.getNameForEntity(entity).equals("Thaumcraft.Taintacle") ? 15 : EntityHelper.getSizeForEntity(entity));//;
            int entityY = EntityHelper.getNameForEntity(entity).equals("Thaumcraft.Taintacle") ? -45: 0;
            GuiInventory.drawEntityOnScreen(offsetX + 24 + (j * 32), GuiCore.INSTANCE.screenTop + 105 + (k * 32) + yOffset + entityY, entitySize, 25F, -5F, entity);
            BossStatus.bossName = null; //Reset boss
        } catch (Exception e) {}
    }

    @Override
    public boolean isAcceptable(Object object) {
        return object instanceof EntityLivingBase;
    }
}
