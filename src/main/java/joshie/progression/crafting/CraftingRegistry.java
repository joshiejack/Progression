package joshie.progression.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Multimap;

import joshie.progression.api.ICriteria;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.SafeStack;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class CraftingRegistry {
    public static HashMap<ActionType, Multimap<SafeStack, ICriteria>> conditions;
    public static HashMap<ActionType, Multimap<SafeStack, ICriteria>> usage;

    public static void remove(ActionType type, String modid, ItemStack stack, String orename, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, ICriteria criteria) {
        SafeStack safe = SafeStack.newInstance(modid, stack, orename, matchDamage, matchNBT);
        if (crafting) {
            Multimap<SafeStack, ICriteria> conditions = CraftingRegistry.conditions.get(type);
            conditions.get(safe).remove(criteria);
        }

        if (usage) {
            Multimap<SafeStack, ICriteria> usageMap = CraftingRegistry.usage.get(type);
            usageMap.get(safe).remove(criteria);
        }
    }

    public static Collection<ICriteria> getCraftingCriteria(ItemStack stack) {
        Collection<ICriteria> conditions = new HashSet();
        for (ActionType type : ActionType.values()) {
            conditions.addAll(getCraftingCriteria(type, stack));
        }

        return conditions;
    }

    public static Collection<ICriteria> getCraftingCriteria(ActionType type, ItemStack stack) {
        Collection<ICriteria> conditions = new HashSet();
        List<SafeStack> safe = SafeStack.allInstances(stack);
        for (SafeStack s : safe) {
            conditions.addAll(CraftingRegistry.conditions.get(type).get(s));
        }

        return conditions;
    }

    public static Collection<ICriteria> getCraftUsageCriteria(ActionType type, ItemStack stack) {
        Collection<ICriteria> conditions = new HashSet();
        List<SafeStack> safe = SafeStack.allInstances(stack);
        for (SafeStack s : safe) {
            conditions.addAll(CraftingRegistry.usage.get(type).get(s));
        }

        return conditions;
    }

    public static void addRequirement(ActionType type, String modid, ItemStack stack, String orename, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, ICriteria c) {
        if (crafting) {
            Multimap<SafeStack, ICriteria> conditions = CraftingRegistry.conditions.get(type);
            conditions.get(SafeStack.newInstance(modid, stack, orename, matchDamage, matchNBT)).add(c);
        }

        if (usage) {
            Multimap<SafeStack, ICriteria> usageMap = CraftingRegistry.usage.get(type);
            usageMap.get(SafeStack.newInstance(modid, stack, orename, matchDamage, matchNBT)).add(c);
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
