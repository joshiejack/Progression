package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.EventBusType;
import joshie.progression.api.ICriteria;
import joshie.progression.api.IField;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ITriggerType;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.handlers.APIHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public abstract class TriggerBase implements ITriggerType {
    protected List<IField> list = new ArrayList();
    protected ICriteria criteria;
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
        return APIHandler.newData(data);
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
        return Progression.translate("trigger." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void updateDraw() {}

    @Override
    public EventBusType[] getEventBusTypes() {
        return new EventBusType[] { getEventBus() };
    }

    protected EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        return Result.DEFAULT;
    }

    @Override
    public void drawDisplay(int mouseX, int mouseY) {}

    @Override
    public void drawEditor(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY) {

    }

    @Override
    public String getDescription() {
        return "MISSING DESCRIPTION";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.furnace_minecart);
    }

    @Override
    public void addFieldTooltip(String fieldName, List<String> tooltip) {}
}
