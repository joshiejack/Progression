package joshie.crafting.trigger;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IItemGettable;
import joshie.crafting.gui.TextFieldHelper.ItemAmountHelper;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerBreakBlock extends TriggerBaseBlock implements IItemSelectable, IItemGettable {
    public TriggerBreakBlock() {
        super("breakBlock", 0xFFCCCCCC);
        oreEdit = new TextFieldHelper("oreDictionary", this);
        amountEdit = new ItemAmountHelper("amount", this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(BreakEvent event) {
        CraftingAPI.registry.fireTrigger(event.getPlayer(), getUnlocalisedName(), event.block, event.blockMetadata);
    }
}
