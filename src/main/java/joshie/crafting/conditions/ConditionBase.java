package joshie.crafting.conditions;

import joshie.crafting.api.IConditionType;
import net.minecraft.util.StatCollector;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class ConditionBase implements IConditionType {
    private String name;
    private int color;

    public ConditionBase(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }
    
    @Override
    public String getLocalisedName() {
        return StatCollector.translateToLocal("progression.condition." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }
    
    @Override
    public void readFromJSON(JsonObject data) {}
    
    @Override
    public void writeToJSON(JsonObject data) {}
    

    @Override
    public void draw(int mouseX, int mouseY) {}

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        return Result.DEFAULT;
    }
}
