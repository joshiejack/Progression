package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.EventBusType;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RewardSpeed extends RewardBase {
    public float speed = 0.1F;
    public boolean land = true;
    public boolean water = false;

    public RewardSpeed() {
        super(new ItemStack(Items.potionitem, 1, 8194), "speed", 0xFFFFBF00);
        list.add(new TextField("speed", this));
        list.add(new BooleanField("land", this));
        list.add(new BooleanField("water", this));
    }
    
    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.worldObj.isRemote) {
                float speed = PlayerTracker.getClientPlayer().getAbilities().getSpeed();
                if (speed != 0) {
                    if (ClientHelper.isMovementPressed()) {
                        boolean shouldSpeed = player.isInWater()? water: land;
                        if (shouldSpeed) {
                            player.motionX *= speed;
                            player.motionZ *= speed;
                        }
                    }
                }
            }
        }
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        speed = JSONHelper.getFloat(data, "speed", speed);
        land = JSONHelper.getBoolean(data, "land", land);
        water = JSONHelper.getBoolean(data, "water", water);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setFloat(data, "speed", speed, 0.1F);
        JSONHelper.setBoolean(data, "land", land, true);
        JSONHelper.setBoolean(data, "water", water, false);
    }

    @Override
    public void reward(UUID uuid) {
        PlayerTracker.getServerPlayer(uuid).addSpeed(speed);
    }
    
    private String getType() {
        if (land && water) return "in water or on land";
        if (land && !water) return "on land";
        else if (!land && water) return "in water";
        else return "never";
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Speed: " + speed + " " + getType());
    }
}
