package joshie.crafting.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import joshie.crafting.Criteria;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICraftingRegistry;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.lib.SafeStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.Multimap;

public class CraftingRegistry implements ICraftingRegistry {
    public static HashMap<CraftingType, Multimap<SafeStack, Criteria>> conditions;
    public static HashMap<CraftingType, Multimap<SafeStack, Criteria>> usage;

    public static void remove(CraftingType type, String modid, ItemStack stack, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, Criteria criteria) {
        SafeStack safe = SafeStack.newInstance(modid, stack, matchDamage, matchNBT);
        if (crafting) {
            Multimap<SafeStack, Criteria> conditions = CraftingRegistry.conditions.get(type);
            conditions.get(safe).remove(criteria);
        }

        if (usage) {
            Multimap<SafeStack, Criteria> usageMap = CraftingRegistry.usage.get(type);
            usageMap.get(safe).remove(criteria);
        }
    }

    @Override
    public Collection<Criteria> getCraftingCriteria(CraftingType type, ItemStack stack) {
        Collection<Criteria> conditions = new HashSet();
        SafeStack[] safe = SafeStack.allInstances(stack);
        for (SafeStack s : safe) {
            conditions.addAll(this.conditions.get(type).get(s));
        }

        return conditions;
    }

    @Override
    public Collection<Criteria> getCraftUsageCriteria(CraftingType type, ItemStack stack) {
        Collection<Criteria> conditions = new HashSet();
        SafeStack[] safe = SafeStack.allInstances(stack);
        for (SafeStack s : safe) {
            conditions.addAll(this.usage.get(type).get(s));
        }

        return conditions;
    }

    @Override
    public void addRequirement(CraftingType type, String modid, ItemStack stack, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, Criteria c) {
        if (crafting) {
            Multimap<SafeStack, Criteria> conditions = this.conditions.get(type);
            conditions.get(SafeStack.newInstance(modid, stack, matchDamage, matchNBT)).add(c);
        }

        if (usage) {
            Multimap<SafeStack, Criteria> usageMap = this.usage.get(type);
            usageMap.get(SafeStack.newInstance(modid, stack, matchDamage, matchNBT)).add(c);
        }
    }

    @Override
    public void addRequirement(CraftingType type, ItemStack stack, Criteria c) {
        addRequirement(type, "IGNORE", stack, true, false, true, true, c);
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
