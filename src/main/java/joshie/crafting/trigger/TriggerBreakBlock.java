package joshie.crafting.trigger;

import java.util.List;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IItemGettable;
import joshie.crafting.gui.TextFieldHelper.ItemAmountHelper;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerBreakBlock extends TriggerBaseBlock implements IItemSelectable, IItemGettable {
    public TriggerBreakBlock() {
        super("Break Block", 0xFFCCCCCC, "breakBlock");
        oreEdit = new TextFieldHelper("oreDictionary", this);
        amountEdit = new ItemAmountHelper("amount", this);
    }

    @Override
    public ITrigger newInstance() {
        return new TriggerBreakBlock();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(BreakEvent event) {
        CraftingAPI.registry.fireTrigger(event.getPlayer(), getTypeName(), event.block, event.blockMetadata);
    }

    @Override
    public void addTooltip(List<String> toolTip) {
        if (oreDictionary.equals("IGNORE")) {
            toolTip.add("  Break " + amount + " " + stack.getDisplayName());
        } else toolTip.add("  Break " + amount + " " + oreDictionary);
    }
}
