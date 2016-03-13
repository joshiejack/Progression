package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.api.IItemFilter;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.SpawnItemHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketRewardItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class RewardItem extends RewardBase {
    private static final ItemStack BROKEN = new ItemStack(Items.baked_potato);
    public List<IItemFilter> filters = new ArrayList();
    public int stackSize = 1;
    private ItemStack preview;
    private int ticker;

    public RewardItem() {
        super("item", 0xFFE599FF);
        list.add(new TextField("stackSize", this));
        list.add(new ItemFilterField("filters", this));
        list.add(new ItemFilterFieldPreview("filters", this, false, 25, 30, 26, 70, 25, 75, 2.8F));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stackSize = JSONHelper.getInteger(data, "stackSize", 1);
        filters = JSONHelper.getItemFilters(data, "filters");
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setInteger(data, "stackSize", stackSize, 1);
        JSONHelper.setItemFilters(data, "filters", filters);
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            ItemStack stack = ItemHelper.getRandomItem(filters, false).copy();
            stack.stackSize = stackSize;
            
            PacketHandler.sendToClient(new PacketRewardItem(stack.copy()), (EntityPlayerMP)player);
            SpawnItemHelper.addToPlayerInventory(player, stack.copy());
        }
    }

    @Override
    public ItemStack getIcon() {
        if (ticker == 0 || ticker >= 200) {
            preview = ItemHelper.getRandomItem(filters, false);
            ticker = 1;
        }
        
        ticker++;
        
        return preview == null ? BROKEN: preview;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Free Item");
        ItemStack stack = preview == null ? BROKEN: preview;;
        list.add(stack.getDisplayName() + " x" + stackSize);
    }
}
