package joshie.progression.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class DefaultSettings {
    public Set<DataTab> tabs = new HashSet();
    
    public String defaultTabID = "DEFAULT";
    public boolean disableCraftingUntilRewardAdded = false;
    public boolean disableUsageUntilRewardAdded = false;
    public boolean unclaimedTileCanCraftAnything = false;
    public boolean unclaimedTileCanUseAnythingForCrafting = false;
    public boolean craftAnythingCreative = false;

    public DefaultSettings setDefaults() {
        tabs.add(new DataTab("DEFAULT", "Default", 0, new ArrayList(), true, new ItemStack(Items.book)));
        return this;
    }
}
