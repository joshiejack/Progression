package joshie.progression.criteria.conditions;

import joshie.progression.api.criteria.IProgressionCondition;
import joshie.progression.api.criteria.IProgressionTrigger;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public abstract class ConditionBase implements IProgressionCondition {
    public boolean inverted = false; //Data for all conditions
    private IProgressionTrigger trigger;
    private ItemStack icon;
    private String unlocalized;
    private int color;

    public ConditionBase(ItemStack stack, String name, int color) {
        this.icon = stack;
        this.unlocalized = name;
        this.color = color;
    }

    public ConditionBase(String name, int color) {
        this.icon = new ItemStack(Items.brick);
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
    public ItemStack getIcon() {
        return icon;
    }
    
    @Override
    public void setTrigger(IProgressionTrigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public IProgressionTrigger getTrigger() {
        return trigger;
    }
}
