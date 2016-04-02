package joshie.progression.gui.editors;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.gui.core.FeatureBarsX1;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IBarProvider;
import joshie.progression.gui.editors.insert.FeatureNewCondition;
import joshie.progression.gui.editors.insert.FeatureNewReward;
import joshie.progression.handlers.APIHandler;
import net.minecraft.util.EnumChatFormatting;

public class GuiConditionEditor extends GuiBaseEditor implements IBarProvider {
    public static final GuiConditionEditor INSTANCE = new GuiConditionEditor();
    private IProgressionTrigger trigger;

    private GuiConditionEditor() {}

    public void setTrigger(IProgressionTrigger trigger) {
        this.trigger = trigger;
    }
    
    public IProgressionTrigger getTrigger() {
        return trigger;
    }

    @Override
    public Object getKey() {
        return trigger;
    }

    @Override
    public IEditorMode getPreviousGui() {
        return GuiCriteriaEditor.INSTANCE;
    }

    @Override
    public void initData(GuiCore core) {
        trigger = APIHandler.getCache().getTriggerFromUUID(trigger.getUniqueID()); //Reload the trigger from the cache
        super.initData(core);
        //Setup the features
        features.add(new FeatureBarsX1(this, "condition"));
        features.add(new FeatureDrawable(EnumChatFormatting.BOLD + Progression.translate("new.condition"), trigger.getConditions(), 45, 201, 201, 64, 119, FeatureNewCondition.INSTANCE, theme.conditionGradient1, theme.conditionGradient2, theme.conditionFontColor));
        features.add(FeatureItemSelector.INSTANCE); //Add the item selector
        features.add(FeatureNewCondition.INSTANCE); //Add new trigger popup
        features.add(FeatureNewReward.INSTANCE); //Add new reward popup
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {}

    @Override
    public boolean guiMouseClicked(boolean overlayvisible, int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public int getColorForBar(BarColorType type) {
        switch (type) {
            case BAR1_GRADIENT1:
                return trigger.getColor();
            case BAR1_GRADIENT2:
                return 0xFF000000;
            case BAR1_BORDER:
                return 0xFF000000;
            case BAR1_FONT:
                return theme.conditionEditorFont;
            case BAR1_UNDERLINE:
                return 0xFF000000;
            default:
                return 0;
        }
    }
}
