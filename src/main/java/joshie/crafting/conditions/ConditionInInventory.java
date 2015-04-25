package joshie.crafting.conditions;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.TextFieldHelper.IItemGettable;
import joshie.crafting.gui.TextFieldHelper.ItemAmountHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class ConditionInInventory extends ConditionBase implements IItemSelectable, IItemGettable {
    private static enum CheckSlots {
        HELD, ARMOR, HOTBAR, INVENTORY;
    }

    private ItemAmountHelper editAmount;
    public ItemStack stack = new ItemStack(Blocks.crafting_table);
    public boolean matchDamage = true;
    public boolean matchNBT = false;
    public int itemAmount = 1;
    private CheckSlots slotType = CheckSlots.INVENTORY;

    public ConditionInInventory() {
        super("In Inventory", theme.conditionInInventory, "ininventory");
        editAmount = new ItemAmountHelper("itemAmount", this);
    }

    @Override
    public ConditionInInventory newInstance() {
        return new ConditionInInventory();
    }

    public CheckSlots next() {
        int id = slotType.ordinal() + 1;
        if (id < CheckSlots.values().length) {
            return CheckSlots.values()[id];
        }

        return CheckSlots.values()[0];
    }

    private CheckSlots getTypeFromString(String name) {
        for (CheckSlots s : CheckSlots.values()) {
            if (s.name().equalsIgnoreCase(name)) {
                return s;
            }
        }

        return CheckSlots.INVENTORY;
    }

    @Override
    public ICondition deserialize(JsonObject data) {
        ConditionInInventory condition = new ConditionInInventory();
        condition.stack = StackHelper.getStackFromString(data.get("item").getAsString());
        if (data.get("matchDamage") != null) {
            condition.matchDamage = data.get("matchDamage").getAsBoolean();
        }

        if (data.get("matchNBT") != null) {
            condition.matchNBT = data.get("matchNBT").getAsBoolean();
        }

        if (data.get("itemAmount") != null) {
            condition.itemAmount = data.get("itemAmount").getAsInt();
        }

        if (data.get("checkType") != null) {
            condition.slotType = getTypeFromString(data.get("checkType").getAsString());
        }

        return condition;
    }

    @Override
    public void serialize(JsonObject data) {
        data.addProperty("item", StackHelper.getStringFromStack(stack));
        if (matchDamage != true) data.addProperty("matchDamage", matchDamage);
        if (matchNBT != false) data.addProperty("matchNBT", matchNBT);
        if (itemAmount != 1) data.addProperty("itemAmount", itemAmount);
        if (slotType != CheckSlots.INVENTORY) data.addProperty("checkType", slotType.name().toLowerCase());
    }

    private boolean matches(ItemStack check) {
        if (check == null) return false;
        if (stack.getItem() == check.getItem()) {
            if (matchDamage) if (stack.getItemDamage() != check.getItemDamage()) return false;
            if (matchNBT) if (stack.getTagCompound() != check.getTagCompound()) return false;
            return true;
        }

        return false;
    }

    private int getAmount(EntityPlayer player, int slots) {
        boolean hasItem = false;
        for (int i = 0; i < slots; i++) {
            if (matches(player.inventory.mainInventory[i])) {
                hasItem = true;
                break;
            }
        }

        if (!hasItem) return 0;
        int amount = 0;
        for (int i = 0; i < slots; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (matches(stack)) {
                amount += stack.stackSize;
            }
        }

        return amount;
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (player == null) return false;
        if (slotType == CheckSlots.HELD) {
            if (matches(player.getCurrentEquippedItem())) return player.getCurrentEquippedItem().stackSize >= itemAmount;
        } else if (slotType == CheckSlots.ARMOR) {
            for (ItemStack armor : player.inventory.armorInventory) {
                if (armor != null) {
                    if (matches(armor)) return armor.stackSize >= itemAmount;
                }
            }

            return false;
        } else if (slotType == CheckSlots.HOTBAR) {
            return getAmount(player, 9) >= itemAmount;
        } else if (slotType == CheckSlots.INVENTORY) {
            return getAmount(player, 36) >= itemAmount;
        }

        return false;
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX >= 27 && mouseX <= 69) {
            if (mouseY >= 62 && mouseY <= 107) {
                SelectItemOverlay.INSTANCE.select(this, Type.TRIGGER);
                return Result.ALLOW;
            }
        }

        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 25 && mouseY <= 33) matchDamage = !matchDamage;
            if (mouseY > 33 && mouseY <= 42) matchNBT = !matchNBT;
            if (mouseY > 42 && mouseY <= 51) editAmount.select();
            if (mouseY > 51 && mouseY <= 60) slotType = next();
            if (mouseY >= 25 && mouseY <= 60) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        drawStack(stack, 25, 60, 3F);
        int typeColor = theme.optionsFontColor;
        int matchColor = theme.optionsFontColor;
        int usageColor = theme.optionsFontColor;
        int typezColor = theme.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 25 && mouseY <= 33) typeColor = theme.optionsFontColorHover;
                if (mouseY > 33 && mouseY <= 42) matchColor = theme.optionsFontColorHover;
                if (mouseY > 42 && mouseY <= 51) usageColor = theme.optionsFontColorHover;
                if (mouseY > 51 && mouseY <= 60) typezColor = theme.optionsFontColorHover;
            }
        }

        drawText("matchDamage: " + matchDamage, 4, 25, typeColor);
        drawText("matchNBT: " + matchNBT, 4, 33, matchColor);
        drawText("itemAmount: " + editAmount.getText(), 4, 42, usageColor);
        drawText("slotType: " + slotType.name().toLowerCase(), 4, 51, typezColor);
    }

    @Override
    public void setItemStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getItemStack() {
        return stack;
    }

    @Override
    public void addToolTip(List<String> toolTip) {
        // TODO Auto-generated method stub

    }
}
