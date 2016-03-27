package joshie.progression.criteria.rewards;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RewardTime extends RewardBaseSingular {
    public boolean add = false;
    public int time = 0;

    public RewardTime() {
        super(new ItemStack(Items.clock), "time", 0xFF26C9FF);
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (add) {
            player.worldObj.setWorldTime(player.worldObj.getWorldTime() + (long) time);
        } else player.worldObj.setWorldTime(time);
    }

    @Override
    public void addTooltip(List list) {
        if (add) list.add("Add " + time + " ticks to time");
        else list.add("Set time to " + time);
    }
}
