package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import net.minecraft.entity.player.EntityPlayerMP;

@ProgressionRule(name="time", color=0xFF26C9FF, icon="minecraft:clock")
public class RewardTime extends RewardBaseSingular {
    public boolean addTime = false;
    public int time = 0;

    @Override
    public String getDescription() {
        if (addTime) return Progression.format("reward.time.add", time);
        else return Progression.format("reward.time.set", time);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 55;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (addTime) {
            player.worldObj.setWorldTime(player.worldObj.getWorldTime() + (long) time);
        } else player.worldObj.setWorldTime(time);
    }
}
