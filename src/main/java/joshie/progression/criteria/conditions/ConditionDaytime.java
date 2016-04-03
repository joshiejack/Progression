package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ConditionDaytime extends ConditionBase {
	private static final ItemStack day = ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.sun);
	private static final ItemStack night = ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.moon);

	public ConditionDaytime() {
		super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifDayOrNight), "daytime", 0xFFFFFF00);
	}
	
	@Override
	public boolean isSatisfied(IPlayerTeam team) {
		for (EntityPlayer player: team.getTeamEntities()) {
			if (player.worldObj.isDaytime()) return true;
		}

		return false;
	}
}
