package joshie.crafting.json;

import com.google.gson.annotations.SerializedName;

public class Options {
	@SerializedName("Enable Editor")
	public boolean editor = true;
	@SerializedName("Display Criteria")
    public boolean display = true;
	@SerializedName("Server Sync Quests")
	public boolean sync = true;
	@SerializedName("Default Tab")
	public String defaultTab = "DEFAULT";
}
