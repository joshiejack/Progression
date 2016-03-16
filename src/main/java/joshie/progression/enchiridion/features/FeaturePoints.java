package joshie.progression.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.features.FeatureAbstract;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.progression.player.PlayerTracker;

public class FeaturePoints extends FeatureAbstract {
    private transient double cachedWidth = 0;
    public transient int wrap = 50;
    public float size = 1F;
    public boolean wholeNumber = true;
    public String description = "[amount] Gold";
    public String variable = "gold";

    public FeaturePoints() {}
    public FeaturePoints(String desc, String var) {
        description = desc;
        variable = var;
    }
    
    @Override
    public void update(IFeatureProvider position) {
        cachedWidth = position.getWidth();
        wrap = Math.max(50, (int) (cachedWidth / size) + 4);
    }

    @Override
    public FeaturePoints copy() {
        FeaturePoints text = new FeaturePoints(description, variable);
        text.size = size;
        return text;
    }

    @Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
        if (variable != null) {
            double amount = PlayerTracker.getClientPlayer().getAbilities().getPoints("points:" + variable);
            EnchiridionAPI.draw.drawSplitScaledString(description.replace("[amount]", "" + (wholeNumber ? (int)amount : amount)), xPos, yPos, wrap, 0x555555, size);
        }
    }
    
    @Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorPoints.INSTANCE.setPoints(this));     
        return true;
    }
    
    @Override
    public void keyTyped(char character, int key) {
        if (MCClientHelper.isShiftPressed()) {
            if (key == 78) {
                size = Math.min(15F, Math.max(0.5F, size + 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            } else if (key == 74) {
                size = Math.min(15F, Math.max(0.5F, size - 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            }
        }
    }
}
