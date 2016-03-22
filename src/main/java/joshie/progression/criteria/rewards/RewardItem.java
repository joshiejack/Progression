package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.IStackSizeable;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketRewardItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class RewardItem extends RewardBaseItemFilter implements ISpecialFieldProvider, IStackSizeable {
    public int stackSize = 1;

    public RewardItem() {
        super("item", 0xFFE599FF);
    }
    
    @Override
    public int getStackSize() {
        return stackSize;
    }

    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("filters");
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(new ItemFilterFieldPreview("filters", this, 25, 30, 2.8F));
        else fields.add(new ItemFilterFieldPreview("filters", this, 25, 25, 2.8F));
    }
    
    @Override
    public String getLocalisedName() {
        return MCClientHelper.isInEditMode() ? Progression.translate("reward." + getUnlocalisedName()) : Progression.translate("reward." + getUnlocalisedName() + ".display");
    }
    
    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + Progression.translate("item.free"));
        list.add(getIcon().getDisplayName() + " x" + stackSize);
    }

    @Override
    public void reward(UUID uuid) {
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        for (EntityPlayerMP player : players) {
            if (player != null) {
                ItemStack stack = ItemHelper.getRandomItemOfSize(filters, stackSize);
                if (stack != null) {
                    PacketHandler.sendToClient(new PacketRewardItem(stack.copy()), (EntityPlayerMP) player);
                    SpawnItemHelper.addToPlayerInventory(player, stack.copy());
                }
            }
        }
    }
}
