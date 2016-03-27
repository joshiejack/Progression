package joshie.progression.criteria.rewards;

import joshie.progression.items.ItemCriteria;
import joshie.progression.player.PlayerTracker;
import joshie.progression.player.data.AbilityStats.SpeedType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static joshie.progression.player.data.AbilityStats.SpeedType.*;

public class RewardSpeed extends RewardBaseAbility {
    public float speed = 0.1F;
    public boolean land = true;
    public boolean air = false;
    public boolean water = true;

    public RewardSpeed() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.speed), "speed", 0xFFFFBF00);
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
    public void reward(EntityPlayerMP player) {
        if (land) PlayerTracker.getServerPlayer(player).addSpeed(LAND, speed);
        if (air) PlayerTracker.getServerPlayer(player).addSpeed(AIR, speed);
        if (water) PlayerTracker.getServerPlayer(player).addSpeed(WATER, speed);
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
