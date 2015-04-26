package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.gui.fields.TextField;
import joshie.crafting.helpers.JSONHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

public class ConditionRandom extends ConditionBase {
    public double chance = 50D;

    public ConditionRandom() {
        super("chance", 0xFF00FFBF);
        list.add(new TextField("chance", this));
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        return (world.rand.nextDouble() * 100) <= chance;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        chance = JSONHelper.getDouble(data, "chance", chance);
    }

    @Override
    public void writeToJSON(JsonObject elements) {
        JSONHelper.setDouble(elements, "chance", chance, 50D);
    }
}
