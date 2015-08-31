package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.gui.editors.SelectItem.Type;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.ItemAmountField;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

public class RewardClear extends RewardBase {
    public ItemStack stack = new ItemStack(Items.diamond);
    public String orename = "IGNORE";
    public boolean matchDamage = true;
    public boolean matchNBT = false;

    public RewardClear() {
        super("clear", 0xFF69008C);
        ItemField field = new ItemField("stack", this, 50, 40, 2F, 10, 100, 30, 100, Type.REWARD);
        list.add(new TextField("orename", this));
        list.add(new BooleanField("matchDamage", this));
        list.add(new BooleanField("matchNBT", this));
        list.add(new ItemAmountField("stack", "stack", field));
        list.add(field);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", stack);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
    }

    @Override
    public void reward(UUID uuid) {
        int toTake = stack.stackSize;
        int taken = 0;
        
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        for (int k = 0; k < players.size() && taken < toTake; k++) {
            EntityPlayer player = players.get(k);
            if (player != null) {
                for (int i = 0; i < player.inventory.mainInventory.length && taken < toTake; i++) {
                    ItemStack check = player.inventory.mainInventory[i];
                    int decrease = 0;

                    if (check != null) {
                        for (int j = 0; j < check.stackSize && taken < toTake; j++) {
                            if (check.getItem() == stack.getItem()) {
                                if (matchDamage && stack.getItemDamage() != check.getItemDamage()) continue;
                                if (matchNBT && stack.getTagCompound() != check.getTagCompound()) continue;
                                decrease++;
                                taken++;
                            }
                        }

                        if (decrease > 0) player.inventory.decrStackSize(i, decrease);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Remove Item");
        list.add(stack.getDisplayName() + " x" + stack.stackSize);
    }
}
