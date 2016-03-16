package joshie.progression.criteria.rewards;

import static joshie.progression.player.DataStats.SpeedType.AIR;
import static joshie.progression.player.DataStats.SpeedType.LAND;
import static joshie.progression.player.DataStats.SpeedType.WATER;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.IHasEventBus;
import joshie.progression.player.DataStats.SpeedType;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RewardSpeed extends RewardBase implements IHasEventBus {
    public float speed = 0.1F;
    public boolean land = true;
    public boolean air = false;
    public boolean water = true;

    public RewardSpeed() {
        super(new ItemStack(Items.potionitem, 1, 8194), "speed", 0xFFFFBF00);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.worldObj.isRemote) {
                SpeedType type = player.isInWater() ? WATER : !player.onGround ? AIR : LAND;
                float speed = PlayerTracker.getClientPlayer().getAbilities().getSpeed(type);
                if (speed != 1F) {
                    player.motionX *= speed;
                    player.motionZ *= speed;
                }
            }
        }
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }

    @Override
    public void reward(UUID uuid) {
        if (land) PlayerTracker.getServerPlayer(uuid).addSpeed(LAND, speed);
        if (air) PlayerTracker.getServerPlayer(uuid).addSpeed(AIR, speed);
        if (water) PlayerTracker.getServerPlayer(uuid).addSpeed(WATER, speed);
    }

    private String getType() {
        if (land && water && air) return "in water, on land or in air";
        else if (land && air && !water) return "on land or in air";
        else if (water && air && !land) return "in water or in air";
        else if (land && water && !air) return "in water or on land";
        else if (!land && !water && air) return "in air";
        else if (land && !water && !air) return "on land";
        else if (!land && water && !air) return "in water";
        else return "never";
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Speed: " + speed + " " + getType());
    }
}
