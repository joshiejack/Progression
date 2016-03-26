package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.handlers.APIHandler;

import java.util.List;


public abstract class FeatureCriteria extends FeatureProgression implements ISimpleEditorFieldProvider {
    protected transient IProgressionCriteria criteria = null;
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
        if (criteria != null) position.setWidth(criteria.getTriggers().size() * 19D);
        double width = position.getWidth();
        position.setHeight(width * 0.35D);
    }

    @Override
    public void onFieldsSet() {
        super.onFieldsSet();

        try {
            for (IProgressionCriteria c : APIHandler.getCriteria().values()) {
                String display = c.getDisplayName();
                if (c.getDisplayName().equals(displayName)) {
                    criteria = c;
                    criteriaID = c.getUniqueName();
                    return;
                }
            }
        } catch (Exception e) {}
    }
   
    public abstract void drawFeature(int mouseX, int mouseY);

    @Override
    public void draw(int mouseX, int mouseY) {
        if (criteriaID.equals("")) onFieldsSet();
        criteria = APIHandler.getCriteriaFromName(criteriaID);
        if (criteria != null) {
            drawFeature(mouseX, mouseY);
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
        EnchiridionAPI.editor.setSimpleEditorFeature(this);
        return true;
    }
}
