package joshie.progression.criteria.rewards;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomTooltip;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

@ProgressionRule(name="boolean", color=0xFF99B3FF, meta="booleanValue")
public class RewardBoolean extends RewardBaseSingular implements ICustomTooltip {
    public String variable = "default";
    public String display = "§9Free Research§r \n§7Default";
    public boolean value = true;

    @Override
    public String getDescription() {
        return display;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 75: 100;
    }

    @Override
    public void addTooltip(List list) {
        String[] tooltip = display.split("\n");
        for (String string : tooltip) {
            list.add(string);
        }
    }

    @Override
    public void reward(EntityPlayerMP player) {
        ProgressionAPI.player.setBoolean(player, variable, value);
    }
}
