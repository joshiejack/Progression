package joshie.progression.api;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRewardType extends IFieldProvider {    
    /** Associates this reward type with the criteria
     *  Most reward types will not need access to this. **/
    public void markCriteria(ICriteria criteria);
    
    /** Returns the unlocalised name of this reward
     *  This name is what is us in the json **/
    public String getUnlocalisedName();
    
    /** Returns the localised name of this reward**/
    public String getLocalisedName();
    
    /** Returns the colour used in the GUI editor **/
    public int getColor();

    /** This class will automatically be registered/de-registered from the event
     *  buses that are returned from this */
    public EventBusType[] getEventBusTypes();
    
    /** Gives the reward to this UUID **/
    public void reward(UUID uuid);
    
    /** Called when the reward is added **/
    public void onAdded();
    
    /** Called when the reward is removed**/
    public void onRemoved();
    
    /** Return the itemstack that represents this reward in tree editor view **/
    public ItemStack getIcon();

    /** Add a tooltip for display in the tree view **/
    public void addTooltip(List list);

    /** Called to load the data about this trigger from json **/
    public void readFromJSON(JsonObject object);
    
    /** Called to save the data about this trigger to json **/
    public void writeToJSON(JsonObject object);

    /** Called to draw the information when editing this trigger  */
    @SideOnly(Side.CLIENT)
    public void drawEditor(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY);

    /** Called to update drawing for this render **/
    public void update();

    public String getDescription();

    public void drawDisplay(int mouseX, int mouseY);

    /** Return the name of the nbt key,
     *  return null if not using nbt */
    public String getNBTKey();
    
    /** Called to get the default tags for players **/
    public NBTTagCompound getDefaultTags(NBTTagCompound tag);

    /** Add tooltip for the fields when hovering **/
    public void addFieldTooltip(String fieldName, List<String> tooltip);
}
