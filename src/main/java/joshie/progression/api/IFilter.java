package joshie.progression.api;

import com.google.gson.JsonObject;

import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IFilter {
    /** Read data about this filter from json **/
    public void readFromJSON(JsonObject data);
    
    /** Write data about htis filter to json **/
    public void writeToJSON(JsonObject typeData);

    /** Type name **/
    public String getName();

    @SideOnly(Side.CLIENT)
    public Result onClicked(int mouseX, int mouseY);

    @SideOnly(Side.CLIENT)
    public void drawEditor(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY);
}
