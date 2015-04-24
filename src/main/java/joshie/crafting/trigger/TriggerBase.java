package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.api.ITriggerType;
import net.minecraft.util.StatCollector;

public abstract class TriggerBase implements ITriggerType {
    private String name;
    private int color;
    private String data;
    
    public TriggerBase(String name, int color, String data) {
        this.name = name;
        this.color = color;
        this.data = data;
    }

    @Override
    public ITriggerData newData() {
        return CraftingAPI.registry.newData(data);
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }
    
    @Override
    public String getLocalisedName() {
        return StatCollector.translateToLocal("progression.trigger." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Bus[] getEventBusTypes() {
        return new Bus[] { getEventBus() } ;
    }
    
    protected Bus getEventBus() {
        return Bus.FORGE;
    }
}
