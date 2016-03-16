package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.enchiridion.helpers.MCClientHelper;
import joshie.progression.api.ISpecialItemFilter;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class RewardPotion extends RewardBaseItemFilter implements ISpecialItemFilter {
    public boolean randomVanilla = false;
    public int customid = -1;
    public int duration = 200;
    public int amplifier = 0;
    public boolean particles = false;

    public RewardPotion() {
        super("potioneffect", 0xFF2C7373);
        BROKEN = new ItemStack(Items.potionitem, 1, 0);
    }
    
    @Override
    public String[] getSpecialFilters() {
        return new String[]  { "potioneffect" };
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            ItemStack stack = ItemHelper.getRandomItem(filters, null);
            for (PotionEffect effect : Items.potionitem.getEffects(stack)) {
                if (randomVanilla) player.addPotionEffect(new PotionEffect(effect));
                else {
                    int id = customid >= 0 ? customid : effect.getPotionID();
                    player.addPotionEffect(new PotionEffect(id, duration, amplifier, false, particles));
                }
            }
        }
    }

    @Override
    public void addTooltip(List list) {
        ItemStack stack = preview == null ? BROKEN : preview;
        Items.potionitem.addInformation(stack, MCClientHelper.getPlayer(), list, false);
    }
}
