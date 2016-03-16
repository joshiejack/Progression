package joshie.progression.api;

import java.util.List;
import java.util.UUID;

import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRewardType extends IFieldProvider {    
    /** Associates this reward type with the criteria
     *  Most reward types will not need access to this. **/
    public void markCriteria(ICriteria criteria);
       
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

    /** Called to draw the information when editing this trigger  */
    @SideOnly(Side.CLIENT)
    public void drawEditor(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY);

    /** Called to update drawing for this render **/
    public void updateDraw();

    public String getDescription();

    public void drawDisplay(int mouseX, int mouseY);

    /** Add tooltip for the fields when hovering **/
    public void addFieldTooltip(String fieldName, List<String> tooltip);
}
