package joshie.crafting.api;

import net.minecraft.item.ItemStack;

public interface ICriteriaEditor extends IEditor {
    public void drawText(String text, int x, int y, int color);
    public void drawBox(int x, int y, int width, int height, int color, int border);
    public void drawStack(ItemStack stack, int x, int y, float scale);
}
