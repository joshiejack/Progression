package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.gui.TextList.TextField;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RewardSpeed extends RewardBase {
    public float speed = 0.1F;

    public RewardSpeed() {
        super(new ItemStack(Items.potionitem, 1, 8194), "speed", 0xFFFFBF00);
        list.add(new TextField("speed", this));
    }
    
    public String[] getFields() {
        return new String[] { "speed" };
    }
    
    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.worldObj.isRemote) {
                float speed = PlayerTracker.getClientPlayer().getAbilities().getSpeed();
                if (speed > 0 && player.onGround && !player.isInWater() && player.isSprinting() && ClientHelper.isForwardPressed()) {
                    player.moveFlying(0F, 1.0F, speed);
                }
            }
        }
    }

    @Override
    public Bus getEventBus() {
        return Bus.FORGE;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        speed = JSONHelper.getFloat(data, "speed", speed);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setFloat(data, "speed", speed, 0.1F);
    }

    @Override
    public void reward(UUID uuid) {
        PlayerTracker.getServerPlayer(uuid).addSpeed(speed);
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Speed: " + speed);
    }
}
