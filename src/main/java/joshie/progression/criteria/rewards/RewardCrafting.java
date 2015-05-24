package joshie.progression.criteria.rewards;

import joshie.progression.crafting.ActionType;

public class RewardCrafting extends RewardBaseAction {
    public RewardCrafting() {
        super("crafting", 0xFFFF4000);
        this.type = ActionType.CRAFTING;
    }
}
