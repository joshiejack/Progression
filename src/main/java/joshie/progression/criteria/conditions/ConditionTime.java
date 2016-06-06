package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import net.minecraft.entity.player.EntityPlayer;

@ProgressionRule(name="timeofday", color=0xFFF99100, icon="minecraft:clock")
public class ConditionTime extends ConditionBase implements ICustomDescription {
    public int timeMin = 0;
    public int timeMax = 0;

    @Override
    public String getDescription() {
        if (getProvider().isInverted()) return Progression.format(getProvider().getUnlocalisedName() + ".description.inverted", timeMin, timeMax);
        else return Progression.format(getProvider().getUnlocalisedName() + ".description", timeMin, timeMax);
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        for (EntityPlayer player: team.getTeamEntities()) {
            long time = player.worldObj.getWorldTime() % 24000L;
            if (time >= timeMin && time <= timeMax) return true;
        }

        return false;
    }
}
