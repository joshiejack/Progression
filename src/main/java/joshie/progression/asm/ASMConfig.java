package joshie.progression.asm;

import com.google.gson.annotations.SerializedName;

public class ASMConfig {
    @SerializedName("Enable Furnace ASM")
    public boolean furnace = true;
    @SerializedName("Enable Shift ASM")
    public boolean transfer = true;
    @SerializedName("Enable Player ASM")
    public boolean player = true;
    @SerializedName("Enable Workbench ASM")
    public boolean workbench = true;
    @SerializedName("Enable Tinkers ASM")
    public boolean tinkers = true;
}
