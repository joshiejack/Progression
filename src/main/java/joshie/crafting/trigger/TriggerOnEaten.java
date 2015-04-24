package joshie.crafting.trigger;

import joshie.crafting.api.Bus;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class TriggerOnEaten extends TriggerBaseCounter {
    public TriggerOnEaten() {
        super("onEaten", 0xFFFFFFFF);
    }
               
    @Override
    public Bus[] getEventBusTypes() {
        return new Bus[] { Bus.NONE };
    }

    @Override
    protected boolean canIncrease(Object... data) {
        return true;
    }
        
    @Override
    public void readFromJSON(JsonObject object) {
        super.readFromJSON(object);
    }

    @Override
    public void writeToJSON(JsonObject object) {
        super.writeToJSON(object);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        return Result.DEFAULT;
    }
    
    @Override
    public void draw(int mouseX, int mouseY) {
        // TODO Auto-generated method stub
        
    }
}
