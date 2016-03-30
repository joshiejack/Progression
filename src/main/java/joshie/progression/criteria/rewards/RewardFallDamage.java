package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.items.ItemCriteria;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class RewardFallDamage extends RewardBaseAbility implements IHasEventBus {
    public int absorption = 1;

    public RewardFallDamage() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.fallResistance), "fallDamage", 0xFF661A00);
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

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Fall Resistance: " + absorption);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 85 : 75;
    }

    @Override
    public String getDescription() {
        return Progression.format(getUnlocalisedName() + ".description", absorption);
    }
}
