package joshie.progression.gui.fields;

import joshie.enchiridion.helpers.MCClientHelper;
import joshie.progression.Progression;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.FeatureItemPreview;

public class BlockFilterField extends ItemFilterField {
    public BlockFilterField(String field, Object object) {
        super(field, object);
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        helper.drawSplitText(renderX, renderY, "Block Editor", 4, yPos, 105, color, 0.75F);
    }

    @Override
    public void click() {
        try {
            GuiItemFilterEditor.INSTANCE.setFilterSet(this); //Adjust this filter object
            FeatureItemPreview.INSTANCE.select(false); //Allow for selection of multiple items 
            MCClientHelper.getPlayer().openGui(Progression.instance, 3, MCClientHelper.getWorld(), 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
