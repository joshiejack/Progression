package joshie.progression.criteria.rewards;

import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class RewardStepAssist extends RewardBaseAbility {
    public float steps = 0.5F;

    public RewardStepAssist() {
        super(new ItemStack(Items.leather_boots), "stepAssist", 0xFF661A00);
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

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Step Assist: " + steps);
    }
}
