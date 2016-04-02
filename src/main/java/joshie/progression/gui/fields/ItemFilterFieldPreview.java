package joshie.progression.gui.fields;

import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.special.IAdditionalTooltip;
import joshie.progression.api.special.IStackSizeable;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemFilterFieldPreview extends ItemFilterField implements IProgressionField {
    private final int x;
    private final int y;
    private final float scale;
    protected final int mouseX1;
    protected final int mouseX2;
    protected final int mouseY1;
    protected final int mouseY2;
    private ItemStack stack;
    private int ticker;

    public ItemFilterFieldPreview(String fieldName, Object object, int x, int y, float scale) {
        super(fieldName, object);
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.mouseX1 = x;
        this.mouseX2 = (int) (x + 14 * scale);
        this.mouseY1 = y - 2;
        this.mouseY2 = (int) (y + 15 * scale);
    }

    @Override
    public String getField() {
        return "";
    }

    private static final ItemStack BROKEN = new ItemStack(Items.baked_potato);

    public ItemStack getStack(boolean hovered) {
        if (ticker >= 200 || ticker == 0) {
            stack = ItemHelper.getRandomItem(getFilters());
            if (stack != null) {
                if (object instanceof IStackSizeable) {
                    stack.stackSize = ((IStackSizeable) object).getStackSize();
                }
            }

            ticker = 1;
        }

        if (!hovered) ticker++;
        else if (!GuiScreen.isShiftKeyDown()) ticker += 2;

        return stack != null ? stack : BROKEN;
    }

    @Override
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        try {
            boolean hovered = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;

            if (hovered) {
                List<String> tooltip = new ArrayList();
                tooltip.addAll(getStack(hovered).getTooltip(MCClientHelper.getPlayer(), false));
                if (object instanceof IAdditionalTooltip) {
                    ((IAdditionalTooltip)object).addHoverTooltip(tooltip);
                }

                FeatureTooltip.INSTANCE.addTooltip(tooltip);
            }

            helper.drawStack(renderX, renderY, getStack(hovered), x, y, scale);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void click() {}

    @Override
    public boolean attemptClick(int mouseX, int mouseY) {
        boolean clicked = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;
        if (clicked) {
            super.click();
            return true;
        } else return false;
    }
}
