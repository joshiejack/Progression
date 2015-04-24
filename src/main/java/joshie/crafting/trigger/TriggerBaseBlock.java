package joshie.crafting.trigger;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IItemGettable;
import joshie.crafting.gui.TextFieldHelper.ItemAmountHelper;
import joshie.crafting.helpers.BlockActionHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
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
    public ItemStack stack = new ItemStack(Blocks.stone, 1, 0);

    public TriggerBaseBlock(String unlocalised, int color) {
        super(unlocalised, color);
        oreEdit = new TextFieldHelper("oreDictionary", this);
        amountEdit = new ItemAmountHelper("amount", this);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        oreDictionary = JSONHelper.getString(data, "ore", "IGNORE");
        if (OreDictionary.getOres(oreDictionary).size() > 0) {
            stack = OreDictionary.getOres(oreDictionary).get(0);
        } else {
            stack = JSONHelper.getItemStack(data, "item", new ItemStack(Blocks.stone, 1, 0));
            block = Block.getBlockFromItem(stack.getItem());
            meta = stack.getItemDamage();
            matchDamage = JSONHelper.getBoolean(data, "matchDamage", true);
        }
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setString(data, "ore", oreDictionary, "IGNORE");
        if (!oreDictionary.equals("IGNORE")) {
            ItemStack stack = new ItemStack(block, 1, meta);
            JSONHelper.setItemStack(data, "item", stack);
            JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        }
    }

    @Override
    protected boolean canIncrease(Object... data) {
        Block theBlock = (Block) data[0];
        int theMeta = (Integer) data[1];
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
    public Result onClicked(int mouseX, int mouseY) {
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
    public void draw(int mouseX, int mouseY) {
        DrawHelper.triggerDraw.drawStack(stack, 35, 43, 1.7F);

        int color = Theme.INSTANCE.optionsFontColor;
        int amountColor = Theme.INSTANCE.optionsFontColor;
        int match2Color = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 94 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 25 && mouseY <= 33) amountColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 34 && mouseY <= 41) match2Color = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.triggerDraw.drawText("name: " + oreEdit, 4, 18, color);
        DrawHelper.triggerDraw.drawText("amount: " + amountEdit, 4, 26, amountColor);
        DrawHelper.triggerDraw.drawText("matchDamage: " + matchDamage, 4, 34, match2Color);
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
}
