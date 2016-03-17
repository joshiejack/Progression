package joshie.progression.gui.selector.filters;

import java.util.List;

import org.lwjgl.opengl.GL11;

import joshie.progression.api.IFilter.FilterType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.IFilterSelectorFilter;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;

public class EntityFilter extends FilterBase {
    public static final IFilterSelectorFilter INSTANCE = new EntityFilter();
    
    @Override
    public double getScale() {
        return 34D;
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
    public void draw(DrawFeatureHelper offset, Object object, int offsetX, int j, int yOffset, int k, int mouseX, int mouseY) {
        boolean test = false;
        if (test) {
            try {
                String name = EntityList.getEntityString((Entity) object);
                Entity entity = EntityList.createEntityByName(name, MCClientHelper.getPlayer().worldObj);
                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                GlStateManager.color(1F, 1F, 1F, 1F);
                GuiInventory.drawEntityOnScreen(ProgressionAPI.draw.getXPosition() + j + offsetX, yOffset, EntityHelper.getSizeForString(name), 25F, -5F, (EntityLivingBase) entity);
            } catch (Exception e) {}
        } else {
            EntityLivingBase entity = (EntityLivingBase) object;
            int max = 12;
            double numberToUse = (double) (entity.width >= entity.height ? entity.width : entity.height);
            int scale = (int) (numberToUse != 0 ? max / numberToUse : max);
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            GuiInventory.drawEntityOnScreen(ProgressionAPI.draw.getXPosition() + 10 + offsetX + j * 20, GuiItemFilterEditor.INSTANCE.screenTop + yOffset + 65 + k * 20, scale, 25F, -5F, entity);
        }
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
