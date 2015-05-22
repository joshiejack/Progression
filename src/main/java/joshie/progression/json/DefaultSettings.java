package joshie.progression.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class DefaultSettings {
    public Set<DataTab> tabs = new HashSet();
    
    public String defaultTabID = "DEFAULT";
    public boolean disableCraftingUntilRewardAdded;
    public boolean disableUsageUntilRewardAdded;
    public boolean unclaimedTileCanCraftAnything;
    public boolean unclaimedTileCanUseAnythingForCrafting;
    public String interfaceItem = "minecraft:book";

    public DefaultSettings setDefaults() {
        tabs.add(new DataTab("DEFAULT", "Default", 0, new ArrayList(), true, new ItemStack(Items.book)));
        return this;
    }
}
