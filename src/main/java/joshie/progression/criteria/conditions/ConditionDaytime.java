package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.ProgressionRule;
import net.minecraft.entity.player.EntityPlayer;

@ProgressionRule(name="daytime", color=0xFFFFFF00, meta="ifDayOrNight")
public class ConditionDaytime extends ConditionBase {
    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        for (EntityPlayer player: team.getTeamEntities()) {
            if (player.worldObj.isDaytime()) return true;
        }

        return false;
    }
}
