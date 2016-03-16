package joshie.progression.gui.fields;

import joshie.progression.api.IField;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemFilterFieldPreview extends ItemFilterField implements IField {
    private final int x;
    private final int y;
    private final float scale;
    protected final int mouseX1;
    protected final int mouseX2;
    protected final int mouseY1;
    protected final int mouseY2;
    private ItemStack stack;
    private int ticker;

    public ItemFilterFieldPreview(String fieldName, Object object, int x, int y, int mouseX1, int mouseX2, int mouseY1, int mouseY2, float scale, String...accepted) {
        super(fieldName, object, accepted);
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.mouseX1 = mouseX1;
        this.mouseX2 = mouseX2;
        this.mouseY1 = mouseY1;
        this.mouseY2 = mouseY2;
    }

    @Override
    public String getField() {
        return "";
    }
    
    private static final ItemStack BROKEN = new ItemStack(Items.baked_potato);
    
    public ItemStack getStack() {
        if (ticker >= 200 || ticker == 0) {
            stack = ItemHelper.getRandomItem(getFilters());
            ticker = 1;
        }
        
        ticker++;
        
        return stack != null ? stack: BROKEN;
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        try {
            helper.drawStack(renderX, renderY, getStack(), x, y, scale);
        } catch (Exception e) { e.printStackTrace(); }
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
