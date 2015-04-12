package joshie.crafting.conditions;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class ConditionBiomeType extends ConditionBase implements ITextEditable {
    private Type[] biomeTypes = new Type[] { Type.FOREST };

    public ConditionBiomeType() {
        super("Biome Type", theme.conditionBiomeType, "biomeType");
    }

    private Type getBiomeType(String string) {
        for (Type t : Type.values()) {
            if (t.name().equalsIgnoreCase(string)) return t;
        }

        return Type.FOREST;
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (player == null) return false;
        Type types[] = BiomeDictionary.getTypesForBiome(world.getBiomeGenForCoords((int) player.posX, (int) player.posZ));
        for (Type type : biomeTypes) {
            for (Type compare : types) {
                if (compare == type) return true;
            }
        }

        return false;
    }

    @Override
    public ICondition deserialize(JsonObject data) {
        ConditionBiomeType condition = new ConditionBiomeType();
        JsonArray array = data.get("types").getAsJsonArray();
        Type[] types = new Type[array.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = getBiomeType(array.get(i).getAsString());
        }

        condition.biomeTypes = types;
        return condition;
    }

    @Override
    public void serialize(JsonObject elements) {
        JsonArray array = new JsonArray();
        for (Type t : biomeTypes) {
            array.add(new JsonPrimitive(t.name().toLowerCase()));
        }

        elements.add("types", array);
    }

    @Override
    public ICondition newInstance() {
        return new ConditionBiomeType();
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 200) SelectTextEdit.INSTANCE.select(this);
            if (mouseY >= 17 && mouseY <= 57) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int matchColor = theme.optionsFontColor;
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 200) matchColor = theme.optionsFontColorHover;
        }

        drawSplitText("biomeTypes: " + SelectTextEdit.INSTANCE.getText(this), 4, 26, matchColor, 100);
    }

    private String textField;

    @Override
    public String getTextField() {
        if (textField == null) {
            StringBuilder builder = new StringBuilder();
            boolean first = false;
            for (Type t : biomeTypes) {
                if (!first) {
                    first = true;
                } else builder.append(", ");

                builder.append(t.name().toLowerCase());
            }

            textField = builder.toString();
        }

        return textField;
    }

    public Type getTypeFromName(String name) {
        for (Type type : Type.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return Type.FOREST;
    }

    @Override
    public void setTextField(String str) {
        String[] split = str.split(",");
        StringBuilder fullString = new StringBuilder();
        try {
            Type[] types = new Type[split.length];
            for (int i = 0; i < types.length; i++) {
                types[i] = getTypeFromName(split[i].trim());
            }

            biomeTypes = types;
        } catch (Exception e) {}

        textField = str;
    }

    @Override
    public void addToolTip(List<String> toolTip) {
        if (textField == null) {
            StringBuilder builder = new StringBuilder();
            boolean first = false;
            for (Type t : biomeTypes) {
                if (!first) {
                    first = true;
                } else builder.append(", ");

                builder.append(t.name().toLowerCase());
            }

            textField = builder.toString();
        }

        String prefix = inverted ? "    Not in" : "    In";
        toolTip.add(prefix + " " + textField);
    }
}
