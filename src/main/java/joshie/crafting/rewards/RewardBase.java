package joshie.crafting.rewards;

import joshie.crafting.api.Bus;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IRewardType;
import net.minecraft.util.StatCollector;

public abstract class RewardBase implements IRewardType {
    protected ICriteria criteria;
    private String name;
    private int color;

    public RewardBase(String name, int color) {
        this.name = name;
        this.color = color;
    }
    
    @Override
    public void markCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return StatCollector.translateToLocal("progression.reward." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Bus[] getEventBusTypes() {
        return new Bus[] { getEventBus() };
    }
    
    protected Bus getEventBus() {
        return Bus.NONE;
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRemoved() {}
}
