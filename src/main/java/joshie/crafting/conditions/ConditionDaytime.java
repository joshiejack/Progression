package joshie.crafting.conditions;

import java.util.UUID;

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
