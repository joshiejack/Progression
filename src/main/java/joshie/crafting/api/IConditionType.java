package joshie.crafting.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IConditionType {       
    /** Returns the name of this trigger type, used to call from the json **/
    public String getUnlocalisedName();
    
    /** Returns the localised name for this trigger type **/
    public String getLocalisedName();
    
    /** Returns the colour used for this trigger type **/
    public int getColor();
   
    /** returns true if this condition is satisfied, player can be null, so check it **/
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid);

    /** Called to load the data about this trigger from json **/
    public void readFromJSON(JsonObject object);
    
    /** Called to save the data about this trigger to json **/
    public void writeToJSON(JsonObject object);

    /** Called to draw the information when editing this trigger  */
    @SideOnly(Side.CLIENT)
    public void draw(int mouseX, int mouseY);

    /** Should return allow, if clicked, or default if not **/
    @SideOnly(Side.CLIENT)
    public Result onClicked(int mouseX, int mouseY);
}
