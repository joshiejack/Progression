package joshie.progression.criteria.conditions;

import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.UUID;

public class ConditionRandom extends ConditionBase {
    public double chance = 50D;

    public ConditionRandom() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifRandom), "chance", 0xFF00FFBF);
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        return (world.rand.nextDouble() * 100) <= chance;
    }
}
