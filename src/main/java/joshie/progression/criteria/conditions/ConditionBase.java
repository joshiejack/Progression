package joshie.progression.criteria.conditions;

import joshie.progression.api.IConditionType;
import joshie.progression.api.ITriggerType;
import net.minecraft.util.StatCollector;

public abstract class ConditionBase implements IConditionType {
    public boolean inverted = false; //Data for all conditions
    private ITriggerType trigger;
    private String unlocalized;
    private int color;

    public ConditionBase(String name, int color) {
        this.unlocalized = name;
        this.color = color;
    }

    @Override
    public boolean isInverted() {
        return inverted;
    }

    @Override
    public String getUnlocalisedName() {
        return unlocalized;
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
    public void updateDraw() {} //Do nothing yo

    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public void setTrigger(ITriggerType trigger) {
        this.trigger = trigger;
    }

    @Override
    public ITriggerType getTrigger() {
        return trigger;
    }
}
