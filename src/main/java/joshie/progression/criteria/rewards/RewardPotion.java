package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.enchiridion.helpers.MCClientHelper;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class RewardPotion extends RewardBaseItemFilter {
    public boolean randomVanilla = false;
    public int customid = -1;
    public int duration = 200;
    public int amplifier = 0;
    public boolean particles = false;

    public RewardPotion() {
        super("potioneffect", 0xFF2C7373);
        list.add(new TextField("customid", this));
        list.add(new TextField("duration", this));
        list.add(new TextField("amplifier", this));
        list.add(new BooleanField("particles", this));
        list.add(new BooleanField("randomVanilla", this));
        list.add(new ItemFilterField("filters", this, "potioneffect"));
        BROKEN = new ItemStack(Items.potionitem, 1, 0);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        randomVanilla = JSONHelper.getBoolean(data, "randomVanilla", false);
        duration = JSONHelper.getInteger(data, "customid", -1);
        duration = JSONHelper.getInteger(data, "duration", 200);
        amplifier = JSONHelper.getInteger(data, "amplifier", 0);
        particles = JSONHelper.getBoolean(data, "particles", false);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setBoolean(data, "randomVanilla", randomVanilla, false);
        JSONHelper.setInteger(data, "customid", duration, -1);
        JSONHelper.setInteger(data, "duration", duration, 200);
        JSONHelper.setInteger(data, "amplifier", amplifier, 0);
        JSONHelper.setBoolean(data, "particles", particles, false);
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
