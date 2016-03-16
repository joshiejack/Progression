package joshie.progression.enchiridion.features;

import java.util.List;

import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.progression.api.ICriteria;
import joshie.progression.handlers.APIHandler;

public abstract class FeatureCriteria extends FeatureProgression implements ISimpleEditorFieldProvider {
    protected transient ICriteria criteria = null;
    protected String criteriaID = "";
    public String displayName = "";
    public boolean background = true;

    public FeatureCriteria() {}

    public FeatureCriteria(String displayName, boolean background) {
        this.displayName = displayName;
        this.background = background;
    }
    
    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        if (criteria != null) position.setWidth(criteria.getRewards().size() * 18.5D);
        double width = position.getWidth();
        position.setHeight(width * 0.475D);
    }

    @Override
    public void onFieldsSet() {
        super.onFieldsSet();

        try {
            for (ICriteria c : APIHandler.getCriteria().values()) {
                String display = c.getDisplayName();
                if (c.getDisplayName().equals(displayName)) {
                    criteria = c;
                    criteriaID = c.getUniqueName();
                    return;
                }
            }
        } catch (Exception e) {}
    }
   
    public abstract void drawFeature(int xPos, int yPos, double width, double height, boolean isMouseHovering);

    @Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
        if (criteriaID.equals("")) onFieldsSet();
        criteria = APIHandler.getCriteriaFromName(criteriaID);
        if (criteria != null) {
            drawFeature(xPos, yPos, width, height, isMouseHovering);
        }
    }
    
    @Override
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {
        if (criteria != null) {
            addFeatureTooltip(tooltip, mouseX, mouseY);
        }
    }

    public abstract void addFeatureTooltip(List<String> tooltip, int mouseX, int mouseY);

    @Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorPoints.INSTANCE.setFeature(this));
        return true;
    }
}
