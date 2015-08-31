package joshie.progression.gui.fields;

import joshie.progression.api.ProgressionAPI;

public class ItemFilterField extends AbstractField {
    public ItemFilterField() {
        super("");
    }

    @Override
    public void draw(int color, int yPos) {
        ProgressionAPI.draw.drawSplitText("Item Editor", 4, yPos, 105, color);
    }
    
    @Override
    public void click() {
        
    }
}
