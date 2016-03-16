package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RewardTime extends RewardBase {
    public boolean add = false;
    public int time = 0;

    public RewardTime() {
        super(new ItemStack(Items.clock), "time", 0xFF26C9FF);
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            if (add) {
                player.worldObj.setWorldTime(player.worldObj.getWorldTime() + (long) time);
            } else player.worldObj.setWorldTime(time);
        }
    }

    @Override
    public void addTooltip(List list) {
        if (add) list.add("Add " + time + " ticks to time");
        else list.add("Set time to " + time);
    }
}
