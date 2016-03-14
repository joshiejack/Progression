package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.Progression;
import joshie.progression.api.EventBusType;
import joshie.progression.api.ICriteria;
import joshie.progression.api.IField;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ITriggerType;
import joshie.progression.gui.fields.AbstractField;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public abstract class TriggerBase implements ITriggerType {
    protected List<IField> list = new ArrayList();
    protected ICriteria criteria;
    private String name;
    private int color;
    private String data;
    protected boolean cancelable = false;
    public boolean cancel = false;

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
    public void update() {}

    @Override
    public EventBusType[] getEventBusTypes() {
        return new EventBusType[] { getEventBus() };
    }

    protected EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData existing, Object... additional) {
        if (cancelable) return cancel ? false : true;
        else return true;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        if (cancelable) cancel = JSONHelper.getBoolean(data, "cancel", cancel);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        if (cancelable) JSONHelper.setBoolean(data, "cancel", cancel, false);
    }
    
    @Override
    public List<IField> getFields() {
        return list;
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
    public boolean isCancelable() {
        return cancelable;
    }
    
    @Override
    public boolean isCanceling() {
        return cancel;
    }
    
    @Override
    public void setCanceling(boolean cancel) {
        this.cancel = cancel;
    }
    
    @Override
    public void addFieldTooltip(String fieldName, List<String> tooltip) {}
}
