package joshie.progression.json;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DefaultSettings {
    public Set<DataTab> tabs = new HashSet();
    
    public UUID defaultTabID = UUID.fromString("c051e0e2-f5ad-4b9d-b889-0ada75d4062b");
    public boolean disableCraftingUntilRewardAdded = false;
    public boolean disableUsageUntilRewardAdded = false;
    public boolean unclaimedTileCanDoAnything = false;
    public boolean craftAnythingCreative = false;

    public DefaultSettings setDefaults() {
        tabs.add(new DataTab(UUID.fromString("c051e0e2-f5ad-4b9d-b889-0ada75d4062b"), "Default", 0, new ArrayList(), true, new ItemStack(Items.book)));
        return this;
    }
}
