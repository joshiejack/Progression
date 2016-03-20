package joshie.progression.criteria.conditions;

import java.util.UUID;

import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ConditionBoolean extends ConditionBase {
    public String variable = "default";
    public boolean isTrue = true;

    public ConditionBoolean() {
        super("boolean", 0xFF00FFBF);
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        boolean check = PlayerTracker.getServerPlayer(uuid).getPoints().getBoolean(variable);
        if (check == isTrue) {
            return true;
        }

        return false;
    }
}
