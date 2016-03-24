package joshie.progression.criteria.rewards;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.items.ItemCriteria;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.UUID;

public class RewardBoolean extends RewardBase {
    public String variable = "default";
    public String display = "Free Research:\nDefault";
    public boolean value = true;

    public RewardBoolean() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.booleanValue), "boolean", 0xFF99B3FF);
    }

    @Override
    public void reward(UUID uuid) {
        ProgressionAPI.player.setBoolean(uuid, variable, value);
    }

    @Override
    public void addTooltip(List list) {
        String[] tooltip = display.split("/n");
        for (String string : tooltip) {
            list.add(EnumChatFormatting.WHITE + string);
        }
    }
}
