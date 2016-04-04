package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomWidth;

import java.util.Random;

@ProgressionRule(name="chance", color=0xFF00FFBF, meta="ifRandom")
public class ConditionRandom extends ConditionBase implements ICustomDescription, ICustomWidth {
    private static final Random rand = new Random();
    public double chance = 50D;

    @Override
    public String getDescription() {
        if (getProvider().isInverted()) return Progression.format(getProvider().getUnlocalisedName() + ".description", 100D - chance);
        else return Progression.format(getProvider().getUnlocalisedName() + ".description", chance);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 65: 100;
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        return (rand.nextDouble() * 100) <= chance;
    }
}
