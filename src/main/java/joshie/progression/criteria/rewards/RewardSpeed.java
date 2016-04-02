package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.special.DisplayMode;
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

    public String[] translate(String... text) {
        String[] translated = new String[text.length];
        for (int i = 0; i < translated.length; i++) {
            translated[i] = Progression.translate("reward.speed." + text[i]);
        }

        return translated;
    }

    private String getType() {
        if (land && water && air) return Progression.format("reward.speed.format3", translate("water.description", "land.description", "air.description"));
        else if (land && water) return Progression.format("reward.speed.format2", translate("water.description", "land.description"));
        else if (water && air) return Progression.format("reward.speed.format2", translate("water.description", "air.description"));
        else if (land && air) return Progression.format("reward.speed.format2", translate("land.description", "air.description"));
        else if (land) return Progression.translate("reward.speed.land");
        else if (water) return Progression.translate("reward.speed.water");
        else if (air) return Progression.translate("reward.speed.air");
        else return Progression.translate("reward.speed.never");
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Speed: " + speed + " " + getType());
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? super.getWidth(mode) : 55;
    }

    @Override
    public String getDescription() {
        return Progression.format("reward.speed.description", speed, getType());
    }
}
