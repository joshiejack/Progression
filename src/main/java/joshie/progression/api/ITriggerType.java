package joshie.progression.api;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITriggerType extends IFieldProvider {
    /** Associates this reward type with the criteria
     *  Most trigger types will not need access to this. **/
    public void markCriteria(ICriteria criteria);
    
    /** Create a new instance for storing data **/
    public ITriggerData newData();
        
    /** Returns the name of this trigger type, used to call from the json **/
    public String getUnlocalisedName();
    
    /** Returns the localised name for this trigger type **/
    public String getLocalisedName();
    
    /** Returns the description of this trigger, to be displayed in the book **/
    public String getDescription();
    
    /** Returns the colour used for this trigger type **/
    public int getColor();
    
    /** Called when ticking the gui **/
	public void update();

    /** This class will automatically be registered/de-registered from the event
     *  buses that are returned from this */
    public EventBusType[] getEventBusTypes();
    
    /** Called to fire this trigger
     *  @return     return false if the trigger cancelled the event **/
    public boolean onFired(UUID uuid, ITriggerData triggerData, Object... data);
    
    /** Called to determine if this trigger has been completed as of yet **/
    public boolean isCompleted(ITriggerData triggerData);

    /** Called to load the data about this trigger from json **/
    public void readFromJSON(JsonObject object);
    
    /** Called to save the data about this trigger to json **/
    public void writeToJSON(JsonObject object);

    /** Called to draw the information when editing this trigger  */
    @SideOnly(Side.CLIENT)
    public void drawEditor(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY);
    
    /** Called to draw this trigger out of edit mode **/
	public void drawDisplay(int mouseX, int mouseY);

    /** Should return allow, if clicked, or default if not **/
    @SideOnly(Side.CLIENT)
    public Result onClicked(int mouseX, int mouseY);

    public boolean isCanceling();

    public boolean isCancelable();

    public void setCanceling(boolean b);

    public void addFieldTooltip(String fieldName, List<String> tooltip);
}
