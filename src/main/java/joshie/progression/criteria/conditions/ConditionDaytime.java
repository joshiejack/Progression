package joshie.progression.criteria.conditions;

import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.UUID;

public class ConditionDaytime extends ConditionBase {
	private static final ItemStack day = ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.sun);
	private static final ItemStack night = ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.moon);

	public ConditionDaytime() {
		super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifDayOrNight), "daytime", 0xFFFFFF00);
	}
	
	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		return world.isDaytime();
	}
}
