package joshie.progression.criteria.conditions;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ConditionRandom extends ConditionBase {
    public double chance = 50D;

    public ConditionRandom() {
        super("chance", 0xFF00FFBF);
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        return (world.rand.nextDouble() * 100) <= chance;
    }
}
