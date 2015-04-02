package joshie.crafting.conditions;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.gui.TextFieldHelper.DoubleFieldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class ConditionRandom extends ConditionBase {
    private DoubleFieldHelper chanceEdit;
    public double chance = 50D;

    public ConditionRandom() {
        super("Random Chance", 0xFF00FFBF, "chance");
        chanceEdit = new DoubleFieldHelper("chance", this);
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        return (world.rand.nextDouble() * 100) <= chance;
    }

    @Override
    public ICondition deserialize(JsonObject data) {
        ConditionRandom condition = new ConditionRandom();
        if (data.get("chance") != null) {
            condition.chance = data.get("chance").getAsDouble();
        }

        return condition;
    }

    @Override
    public void serialize(JsonObject elements) {
        if (chance != 50D) {
            elements.addProperty("chance", chance);
        }
    }

    @Override
    public ICondition newInstance() {
        return new ConditionRandom();
    }

    @Override
    public Result clicked() {
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 33) chanceEdit.select();
            if (mouseY >= 17 && mouseY < 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int chanceColor = 0xFFFFFFFF;
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 33) chanceColor = 0xFFBBBBBB;
        }

        drawText("chance: " + chanceEdit, 4, 25, chanceColor);
    }
    
    @Override
    public void addToolTip(List<String> toolTip) {
        if (!inverted) {
            toolTip.add("    With a chance of " + chance + "%");
        } toolTip.add("    With a chance of " + (100 - chance) + "%");
    }
}
