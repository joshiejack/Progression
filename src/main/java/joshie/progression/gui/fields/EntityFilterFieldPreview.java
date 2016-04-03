package joshie.progression.gui.fields;

import joshie.progression.api.criteria.IField;
import joshie.progression.api.special.IAdditionalTooltip;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;

import java.util.ArrayList;
import java.util.List;

public class EntityFilterFieldPreview extends ItemFilterField implements IField {
    private final int x;
    private final int y;
    private final float scale;
    protected final int mouseX1;
    protected final int mouseX2;
    protected final int mouseY1;
    protected final int mouseY2;
    private EntityLivingBase entity;
    private int ticker;

    public EntityFilterFieldPreview(String fieldName, Object object, int x, int y, float scale) {
        super(fieldName, object);
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.mouseX1 = x + 5;
        this.mouseX2 = (int) (x + 14 * scale);
        this.mouseY1 = y - (int)(14.2 * scale);
        this.mouseY2 = y;
    }

    @Override
    public String getField() {
        return "";
    }

    public EntityLivingBase getEntity() {
        return entity != null ? entity : MCClientHelper.getPlayer();
    }

    public EntityLivingBase getEntity(boolean hovered) {
        if (ticker >= 200 || ticker == 0) {
            entity = EntityHelper.getRandomEntityForFilters(getFilters());
            ticker = 1;
        }

        if (!hovered) ticker++;
        else if (!GuiScreen.isShiftKeyDown()) ticker += 2;

        return entity != null ? entity : MCClientHelper.getPlayer();
    }

    @Override
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        try {
            boolean hovered = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;
            EntityLivingBase entity = getEntity(hovered);
            if (hovered) {
                List<String> tooltip = new ArrayList();
                tooltip.add(entity.getName());
                if (object instanceof IAdditionalTooltip) {
                    ((IAdditionalTooltip)object).addHoverTooltip(tooltip);
                }

                FeatureTooltip.INSTANCE.addTooltip(tooltip);
            }

            GuiInventory.drawEntityOnScreen(GuiCore.INSTANCE.getOffsetX() + renderX + 24 + x, GuiCore.INSTANCE.screenTop + renderY + y + EntityHelper.getOffsetForEntity(entity), EntityHelper.getSizeForEntity(entity), 25F, -5F, entity);
            BossStatus.bossName = null; //Reset boss
            //helper.drawStack(renderX, renderY, getEntity(hovered), x, y, scale);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void click() {}

    @Override
    public boolean attemptClick(int mouseX, int mouseY) {
        boolean clicked = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;
        if (clicked) {
            super.click();
            return true;
        } else return false;
    }
}
