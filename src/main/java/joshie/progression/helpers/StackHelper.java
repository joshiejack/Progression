package joshie.progression.helpers;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class StackHelper {
    public static ItemStack getStackFromString(String str) {
        if (str == null || str.equals("")) return null;
        return getStackFromArray(str.trim().split(" "));
    }

    public static String getStringFromStack(ItemStack stack) {
        String str = Item.itemRegistry.getNameForObject(stack.getItem());
        str = str + " " + stack.getItemDamage();

        if (stack.hasTagCompound()) {
            str = str + " " + stack.stackTagCompound.toString();
        }

        str = str + " *" + stack.stackSize;

        return str;
    }

    private static NBTTagCompound getTag(String[] str, int pos) {
        String s = formatNBT(str, pos).getUnformattedText();
        try {
            NBTBase nbtbase = JsonToNBT.func_150315_a(s);
            if (!(nbtbase instanceof NBTTagCompound)) return null;
            return (NBTTagCompound) nbtbase;
        } catch (Exception nbtexception) {
            return null;
        }
    }

    public static boolean isAmount(String str) {
        return str.startsWith("*");
    }

    private static ItemStack getStackFromArray(String[] str) {
        Item item = getItemByText(str[0]);
        if (item == null) return null;
        
        int meta = 0;
        int amount = 1;
        ItemStack stack = new ItemStack(item, 1, meta);
        NBTTagCompound tag = null;
        if (str.length > 1) {            
            if (isAmount(str[1])) amount = parseAmount(str[1]);
            else meta = parseMeta(str[1]);
        }

        if (str.length > 2) {
            tag = getTag(str, 2);
            if (tag == null) amount = parseAmount(str[2]);
        }

        if (str.length > 3) {
            amount = parseAmount(str[3]);
        }
        
        stack.setItemDamage(meta);

        if (tag != null) {
            stack.setTagCompound(tag);
        }

        if (amount >= 1) {
            stack.stackSize = amount;
        }

        return stack;
    }

    private static Item getItemByText(String str) {
        Item item = (Item) Item.itemRegistry.getObject(str);
        if (item == null) {
            try {
                Item item1 = Item.getItemById(Integer.parseInt(str));
                item = item1;
            } catch (NumberFormatException numberformatexception) {
                ;
            }
        }

        return item;
    }

    private static IChatComponent formatNBT(String[] str, int start) {
        ChatComponentText chatcomponenttext = new ChatComponentText("");

        for (int j = start; j < str.length; ++j) {
            if (j > start) {
                chatcomponenttext.appendText(" ");
            }

            Object object = new ChatComponentText(str[j]);
            chatcomponenttext.appendSibling((IChatComponent) object);
        }

        return chatcomponenttext;
    }

    private static int parseMeta(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException numberformatexception) {
            return 0;
        }
    }

    private static int parseAmount(String str) {
        try {
            return Integer.parseInt(str.substring(1, str.length()));
        } catch (NumberFormatException numberformatexception) {
            return 0;
        }
    }
}