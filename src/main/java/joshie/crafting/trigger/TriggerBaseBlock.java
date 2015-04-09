package joshie.crafting.trigger;

import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IItemGettable;
import joshie.crafting.gui.TextFieldHelper.ItemAmountHelper;
import joshie.crafting.helpers.BlockActionHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class TriggerBaseBlock extends TriggerBaseCounter implements IItemSelectable, IItemGettable {
    protected TextFieldHelper oreEdit;
    protected ItemAmountHelper amountEdit;
    public String oreDictionary = "IGNORE";
    public Block block = Blocks.stone;
    public int meta = 0;
    public boolean matchDamage = true;
    public int amount = 1;
    public ItemStack stack = new ItemStack(Blocks.stone, 1, 0);

    public TriggerBaseBlock(String localised, int color, String unlocalised) {
        super(localised, color, unlocalised);
        oreEdit = new TextFieldHelper("oreDictionary", this);
        amountEdit = new ItemAmountHelper("amount", this);
    }

    @Override
    public ITrigger deserialize(JsonObject data) {
        TriggerBaseBlock trigger = (TriggerBaseBlock) newInstance();
        if (data.get("ore") != null) {
            trigger.oreDictionary = data.get("ore").getAsString();
            if (OreDictionary.getOres(trigger.oreDictionary).size() > 0) {
                trigger.stack = OreDictionary.getOres(trigger.oreDictionary).get(0);
            }
        } else {
            String stack = data.get("item").getAsString();
            trigger.stack = StackHelper.getStackFromString(stack);
            trigger.block = Block.getBlockFromItem(trigger.stack.getItem());
            trigger.meta = trigger.stack.getItemDamage();
            if (data.get("matchDamage") != null) {
                trigger.matchDamage = data.get("matchDamage").getAsBoolean();
            }
        }

        if (data.get("amount") != null) {
            trigger.amount = data.get("amount").getAsInt();
        }

        return trigger;
    }

    @Override
    public void serialize(JsonObject data) {
        if (!oreDictionary.equals("IGNORE")) {
            data.addProperty("ore", oreDictionary);
        } else {
            ItemStack stack = new ItemStack(block, 1, meta);
            String serial = StackHelper.getStringFromStack(stack);
            data.addProperty("item", serial);
            if (matchDamage != true) {
                data.addProperty("matchDamage", false);
            }
        }

        if (amount != 1) {
            data.addProperty("amount", amount);
        }
    }

    @Override
    protected boolean canIncrease(Object... data) {
        Block theBlock = asBlock(data, 0);
        int theMeta = asInt(data, 1);
        boolean doesMatch = false;
        if (!oreDictionary.equals("IGNORE")) {
        	ItemStack stack = BlockActionHelper.getStackFromBlockData(theBlock, theMeta);
            int[] ids = OreDictionary.getOreIDs(stack);
            for (int i : ids) {
                String oreName = OreDictionary.getOreName(i);
                if (oreName.equals(oreDictionary)) {
                    doesMatch = true;
                    break;
                }
            }
        } else {
            if (theBlock == block) {
                if (!matchDamage || meta == theMeta) {
                    doesMatch = true;
                }
            }
        }

        return doesMatch;
    }

    @Override
    public Result clicked() {
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) SelectTextEdit.INSTANCE.select(oreEdit);
            if (mouseY > 25 && mouseY <= 33) SelectTextEdit.INSTANCE.select(amountEdit);
            if (mouseY > 34 && mouseY <= 41) matchDamage = !matchDamage;
            if (mouseY > 45 && mouseY < 100) SelectItemOverlay.INSTANCE.select(this, Type.TRIGGER);
            if (mouseY >= 17 && mouseY < 100) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        drawStack(stack, 35, 43, 1.7F);

        int color = 0xFFFFFFFF;
        int amountColor = 0xFFFFFFFF;
        int match2Color = 0xFFFFFFFF;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 94 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = 0xFFBBBBBB;
                if (mouseY > 25 && mouseY <= 33) amountColor = 0xFFBBBBBB;
                if (mouseY > 34 && mouseY <= 41) match2Color = 0xFFBBBBBB;
            }
        }

        drawText("name: " + oreEdit, 4, 18, color);
        drawText("amount: " + amountEdit, 4, 26, amountColor);
        drawText("matchDamage: " + matchDamage, 4, 34, match2Color);
    }

    @Override
    public void setItemStack(ItemStack stack) {
        Block copy = null;
        Block block = this.block;
        int meta = 0;

        try {
            copy = Block.getBlockFromItem(stack.getItem());
            meta = stack.getItemDamage();
        } catch (Exception e) {}

        if (copy != null) {
            this.stack = new ItemStack(copy, amount, meta);
            this.block = copy;
            this.meta = meta;
        }
    }

    @Override
    public ItemStack getItemStack() {
        return stack;
    }

    @Override
    public int getAmountRequired() {
        return amount;
    }
}
