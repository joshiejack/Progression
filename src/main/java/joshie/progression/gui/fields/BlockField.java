package joshie.progression.gui.fields;

import java.lang.reflect.Field;

import joshie.progression.gui.SelectItemOverlay.Type;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockField extends ItemField {
    private Field blockField;
    private Field metaField;
    
    public BlockField(String fieldName, String blockField, String metaField, Object object, int x, int y, float scale, int mouseX1, int mouseX2, int mouseY1, int mouseY2, Type type) {
        super(fieldName, object, x, y, scale, mouseX1, mouseX2, mouseY1, mouseY2, type);
        
        try {
            this.blockField = object.getClass().getField(blockField);
            this.metaField = object.getClass().getField(metaField);
        } catch (Exception e) {}
    } 
    
    private Block getBlock() {
        try {
            return (Block) blockField.get(object);
        } catch (Exception e) { return null; }
    }
    
    private int getMeta() {
        try {
            return metaField.getInt(object);
        } catch (Exception e) { return 0; }
    }
    
    @Override
    public void setItemStack(ItemStack stack) {
        Block block = null;
        int meta = 0;
        
        try {
            block = Block.getBlockFromItem(stack.getItem());
            meta = stack.getItemDamage();
        } catch (Exception e) { e.printStackTrace(); }

        if (block != null) {
            try {
                blockField.set(object, block);
                metaField.set(object, meta);
                super.setItemStack(stack);
            } catch (Exception e) {}
        }
    }

}
