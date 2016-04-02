package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.special.DisplayMode;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RewardTime extends RewardBaseSingular {
    public boolean addTime = false;
    public int time = 0;

    public RewardTime() {
        super(new ItemStack(Items.clock), "time", 0xFF26C9FF);
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (addTime) {
            player.worldObj.setWorldTime(player.worldObj.getWorldTime() + (long) time);
        } else player.worldObj.setWorldTime(time);
    }

    @Override
    public void addTooltip(List list) {
        list.add(getDescription());
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? super.getWidth(mode) : 55;
    }

    @Override
    public String getDescription() {
        if (addTime) return Progression.format("reward.time.add", time);
        else return Progression.format("reward.time.set", time);
    }
}
