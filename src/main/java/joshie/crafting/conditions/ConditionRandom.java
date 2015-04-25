package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.TextFieldHelper.DoubleFieldHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class ConditionRandom extends ConditionBase {
    private DoubleFieldHelper chanceEdit;
    public double chance = 50D;

    public ConditionRandom() {
        super("chance", 0xFF00FFBF);
        chanceEdit = new DoubleFieldHelper("chance", this);
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

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 33) chanceEdit.select();
            if (mouseY >= 17 && mouseY < 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int chanceColor = Theme.INSTANCE.optionsFontColor;
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 33) chanceColor = Theme.INSTANCE.optionsFontColorHover;
        }

        DrawHelper.drawText("chance: " + chanceEdit, 4, 25, chanceColor);
    }
}
