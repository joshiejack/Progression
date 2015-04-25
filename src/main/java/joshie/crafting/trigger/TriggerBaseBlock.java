package joshie.crafting.trigger;

import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.fields.BlockField;
import joshie.crafting.gui.fields.BooleanField;
import joshie.crafting.gui.fields.TextField;
import joshie.crafting.helpers.BlockActionHelper;
import joshie.crafting.helpers.JSONHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.gson.JsonObject;

public abstract class TriggerBaseBlock extends TriggerBaseCounter {
    public String name = "IGNORE";
    public Block block = Blocks.stone;
    public int meta = 0;
    public boolean matchDamage = true;
    public ItemStack stack = new ItemStack(Blocks.stone, 1, 0);

    public TriggerBaseBlock(String unlocalised, int color) {
        super(unlocalised, color);
        list.add(new TextField("name", this));
        list.add(new BooleanField("matchDamage", this));
        list.add(new BlockField("stack", "block", "meta", this, 35, 41, 1.5F, 1, 94, 40, 99, Type.TRIGGER));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        name = JSONHelper.getString(data, "ore", "IGNORE");
        if (OreDictionary.getOres(name).size() > 0) {
            stack = OreDictionary.getOres(name).get(0);
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
        JSONHelper.setString(data, "ore", name, "IGNORE");
        if (name.equals("IGNORE")) {
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
        if (!name.equals("IGNORE")) {
        	ItemStack stack = BlockActionHelper.getStackFromBlockData(theBlock, theMeta);
            int[] ids = OreDictionary.getOreIDs(stack);
            for (int i : ids) {
                String oreName = OreDictionary.getOreName(i);
                if (oreName.equals(name)) {
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
}
