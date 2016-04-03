package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ICondition;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.UUID;

public abstract class ConditionBase implements ICondition {
    private UUID uuid;
    private ITrigger trigger;
    private ItemStack icon;
    private String unlocalized;
    private int color;

    //Cached boolean
    private transient boolean isTrue = false;
    private transient int checkTick = 0;

    public boolean inverted = false; //Data for all conditions
    //Whether this is visible or not
    public boolean isVisible = true;

    public ConditionBase(ItemStack stack, String name, int color) {
        this.icon = stack;
        this.unlocalized = "condition." + name;
        this.color = color;
    }

    public ConditionBase(String name, int color) {
        this.icon = new ItemStack(Items.brick);
        this.unlocalized = "condition." + name;
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
        return StatCollector.translateToLocal("progression." + getUnlocalisedName());
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

    public String getConditionDescription() {
        if (inverted) return Progression.translate(getUnlocalisedName() + ".description.inverted");
        else return Progression.translate(getUnlocalisedName() + ".description");
    }

    protected boolean isSatisfied() {
        if (checkTick == 0 || checkTick >= 200) {
            isTrue = isSatisfied(PlayerTracker.getClientPlayer().getTeam());
            checkTick = 1;
        }

        checkTick++;
        return isTrue;
    }

    @Override
    public String getDescription() {
        if (inverted) return getConditionDescription() + "\n\n" + Progression.format("truth", !isSatisfied());
        return getConditionDescription() + "\n\n" + Progression.format("truth", isSatisfied());
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }
    
    @Override
    public void setTrigger(ITrigger trigger, UUID uuid) {
        this.trigger = trigger;
        this.uuid = uuid;
    }

    @Override
    public ITrigger getTrigger() {
        return trigger;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return 100;
    }
}
