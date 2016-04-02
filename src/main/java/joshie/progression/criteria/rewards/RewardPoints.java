package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RewardPoints extends RewardBaseSingular {
    public ItemStack icon = new ItemStack(Items.gold_ingot);
    public String variable = "gold";
    public int amount = 1;

    public RewardPoints() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.points), "points", 0xFF002DB2);
    }

    @Override
    public void reward(EntityPlayerMP player) {
        ProgressionAPI.player.addDouble(player, variable, amount);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 75: super.getWidth(mode);
    }

    @Override
    public String getDescription() {
        return Progression.format("reward.points.description", amount, variable);
    }
}
