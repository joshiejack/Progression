package joshie.progression.criteria.conditions;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.IField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ConditionDaytime extends ConditionBase {	
	public ConditionDaytime() {
		super("daytime", 0xFFFFFF00);
	}
	
	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		return world.isDaytime();
	}
}
