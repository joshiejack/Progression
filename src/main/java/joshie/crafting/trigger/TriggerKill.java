package joshie.crafting.trigger;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.gui.SelectEntity;
import joshie.crafting.gui.SelectEntity.IEntitySelectable;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.EntityHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import org.lwjgl.opengl.GL11;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerKill extends TriggerBaseCounter implements IEntitySelectable {
    private IntegerFieldHelper amountEdit;
    public String entity = "Pig";

    public TriggerKill() {
        super("kill", 0xFF000000);
        amountEdit = new IntegerFieldHelper("amount", this);
    }

    @SubscribeEvent
    public void onEvent(LivingDeathEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer) {
            String entity = EntityList.getEntityString(event.entity);
            CraftingAPI.registry.fireTrigger((EntityPlayer) source, getUnlocalisedName(), entity);
        }
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        entity = JSONHelper.getString(data, "entity", entity);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setString(data, "entity", entity, "Pig");
    }

    @Override
    protected boolean canIncrease(Object... data) {
        return ((String)data[0]).equals(entity);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) {
                amountEdit.select();
                return Result.ALLOW;
            } else if (mouseY >= 27 && mouseY < 66) {
                SelectEntity.INSTANCE.select(this, Type.TRIGGER);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int color = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.triggerDraw.drawText("times: " + amountEdit, 4, 18, color);

        Entity entity = EntityList.createEntityByName(this.entity, ClientHelper.getPlayer().worldObj);
        int yPos = GuiCriteriaEditor.INSTANCE.y + 105;
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GuiInventory.func_147046_a(DrawHelper.triggerDraw.getXPosition() + 50 + GuiCriteriaEditor.INSTANCE.offsetX, yPos, EntityHelper.getSizeForString(this.entity), 25F, -5F, (EntityLivingBase) entity);
    }

    @Override
    public void setEntity(Entity entity) {
        try {
            this.entity = EntityList.getEntityString(entity);
        } catch (Exception e) {}
    }
}
