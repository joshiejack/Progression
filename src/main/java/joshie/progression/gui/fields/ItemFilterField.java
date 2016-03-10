package joshie.progression.gui.fields;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;

public class ItemFilterField extends AbstractField {
    public ItemFilterField() {
        super("");
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        helper.drawSplitText(renderX, renderY, "Item Editor", 4, yPos, 105, color);
    }
    
    @Override
    public void click() {
        
    }
}
