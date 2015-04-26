package joshie.progression.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import joshie.progression.api.ICriteria;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.SafeStack;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.Multimap;

public class CraftingRegistry {
    public static HashMap<CraftingType, Multimap<SafeStack, ICriteria>> conditions;
    public static HashMap<CraftingType, Multimap<SafeStack, ICriteria>> usage;

    public static void remove(CraftingType type, String modid, ItemStack stack, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, ICriteria criteria) {
        SafeStack safe = SafeStack.newInstance(modid, stack, matchDamage, matchNBT);
        if (crafting) {
            Multimap<SafeStack, ICriteria> conditions = CraftingRegistry.conditions.get(type);
            conditions.get(safe).remove(criteria);
        }

        if (usage) {
            Multimap<SafeStack, ICriteria> usageMap = CraftingRegistry.usage.get(type);
            usageMap.get(safe).remove(criteria);
        }
    }

    public static Collection<ICriteria> getCraftingCriteria(CraftingType type, ItemStack stack) {
        Collection<ICriteria> conditions = new HashSet();
        SafeStack[] safe = SafeStack.allInstances(stack);
        for (SafeStack s : safe) {
            conditions.addAll(CraftingRegistry.conditions.get(type).get(s));
        }

        return conditions;
    }

    public static Collection<ICriteria> getCraftUsageCriteria(CraftingType type, ItemStack stack) {
        Collection<ICriteria> conditions = new HashSet();
        SafeStack[] safe = SafeStack.allInstances(stack);
        for (SafeStack s : safe) {
            conditions.addAll(CraftingRegistry.usage.get(type).get(s));
        }

        return conditions;
    }

    public static void addRequirement(CraftingType type, String modid, ItemStack stack, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, ICriteria c) {
        if (crafting) {
            Multimap<SafeStack, ICriteria> conditions = CraftingRegistry.conditions.get(type);
            conditions.get(SafeStack.newInstance(modid, stack, matchDamage, matchNBT)).add(c);
        }

        if (usage) {
            Multimap<SafeStack, ICriteria> usageMap = CraftingRegistry.usage.get(type);
            usageMap.get(SafeStack.newInstance(modid, stack, matchDamage, matchNBT)).add(c);
        }
    }

    public static Crafter getCrafterFromPlayer(EntityPlayer player) {
        return getCrafterFromUUID(PlayerHelper.getUUIDForPlayer(player));
    }

    public static Crafter getCrafterFromTile(TileEntity tile) {
        return getCrafterFromUUID(PlayerTracker.getTileOwner(tile));
    }

    public static Crafter getCrafterFromUUID(UUID uuid) {
        return PlayerHelper.getCrafterForUUID(uuid);
    }
}
