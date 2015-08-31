package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.editors.SelectItem.Type;
import joshie.progression.gui.fields.EntityField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerKill extends TriggerBaseCounter {
    public String entity = "Pig";

    public TriggerKill() {
        super("kill", 0xFF000000);
        list.add(new EntityField("entity", this, 50, 105, 1, 84, 27, 65, Type.TRIGGER));
    }

    @SubscribeEvent
    public void onEvent(LivingDeathEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer) {
            String entity = EntityList.getEntityString(event.entity);
            ProgressionAPI.registry.fireTrigger((EntityPlayer) source, getUnlocalisedName(), entity);
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
}
