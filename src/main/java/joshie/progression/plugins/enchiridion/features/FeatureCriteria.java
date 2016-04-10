package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.handlers.APIHandler;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.UUID;

import static joshie.progression.gui.editors.TreeEditorElement.getModeForCriteria;
import static net.minecraft.util.EnumChatFormatting.GOLD;


public class FeatureCriteria extends FeatureProgression implements ISimpleEditorFieldProvider {
    protected transient ICriteria criteria = null;
    protected UUID criteriaID = UUID.randomUUID();
    public String displayName = "";
    public boolean background = true;

    public FeatureCriteria() {}

    public FeatureCriteria(String displayName, boolean background) {
        this.displayName = displayName;
        this.background = background;
    }

    @Override
    public FeatureCriteria copy() {
        return new FeatureCriteria(displayName, background);
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        position.setWidth(16);
        position.setHeight(16);
    }

    @Override
    public void onFieldsSet() {
        super.onFieldsSet();

        try {
            for (ICriteria c : APIHandler.getCache(true).getCriteria().values()) {
                if (c.getLocalisedName().equals(displayName)) {
                    criteria = c;
                    criteriaID = c.getUniqueID();
                    return;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public FeatureCriteria getFeatureFromCriteria(ICriteria criteria) {
        for (IFeatureProvider feature: position.getPage().getFeatures()) {
            if (feature.getFeature() instanceof FeatureCriteria) {
                FeatureCriteria fC = ((FeatureCriteria)feature.getFeature());
                if (fC.criteria != null) {
                    if (fC.criteria.getUniqueID().equals(criteria.getUniqueID())) return fC;
                }
            }
        }

        return null;
    }

    private static final Theme theme = Theme.INSTANCE;

    private static final ResourceLocation unlocked = new ResourceLocation(ProgressionInfo.BOOKPATH + "hexunlocked.png");
    private static final ResourceLocation locked = new ResourceLocation(ProgressionInfo.BOOKPATH + "hexlocked.png");
    private static final ResourceLocation completed = new ResourceLocation(ProgressionInfo.BOOKPATH + "hexcompleted.png");
    private static final ResourceLocation error = new ResourceLocation(ProgressionInfo.BOOKPATH + "hexerror.png");

    private ResourceLocation getResource() {
        switch (getModeForCriteria(criteria, false)) {
            case DEFAULT: return locked;
            case COMPLETED: return completed;
            case AVAILABLE: return unlocked;
            default: return null;
        }
    }
   
    public void drawFeature(int mouseX, int mouseY) {
        ResourceLocation location = getResource();
        if (location != null) {
            //Large EnchiridionAPI.draw.drawImage(unlocked, position.getLeft() - 6, position.getTop() - 6 , position.getLeft() + 22, position.getTop() + 22);
            List<ICriteria> prereqs = criteria.getPreReqs();
            for (ICriteria c : prereqs) {
                FeatureCriteria connected = getFeatureFromCriteria(c);
                if (connected != null) {
                    int y1 = connected.position.getTop();
                    int y2 = position.getTop();
                    int x1 = connected.position.getLeft();
                    int x2 = position.getLeft();

                    EnchiridionAPI.draw.drawLine(x1 + 8, 8 + y1 - 1, 8 + x2, 8 + y2 - 1, 2, 0xFF404040);
                    //EnchiridionAPI.draw.drawLine(x1 + 8, 8 + y1 + 1, 8 + x2, 8 + y2 + 1, 1, theme.connectLineColor2); //#636C69
                    //EnchiridionAPI.draw.drawLine(x1 + 8, 8 + y1, 8 + x2, 8 + y2, 1, theme.connectLineColor3);
                }
            }

            EnchiridionAPI.draw.drawImage(location, position.getLeft() - 2, position.getTop() - 3, position.getLeft() + 18, position.getTop() + 19);
            EnchiridionAPI.draw.drawStack(criteria.getIcon(), position.getLeft(), position.getTop(), 1F);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (criteriaID.equals("")) onFieldsSet();
        criteria = APIHandler.getCache(true).getCriteria().get(criteriaID);
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

    public void addFeatureTooltip(List<String> tooltip, int mouseX, int mouseY) {
        tooltip.add(GOLD + criteria.getLocalisedName());

        double completion = 0;
        for (ITriggerProvider trigger: criteria.getTriggers()) {
            completion += trigger.getProvided().getPercentage();
        }

        int maxPercentage = (criteria.getTriggers().size() * 100);
        int percent = maxPercentage > 0 ? (int)(completion * 100) / maxPercentage: 100;
        tooltip.add(Progression.format("completed", percent));
    }

    @Override
    public boolean getAndSetEditMode() {
        EnchiridionAPI.editor.setSimpleEditorFeature(this);
        return true;
    }
}
