package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class RewardBoolean extends RewardBase {
    public String variable = "default";
    public String display = "Free Research:\nDefault";
    public boolean value = true;

    public RewardBoolean() {
        super(new ItemStack(Items.potionitem), "boolean", 0xFF99B3FF);
    }

    @Override
    public void reward(UUID uuid) {
        PlayerTracker.getServerPlayer(uuid).setBoolean(variable, value);
    }

    @Override
    public void addTooltip(List list) {
        String[] tooltip = display.split("/n");
        for (String string : tooltip) {
            list.add(EnumChatFormatting.WHITE + string);
        }
    }
}
