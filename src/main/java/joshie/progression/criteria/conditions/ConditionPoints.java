package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.UUID;

public class ConditionPoints extends ConditionBase {
    public String variable = "gold";
    public double amount = 1D;
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    public ConditionPoints() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifHasPoints), "points", 0xFF00FFBF);
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

    @Override
    public String getDescription() {
        if (greaterThan && isEqualTo) return Progression.format(getUnlocalisedName() + ".greater.equal", amount, variable);
        else if (lesserThan && isEqualTo) return Progression.format(getUnlocalisedName() + ".lesser.equal", amount, variable);
        else if (greaterThan) return Progression.format(getUnlocalisedName() + ".greater", amount, variable);
        else if (lesserThan) return Progression.format(getUnlocalisedName() + ".lesser", amount, variable);
        else if (isEqualTo) return Progression.format(getUnlocalisedName() + ".equal", amount, variable);
        else return "INVALID SETUP";
    }
}
