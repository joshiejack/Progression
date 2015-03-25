package joshie.crafting.crafting;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICraftingRegistry;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.lib.SafeStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CraftingRegistry implements ICraftingRegistry {
	public EnumMap<CraftingType, Multimap<SafeStack, ICriteria>> conditions = new EnumMap(CraftingType.class);
	public EnumMap<CraftingType, Multimap<SafeStack, ICriteria>> usage = new EnumMap(CraftingType.class);
	
	@Override
	public ICraftingRegistry init() {
		for (CraftingType type: CraftingType.values()) {
			Multimap<SafeStack, ICriteria> conditions = HashMultimap.create();
			Multimap<SafeStack, ICriteria> usage = HashMultimap.create();
			this.conditions.put(type, conditions);
			this.usage.put(type, usage);
		}
		
		return this;
	}

	@Override
	public Collection<ICriteria> getCraftingCriteria(CraftingType type, ItemStack stack) {
		Collection<ICriteria> conditions = new HashSet();
		SafeStack[] safe = SafeStack.allInstances(stack);
		for (SafeStack s: safe) {
			conditions.addAll(this.conditions.get(type).get(s));
		}
		
		return conditions;
	}

	@Override
	public Collection<ICriteria> getCraftUsageCriteria(CraftingType type, ItemStack stack) {
		Collection<ICriteria> conditions = new HashSet();
		SafeStack[] safe = SafeStack.allInstances(stack);
		for (SafeStack s: safe) {
			conditions.addAll(this.usage.get(type).get(s));
		}
		
		return conditions;
	}
	
	@Override
	public void addRequirement(CraftingType type, ItemStack stack, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, ICriteria c) {
		if (crafting) {
			Multimap<SafeStack, ICriteria> conditions = this.conditions.get(type);
			conditions.put(SafeStack.newInstance(stack, matchDamage, matchNBT), c);
		}
		
		if (usage) {
			Multimap<SafeStack, ICriteria> usageMap = this.usage.get(type);
			usageMap.put(SafeStack.newInstance(stack, matchDamage, matchNBT), c);
		}
	}

	@Override
	public void addRequirement(CraftingType type, ItemStack stack, ICriteria c) {
		addRequirement(type, stack, true, false, true, true, c);
	}

	@Override
	public ICrafter getCrafterFromPlayer(EntityPlayer player) {
		return getCrafterFromUUID(PlayerHelper.getUUIDForPlayer(player));
	}

	@Override
	public ICrafter getCrafterFromTile(TileEntity tile) {
		return getCrafterFromUUID(CraftingAPI.players.getTileOwner(tile).getUUID());
	}

	@Override
	public ICrafter getCrafterFromUUID(UUID uuid) {
		return PlayerHelper.getCrafterForUUID(uuid);
	}
}
