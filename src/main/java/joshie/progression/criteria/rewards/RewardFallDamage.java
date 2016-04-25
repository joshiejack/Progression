package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@ProgressionRule(name="fallDamage", color=0xFF661A00, meta="fallResistance")
public class RewardFallDamage extends RewardBaseAbility implements ICustomTooltip {
    public int absorption = 1;

    @Override
    public String getDescription() {
        return Progression.format(getProvider().getUnlocalisedName() + ".description", absorption);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 85 : 75;
    }

    @Override
    public void addAbilityTooltip(List list) {
        list.add(EnumChatFormatting.GRAY + getProvider().getLocalisedName() + " " + absorption);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            int damage = (int) (event.distance - 3);
            int maxAbsorbed = PlayerTracker.getPlayerData(player).getAbilities().getFallDamagePrevention();
            if (damage < maxAbsorbed) {
                event.setCanceled(true);
            } else {
                event.distance = event.distance - maxAbsorbed;
            }
        }
    }

    @Override
    public void reward(EntityPlayerMP player) {
        PlayerTracker.getServerPlayer(player).addFallDamagePrevention(absorption);
    }






}
