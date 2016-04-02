package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionCondition;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICancelable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class TriggerBase implements IProgressionTrigger {
    private List<IProgressionCondition> conditions = new ArrayList();
    private IProgressionCriteria criteria;
    private UUID uuid;
    private String name;
    private int color;
    private ItemStack stack;

    //Whether this is visible or not
    public boolean isVisible = true;

    public TriggerBase(ItemStack stack, String name, int color) {
        this.name = "trigger." + name;
        this.color = color;
        this.stack = stack;
    }

    public TriggerBase(String name, int color, String data) {
        this.name = "trigger." + name;
        this.color = color;
        this.stack = new ItemStack(Blocks.stone);
    }

    public TriggerBase copyBase(TriggerBase trigger) {
        trigger.conditions = conditions;
        trigger.criteria = criteria;
        trigger.uuid = uuid;
        return trigger;
    }

    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public List<IProgressionCondition> getConditions() {
        return conditions;
    }
    
    @Override
    public IProgressionCriteria getCriteria() {
        return criteria;
    }

    @Override
    public void setCriteria(IProgressionCriteria criteria, UUID uuid) {
        this.criteria = criteria;
        this.uuid = uuid;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return Progression.translate(getUnlocalisedName());
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
        return color;
    }

    @Override
    public void updateDraw() {}

    public String getTriggerDescription() {
        return Progression.translate(getUnlocalisedName() + ".description");
    }

    @Override
    public String getDescription() {
        if (this instanceof ICancelable) {
            if(((ICancelable)this).isCanceling()) return Progression.format(getUnlocalisedName() + ".cancel");
        }

        return getTriggerDescription() + "\n\n" + Progression.format("completed", getPercentage());
    }

    protected int getPercentage() {
        return 100;
    }
    
    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IProgressionTrigger)) return false;

        IProgressionTrigger that = (IProgressionTrigger) o;
        return getUniqueID() != null ? getUniqueID().equals(that.getUniqueID()) : that.getUniqueID() == null;

    }

    @Override
    public int hashCode() {
        return getUniqueID() != null ? getUniqueID().hashCode() : 0;
    }
}
