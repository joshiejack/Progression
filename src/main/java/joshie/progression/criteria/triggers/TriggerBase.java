package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.IConditionType;
import joshie.progression.api.ICriteria;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ITriggerType;
import joshie.progression.handlers.APIHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public abstract class TriggerBase implements ITriggerType {
    private List<IConditionType> conditions = new ArrayList();
    private ICriteria criteria;
    private String name;
    private int color;
    private String data;
    private ItemStack stack;
    
    public TriggerBase(ItemStack stack, String name, int color, String data) {
        this.name = name;
        this.color = color;
        this.data = data;
        this.stack = stack;
    }

    public TriggerBase(String name, int color, String data) {
        this.name = name;
        this.color = color;
        this.data = data;
        this.stack = new ItemStack(Blocks.stone);
    }
    
    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public List<IConditionType> getConditions() {
        return conditions;
    }
    
    @Override
    public ICriteria getCriteria() {
        return criteria;
    }

    @Override
    public ITriggerData newData() {
        return APIHandler.newData(data);
    }

    @Override
    public void setCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return Progression.translate("trigger." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void updateDraw() {}

    @Override
    public String getDescription() {
        return "MISSING DESCRIPTION";
    }
    
    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }
}
