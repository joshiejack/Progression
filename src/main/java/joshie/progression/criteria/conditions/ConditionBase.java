package joshie.progression.criteria.conditions;

import joshie.progression.api.criteria.IProgressionCondition;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.api.special.DisplayMode;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.UUID;

public abstract class ConditionBase implements IProgressionCondition {
    private UUID uuid;
    private IProgressionTrigger trigger;
    private ItemStack icon;
    private String unlocalized;
    private int color;

    public boolean inverted = false; //Data for all conditions
    //Whether this is visible or not
    public boolean isVisible = true;

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
    public boolean isVisible() {
        return isVisible;
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
    public UUID getUniqueID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    @Override
    public int getColor() {
        return trigger.getColor();
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
    public void setTrigger(IProgressionTrigger trigger, UUID uuid) {
        this.trigger = trigger;
        this.uuid = uuid;
    }

    @Override
    public IProgressionTrigger getTrigger() {
        return trigger;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return 100;
    }
}
