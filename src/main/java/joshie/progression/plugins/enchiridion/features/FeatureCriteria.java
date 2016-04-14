package joshie.progression.plugins.enchiridion.features;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.enchiridion.util.ELocation;
import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.UUID;

import static joshie.progression.gui.editors.TreeEditorElement.getModeForCriteria;
import static net.minecraft.util.EnumChatFormatting.GOLD;


public class FeatureCriteria extends FeatureProgression implements ISimpleEditorFieldProvider {
    protected transient ICriteria criteria;
    protected transient UUID uuid = UUID.randomUUID();
    protected transient boolean isInit = false;
    public String display = "New Criteria";
    public boolean background = true;

    public FeatureCriteria() {}

    public FeatureCriteria(ICriteria criteria, boolean background) {
        this.criteria = criteria;
        if (criteria != null) {
            uuid = criteria.getUniqueID();
            display = criteria.getLocalisedName();
        }

        this.background = background;
    }

    @Override
    public FeatureCriteria copy() {
        return new FeatureCriteria(criteria, background);
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        position.setWidth(16);
        position.setHeight(16);
    }

    @Override
    public void onFieldsSet(String field) {
        super.onFieldsSet(field);

        if (field.equals("")) {
            criteria = APIHandler.getCache(true).getCriteria().get(uuid);
            if (criteria != null) display = criteria.getLocalisedName();
        } else if (field.equals("display")) {
            for (ICriteria c : APIHandler.getCache(true).getCriteria().values()) {
                if (c.getLocalisedName().equals(display)) {
                    criteria = c;
                    uuid = c.getUniqueID();
                }
            }
        }
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
                }
            }

            EnchiridionAPI.draw.drawImage(location, position.getLeft() - 2, position.getTop() - 3, position.getLeft() + 18, position.getTop() + 19);
            EnchiridionAPI.draw.drawStack(criteria.getIcon(), position.getLeft(), position.getTop(), 1F);
        }
    }

    @Override
    public void performClick(int mouseX, int mouseY) {
        if (criteria != null) {
            int number = (criteria.getUniqueID().hashCode() / 100) + 50;
            IPage page = EnchiridionAPI.book.getPageIfNotExists(number);
            if (page != null) {
                IButtonActionProvider button = EnchiridionAPI.editor.getJumpPageButton(EnchiridionAPI.book.getPage().getPageNumber());
                button.getAction().setResourceLocation(true, new ELocation("arrow_left_on")).setResourceLocation(false, new ELocation("arrow_left_off"));
                page.addFeature(button, 21, 200, 18, 10, true, false);
            }

            EnchiridionAPI.book.jumpToPageIfExists(number);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!isInit) {
            isInit = true;
            onFieldsSet("");
        }

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

    @Override
    public void readFromJson(JsonObject object) {
        try {
            uuid = UUID.fromString(JSONHelper.getString(object, "uuid", "d977334a-a7e9-5e43-b87e-91df8eebfdff"));
        } catch (Exception e){}
    }

    @Override
    public void writeToJson(JsonObject object) {
        if (uuid != null) {
            JSONHelper.setString(object, "uuid", uuid.toString(), "d977334a-a7e9-5e43-b87e-91df8eebfdff");
        }
    }
}
