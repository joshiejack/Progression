package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@ProgressionRule(name="stepAssist", color=0xFF661A00, meta="stepAssist")
public class RewardStepAssist extends RewardBaseAbility implements ICustomTooltip {
    public float steps = 0.5F;

    @Override
    public String getDescription() {
        return Progression.format("reward.stepAssist.description", steps);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 65;
    }

    @Override
    public void addAbilityTooltip(List list) {
        list.add(EnumChatFormatting.GRAY + getProvider().getLocalisedName() + ": " + steps);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            float step = PlayerTracker.getPlayerData(player).getAbilities().getStepAssist();
            float steps = 0.5F * (step + 1);
            if (steps > player.stepHeight) {
                player.stepHeight = steps;
                player.getEntityData().setBoolean("HasRewardStepAssist", true);
            }

            if (step == 0.5F && player.getEntityData().hasKey("HasRewardStepAssist")) {
                player.getEntityData().removeTag("HasRewardStepAssist");
                player.stepHeight = 0.5F;
            }
        }
    }

    @Override
    public void reward(EntityPlayerMP player) {
        PlayerTracker.getServerPlayer(player).addStepAssist(steps);
    }
}
