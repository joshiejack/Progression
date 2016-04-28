package joshie.progression.plugins.enchiridion.features;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.gui.book.features.FeaturePreviewWindow;
import joshie.enchiridion.gui.book.features.FeatureText;
import joshie.enchiridion.util.ELocation;
import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.gui.editors.TreeEditorElement.ColorMode;
import joshie.progression.handlers.APICache;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.lib.PInfo;
import joshie.progression.plugins.enchiridion.actions.ActionClaimCriteria;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.UUID;

import static joshie.progression.api.special.DisplayMode.EDIT;
import static joshie.progression.gui.core.GuiList.MODE;
import static joshie.progression.gui.editors.TreeEditorElement.getModeForCriteria;
import static joshie.progression.plugins.enchiridion.EnchiridionSupport.TRANSPARENT;
import static net.minecraft.util.EnumChatFormatting.GOLD;


public class FeatureCriteria extends FeatureProgression implements ISimpleEditorFieldProvider {
    protected transient UUID uuid = UUID.randomUUID();
    protected transient boolean isInit = false;
    public String display = "New Criteria";
    public boolean background = true;

    public FeatureCriteria() {}

    public FeatureCriteria(ICriteria criteria, boolean background) {
        if (criteria != null) {
            uuid = criteria.getUniqueID();
            display = getCriteria().getLocalisedName();
        }

        this.background = background;
    }

    public ICriteria getCriteria() {
        return APICache.getClientCache().getCriteria(uuid);
    }

