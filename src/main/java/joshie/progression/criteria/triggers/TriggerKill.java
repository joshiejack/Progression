package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import joshie.progression.api.IEntityFilter;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerKill extends TriggerBaseCounterVaries {
    public List<IEntityFilter> filters = new ArrayList();
    private transient EntityLivingBase entity;

    public TriggerKill() {
        super("kill", 0xFF000000);
    }

    @SubscribeEvent
    public void onEvent(LivingDeathEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer && source instanceof EntityLivingBase) {
            ProgressionAPI.registry.fireTrigger((EntityPlayer) source, getUnlocalisedName(), source);
        }
    }

    @Override
    protected boolean canIncrease(Object... data) {
        EntityLivingBase entity = (EntityLivingBase) data[0];
        for (IEntityFilter filter : filters) {
            if (filter.matches(entity)) return true;
        }

        return false;
    }

    @Override
    public void updateDraw() {
        entity = EntityHelper.getRandomEntityForFilters(filters);
    }

    @Override
    public String getDescription() {
        String name = "INVALID";
        try {
            name = entity.getName();
        } catch (Exception e) {}

        return amount + " x " + name;
    }

    @Override
    public void drawDisplay(int mouseX, int mouseY) {
        try {
            int max = 36;
            double numberToUse = (double) (entity.width >= entity.height ? entity.width : entity.height);
            int scale = (int) (numberToUse != 0 ? max / numberToUse : max);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GuiInventory.drawEntityOnScreen(ProgressionAPI.draw.getXPosition() + 37 + GuiCriteriaEditor.INSTANCE.offsetX, GuiCriteriaEditor.INSTANCE.y + 117, scale, 25F, -5F, entity);
        } catch (Exception e) {}
    }
}
