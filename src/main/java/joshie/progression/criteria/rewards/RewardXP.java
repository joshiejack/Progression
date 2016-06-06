package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import net.minecraft.entity.player.EntityPlayerMP;

@ProgressionRule(name="giveXP", color=0xFF589653, icon="minecraft:experience_bottle")
public class RewardXP extends RewardBaseSingular {
    public int amount = 1;

    @Override
    public String getDescription() {
        return Progression.format("reward.giveXP.description", amount, amount);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 75: 100;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (amount >= 0) player.addExperience(amount);
        else if (amount < 0) {
            player.removeExperienceLevel(-amount);
        }
    }
}