    @Override
    public FeatureCriteria copy() {
        return new FeatureCriteria(getCriteria(), background);
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
            if (getCriteria() != null) display = getCriteria().getLocalisedName();
        } else if (field.equals("display")) {
            for (ICriteria c : APICache.getClientCache().getCriteriaSet()) {
                if (c.getLocalisedName().equals(display)) {
                    uuid = c.getUniqueID();
                }
            }
        }
    }

    public FeatureCriteria getFeatureFromCriteria(ICriteria criteria) {
        for (IFeatureProvider feature: position.getPage().getFeatures()) {
            if (feature.getFeature() instanceof FeatureCriteria) {
                FeatureCriteria fC = ((FeatureCriteria)feature.getFeature());
                if (fC.uuid != null) {
                    if (fC.uuid.equals(criteria.getUniqueID())) return fC;
                }
            }
        }

        return null;
    }

    private static final ResourceLocation unlocked = new ResourceLocation(PInfo.BOOKPATH + "hexunlocked.png");
    private static final ResourceLocation locked = new ResourceLocation(PInfo.BOOKPATH + "hexlocked.png");
    private static final ResourceLocation completed = new ResourceLocation(PInfo.BOOKPATH + "hexcompleted.png");
    private static final ResourceLocation unlockedT = new ResourceLocation(PInfo.BOOKPATH + "hexunlockedT.png");
    private static final ResourceLocation lockedT = new ResourceLocation(PInfo.BOOKPATH + "hexlockedT.png");
    private static final ResourceLocation completedT = new ResourceLocation(PInfo.BOOKPATH + "hexcompletedT.png");

    //Flashy
    private static final ResourceLocation flash = new ResourceLocation(PInfo.BOOKPATH + "flash.png");
    private static final ResourceLocation flashT = new ResourceLocation(PInfo.BOOKPATH + "flashT.png");

    private ResourceLocation getResource(ColorMode mode) {
        switch (mode) {
            case DEFAULT: return locked;
            case COMPLETED:
            case READY: return completed;
            case UNUSED: return flash;
            case AVAILABLE: return unlocked;
            default: return null;
        }
    }

    private ResourceLocation getTransparent(ColorMode mode) {
        switch (mode) {
            case DEFAULT: return lockedT;
            case COMPLETED:
            case READY:   return completedT;
            case UNUSED:  return flashT;
            case AVAILABLE: return unlockedT;
            default: return null;
        }
    }
   
    public void drawFeature(ICriteria criteria, int mouseX, int mouseY) {
        ColorMode mode = getModeForCriteria(getCriteria(), false);
        ResourceLocation location = getResource(mode);
        if (location != null) {
            if (!criteria.isVisible()) { //Make the texture transparent if this is edit MODE
                if (MODE == EDIT) {
                    location = getTransparent(mode);
                }
            }

            List<ICriteria> prereqs = criteria.getPreReqs();
            for (ICriteria c : prereqs) {
                FeatureCriteria connected = getFeatureFromCriteria(c);
                if (connected != null) {
                    int y1 = connected.position.getTop();
                    int y2 = position.getTop();
                    int x1 = connected.position.getLeft();
                    int x2 = position.getLeft();

                    EnchiridionAPI.draw.drawLine(x1 + 8, 8 + y1, 8 + x2, 8 + y2, 2, 0xFF404040);
                }
            }

            if (background) EnchiridionAPI.draw.drawImage(location, position.getLeft() - 2, position.getTop() - 3, position.getLeft() + 18, position.getTop() + 19);
            EnchiridionAPI.draw.drawStack(criteria.getIcon(), position.getLeft(), position.getTop(), 1F);
        }
    }

    @Override
    public boolean performClick(int mouseX, int mouseY, int mouseButton) {
        ICriteria criteria = getCriteria();
        if (criteria != null && (MODE == EDIT || getModeForCriteria(criteria, false).openable)) {
            int number = criteria.getUniqueID().hashCode();
            IPage page = EnchiridionAPI.book.getPageIfNotExists(number);
            if (page != null) {
                int previous = EnchiridionAPI.book.getPage().getPageNumber();
                IButtonActionProvider button = EnchiridionAPI.editor.getJumpPageButton(previous);
                button.setResourceLocation(true, new ELocation("arrow_left_on")).setResourceLocation(false, new ELocation("arrow_left_off"));
                page.addFeature(button, 21, 200, 18, 10, true, false);

                //Create the left side page
                IPage pageLeft = EnchiridionAPI.book.getPageIfNotExists(page.getPageNumber() + 1);
                pageLeft.addFeature(new FeatureText("Jump to the page after this one to edit the text."), 20, 20, 181, 81, false, false);
                pageLeft.toggleScroll();
                FeaturePreviewWindow previewLeft = new FeaturePreviewWindow(page.getPageNumber() + 1);
                page.addFeature(previewLeft, 16, 16, 189, 117, true, false);

                //Create the right side page
                IPage pageRight = EnchiridionAPI.book.getPageIfNotExists(page.getPageNumber() + 2);
                pageRight.addFeature(new FeatureText("Jump two pages after to edit this one."), 230, 20, 181, 81, false, false);
                pageRight.toggleScroll();
                FeaturePreviewWindow previewRight = new FeaturePreviewWindow(page.getPageNumber() + 2);
                page.addFeature(previewRight, 225, 16, 192, 117, true, false);

                //Add the Rewards
                FeatureRewards reward = new FeatureRewards(criteria, true);
                page.addFeature(reward, 22, 138, 17, 28, true, false);

                //Add The Tasks
                FeatureTasks tasks = new FeatureTasks(criteria, true);
                page.addFeature(tasks, 230, 138, 17, 28, true, false);

                //Add the Claim Button
                IButtonActionProvider claimButton = new FeatureButton(new ActionClaimCriteria(criteria));
                claimButton.setTooltip("Claim " + criteria.getLocalisedName());
                claimButton.setText(true, "[color=gray]Claim").setText(false, "Claim");
                claimButton.setTextOffsetX(true, 14).setTextOffsetY(true, 3);
                claimButton.setTextOffsetX(false, 14).setTextOffsetY(false, 3);
                claimButton.setResourceLocation(true, new ResourceLocation("progression:textures/books/open_button_on.png"));
                claimButton.setResourceLocation(false, new ResourceLocation("progression:textures/books/open_button_off.png"));
                page.addFeature(claimButton, 139, 175, 50, 14, true, false);

                //Return to tabs page
                IButtonActionProvider pageBack = EnchiridionAPI.editor.getJumpPageButton(previous);
                pageBack.setResourceLocation(true, TRANSPARENT).setResourceLocation(false, TRANSPARENT);
                pageBack.setProcessesClick(0, false);
                page.addFeature(pageBack, -10, -10, 450, 250, true, false);
            }

            return EnchiridionAPI.book.jumpToPageIfExists(number);
        }

        return false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!isInit) {
            isInit = true;
            onFieldsSet("");
        }

        ICriteria criteria = getCriteria();
        if (criteria != null) {
            drawFeature(criteria, mouseX, mouseY);
        }
    }
    
    @Override
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {
        ICriteria criteria = getCriteria();
        if (criteria != null && getModeForCriteria(criteria, false).openable) {
            addFeatureTooltip(criteria, tooltip, mouseX, mouseY);
        }
    }

    public void addFeatureTooltip(ICriteria criteria, List<String> tooltip, int mouseX, int mouseY) {
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
        onFieldsSet(""); //When we reopen, reload the data
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
