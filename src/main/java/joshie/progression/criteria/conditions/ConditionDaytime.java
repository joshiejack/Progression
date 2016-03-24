package joshie.progression.criteria.conditions;

import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.UUID;

public class ConditionDaytime extends ConditionBase {	
    public boolean isDaytime = true;
    
	public ConditionDaytime() {
		super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifDayOrNight), "daytime", 0xFFFFFF00);
	}
	
	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		return world.isDaytime() == isDaytime;
	}
}
