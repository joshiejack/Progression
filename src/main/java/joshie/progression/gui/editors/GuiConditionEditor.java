package joshie.progression.gui.editors;

import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.gui.core.IBarProvider;
import joshie.progression.handlers.APICache;

import java.util.UUID;

import static joshie.progression.gui.core.GuiList.*;

public class GuiConditionEditor extends GuiBaseEditorRule<ITriggerProvider> implements IBarProvider {
    private UUID uuid;

    public GuiConditionEditor() {
        features.add(BACKGROUND);
        features.add(CONDITION_BG);
        features.add(CONDITIONS);
        features.add(TEXT_EDITOR_FULL); //Add the text selector
        features.add(ITEM_EDITOR); //Add the item selector
        features.add(NEW_CONDITION); //Add new trigger popup
        features.add(NEW_REWARD); //Add new reward popup
        features.add(FOOTER);
    }

    @Override
    public ITriggerProvider get() {
        return APICache.getClientCache().getTriggerFromUUID(uuid);
    }

    @Override
    public void set(ITriggerProvider trigger) {
        this.uuid = trigger.getUniqueID();
    }

    @Override
    public IEditorMode getPreviousGui() {
        return CRITERIA_EDITOR;
    }

    @Override
    public void initData() {
        if (get() == null) {
            CORE.setEditor(CRITERIA_EDITOR);
            return;
        }

        //Setup the features
        CONDITION_BG.setProvider(this);
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
                return get().getColor();
            case BAR1_GRADIENT2:
                return 0xFF000000;
            case BAR1_BORDER:
                return 0xFF000000;
            case BAR1_FONT:
                return THEME.conditionEditorFont;
            case BAR1_UNDERLINE:
                return 0xFF000000;
            default:
                return 0;
        }
    }
}
