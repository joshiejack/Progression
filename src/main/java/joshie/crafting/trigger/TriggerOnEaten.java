package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.api.ITriggerNew;

import com.google.gson.JsonObject;

public class TriggerOnEaten implements ITriggerNew {
    @Override
    public ITriggerNew newInstance() {
        return new TriggerOnEaten();
    }
    
    @Override
    public ITriggerData newData() {
        return CraftingAPI.registry.newData("count");
    }
    
    @Override
    public String getUniqueName() {
        return "onEaten";
    }
    
    @Override
    public int getColor() {
        return 0xFFFFFFFF;
    }
    
    @Override
    public Bus[] getEventBusTypes() {
        return new Bus[] { Bus.NONE };
    }
    
    @Override
    public void onFired(UUID uuid, ITriggerData triggerData, Object... data) {
        
    }
    
    @Override
    public void readFromJSON(JsonObject object) {
        
    }

    @Override
    public void writeToJSON(JsonObject object) {
        
    }
}
