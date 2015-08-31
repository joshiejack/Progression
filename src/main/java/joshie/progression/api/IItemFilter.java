package joshie.progression.api;

import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IItemFilter {
    /** Return true if the pass in stack, matches this item filter. */
    public boolean matches(ItemStack stack);

    /** Read data about this filter from json **/
    public void readFromJSON(JsonObject data);
    
    /** Write data about htis filter to json **/
    public void writeToJSON(JsonObject typeData);

    /** Type name **/
    public String getName();

    @SideOnly(Side.CLIENT)
    public Result onClicked(int mouseX, int mouseY);

    @SideOnly(Side.CLIENT)
    public void draw(int mouseX, int mouseY);
}
