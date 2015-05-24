package joshie.progression.criteria.rewards;

import joshie.progression.crafting.ActionType;

public class RewardFurnace extends RewardBaseAction {
    public RewardFurnace() {
        super("furnace", 0xFFAAAAAA);
        this.type = ActionType.FURNACE;
    }
}
