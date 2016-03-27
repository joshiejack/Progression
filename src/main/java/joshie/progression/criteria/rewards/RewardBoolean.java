package joshie.progression.criteria.rewards;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class RewardBoolean extends RewardBaseSingular {
    public String variable = "default";
    public String display = "Free Research:\nDefault";
    public boolean value = true;

    public RewardBoolean() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.booleanValue), "boolean", 0xFF99B3FF);
    }

    @Override
    public void reward(EntityPlayerMP player) {
        ProgressionAPI.player.setBoolean(player, variable, value);
    }

    @Override
    public void addTooltip(List list) {
        String[] tooltip = display.split("/n");
        for (String string : tooltip) {
            list.add(EnumChatFormatting.WHITE + string);
        }
    }
}
