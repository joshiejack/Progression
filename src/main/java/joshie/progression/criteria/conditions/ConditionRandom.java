package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.items.ItemCriteria;

import java.util.Random;

public class ConditionRandom extends ConditionBase {
    private static final Random rand = new Random();
    public double chance = 50D;

    public ConditionRandom() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifRandom), "chance", 0xFF00FFBF);
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        return (rand.nextDouble() * 100) <= chance;
    }

    @Override
    public String getDescription() {
        if (inverted) return Progression.format(getUnlocalisedName() + ".description", 100D - chance);
        else return Progression.format(getUnlocalisedName() + ".description", chance);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 65: super.getWidth(mode);
    }
}
