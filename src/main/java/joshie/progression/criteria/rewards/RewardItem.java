package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketRewardItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

@ProgressionRule(name="item", color=0xFFE599FF)
public class RewardItem extends RewardBaseItemFilter implements ICustomDisplayName, ICustomDescription, ICustomWidth, ICustomTooltip, ISpecialFieldProvider, IStackSizeable {
    public int stackSize = 1;

    @Override
    public String getDisplayName() {
        return MCClientHelper.isInEditMode() ? Progression.translate(getProvider().getUnlocalisedName()) : Progression.translate(getProvider().getUnlocalisedName() + ".display");
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 55;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + Progression.translate("item.free"));
        list.add(getIcon().getDisplayName() + " x" + stackSize);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(new ItemFilterFieldPreview("filters", this, 25, 30, 2.8F));
        else fields.add(new ItemFilterFieldPreview("filters", this, 5, 25, 2.8F));
    }

    @Override
    public int getStackSize() {
        return stackSize;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        ItemStack stack = ItemHelper.getRandomItemOfSize(filters, stackSize);
        if (stack != null) {
            PacketHandler.sendToClient(new PacketRewardItem(stack.copy()), (EntityPlayerMP) player);
            SpawnItemHelper.addToPlayerInventory(player, stack.copy());
        }
    }
}
