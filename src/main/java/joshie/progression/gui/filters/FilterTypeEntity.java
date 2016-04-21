package joshie.progression.gui.filters;

import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.gui.IDrawHelper;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.concurrent.Callable;

import static joshie.progression.gui.core.GuiList.CORE;
import static joshie.progression.gui.core.GuiList.LAST;
import static joshie.progression.gui.core.GuiList.TOOLTIP;

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
    public void draw(final IDrawHelper offset, final Object object, final int offsetX, final int j, final int yOffset, final int k, final int mouseX, final int mouseY) {
        try {
            LAST.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    EntityLivingBase entity = ((EntityLivingBase) object);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F); //Using state manager doesn't fix this
                    boolean hovered = (mouseX >= 10 + (j * 32) && mouseX <= 9 + ((j + 1) * 32) && mouseY >= 40 && mouseY <= 120);
                    if (hovered) {
                        TOOLTIP.add("Localised: " + entity.getName());
                        TOOLTIP.add("Name: " + EntityHelper.getNameForEntity(entity));
                    }

                    int entitySize = EntityHelper.getSizeForEntity(entity);
                    int entityY = EntityHelper.getOffsetForEntity(entity);
                    GuiInventory.drawEntityOnScreen(offsetX + 24 + (j * 32), CORE.screenTop + 105 + (k * 32) + yOffset + entityY, entitySize, 25F, -5F, entity);
                    BossStatus.bossName = null; //Reset boss

                    return null;
                }
            });

        } catch (Exception e) {}
    }

    @Override
    public boolean isAcceptable(Object object) {
        return object instanceof EntityLivingBase;
    }
}
