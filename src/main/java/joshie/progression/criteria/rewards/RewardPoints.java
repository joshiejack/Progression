package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@ProgressionRule(name="points", color=0xFF002DB2, meta="points")
public class RewardPoints extends RewardBaseSingular {
    public ItemStack icon = new ItemStack(Items.gold_ingot);
    public String variable = "gold";
    public int amount = 1;

    @Override
    public String getDescription() {
        return Progression.format("reward.points.description", amount, variable);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 75: 100;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        ProgressionAPI.player.addDouble(player, variable, amount);
    }
}
