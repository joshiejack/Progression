package joshie.progression.criteria.conditions;

import java.util.UUID;

import joshie.progression.api.ProgressionAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ConditionPoints extends ConditionBase {
    public String variable = "gold";
    public double amount = 1D;
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    public ConditionPoints() {
        super("points", 0xFF00FFBF);
    }

    private boolean isValidValue(double total) {
        if (greaterThan && total > amount) return true;
        if (isEqualTo && total == amount) return true;
        if (lesserThan && total < amount) return true;

        //FALSE BABY!!!
        return false;
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        return isValidValue(ProgressionAPI.player.getDouble(uuid, variable));
    }
}
