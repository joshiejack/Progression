package joshie.progression.gui.fields;

import java.lang.reflect.Field;

import org.lwjgl.opengl.GL11;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.gui.editors.SelectEntity;
import joshie.progression.gui.editors.SelectEntity.IEntitySelectable;
import joshie.progression.gui.editors.SelectItem.Type;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;

public class EntityField extends AbstractField implements IEntitySelectable {
    private Field field;
    private Object object;
    private final int x;
    private final int y;
    private final int mouseX1;
    private final int mouseX2;
    private final int mouseY1;
    private final int mouseY2;
    private final Type type;

    public EntityField(String fieldName, Object object, int x, int y, int mouseX1, int mouseX2, int mouseY1, int mouseY2, Type type) {
        super(fieldName);
        this.x = x;
        this.y = y;
        this.mouseX1 = mouseX1;
        this.mouseX2 = mouseX2;
        this.mouseY1 = mouseY1;
        this.mouseY2 = mouseY2;
        this.type = type;

        try {
            this.field = object.getClass().getField(fieldName);
            this.object = object;
        } catch (Exception e) {}
    }

    @Override
    public void click() {}

    @Override
    public boolean attemptClick(int mouseX, int mouseY) {
        boolean clicked = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;
        if (clicked) {
            SelectEntity.INSTANCE.select(this, type);
            return true;
        } else return false;
    }

    public String getEntity() {
        try {
            return (String) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        try {
            Entity entity = EntityList.createEntityByName(getEntity(), MCClientHelper.getPlayer().worldObj);
            yPos = GuiCriteriaEditor.INSTANCE.y + y;
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GuiInventory.drawEntityOnScreen(ProgressionAPI.draw.getXPosition() + x + GuiCriteriaEditor.INSTANCE.offsetX, yPos, EntityHelper.getSizeForString(getEntity()), 25F, -5F, (EntityLivingBase) entity);
        } catch (Exception e) {}
    }

    @Override
    public void setEntity(Entity entity) {
        try {
            field.set(object, EntityList.getEntityString(entity));
        } catch (Exception e) {}
    }
    
    @Override
    public void setObject(Object object) {
        this.object = object;
    }
}