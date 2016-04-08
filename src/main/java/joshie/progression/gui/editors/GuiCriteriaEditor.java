package joshie.progression.gui.editors;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.api.gui.Position;
import joshie.progression.gui.core.FeatureBarsX2;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IBarProvider;
import joshie.progression.gui.editors.insert.FeatureNewReward;
import joshie.progression.gui.editors.insert.FeatureNewTrigger;
import joshie.progression.gui.fields.TextFieldHideable;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.item.ItemStack;

import static joshie.progression.Progression.translate;
import static joshie.progression.api.special.DisplayMode.DISPLAY;
import static joshie.progression.api.special.DisplayMode.EDIT;
import static joshie.progression.gui.editors.FeatureReward.selected;
import static net.minecraft.util.EnumChatFormatting.BOLD;
import static net.minecraft.util.EnumChatFormatting.ITALIC;

public class GuiCriteriaEditor extends GuiBaseEditor implements IBarProvider, IItemSelectable, IEditorMode {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();
    private ICriteria criteria;

    //Fields
    private IField name;
    private IField popup;
    private IField rewards;
    private IField tasks;
    private IField repeat;

    private GuiCriteriaEditor() {}

    public void setCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }

    public ICriteria getCriteria() {
        return criteria;
    }

    @Override
    public Object getKey() {
        return criteria;
    }

    @Override
    public IEditorMode getPreviousGui() {
        return GuiTreeEditor.INSTANCE;
    }

    @Override
    public void initData(GuiCore core) {
        criteria = APIHandler.getCache(true).getCriteria().get(criteria.getUniqueID()); //Reload the criteria from the cache
        if (criteria == null) {
            GuiCore.INSTANCE.setEditor(GuiTreeEditor.INSTANCE);
            return;
        }

        super.initData(core);
        //Setup the features
        features.add(new FeatureTrigger(criteria));
        features.add(new FeatureReward(criteria));
        features.add(new FeatureBarsX2(this, "trigger", "reward"));
        features.add(FeatureFullTextEditor.INSTANCE); //Add the text selector
        features.add(FeatureItemSelector.INSTANCE); //Add the item selector
        features.add(FeatureNewTrigger.INSTANCE); //Add new trigger popup
        features.add(FeatureNewReward.INSTANCE); //Add new reward popup

        name = ProgressionAPI.fields.getText(criteria, "displayName");
        rewards = ProgressionAPI.fields.getToggleBoolean(criteria, "allRewards", "rewardsGiven");
        tasks = ProgressionAPI.fields.getToggleBoolean(criteria, "allTasks", "tasksRequired");
        repeat = ProgressionAPI.fields.getToggleBoolean(criteria, "infinite", "isRepeatable");
        if (MCClientHelper.isInEditMode()) {
            popup = ProgressionAPI.fields.getBoolean(criteria, "achievement");
        }
    }

    private boolean returnedBoolean(IField field) {
        return !((TextFieldHideable)field).isVisible();
    }

    public String translateCriteria(String s) {
        return translate("criteria." + s);
    }

    private void addCriteriaTooltip(String s) {
        addTooltip(BOLD + translateCriteria(s));
        addTooltip(translateCriteria(s + ".tooltip"), 30);
    }

    private boolean clickHeader(int mouseLeft, int mouseRight, int mouseY, int button) {
        if (mouseY >= 8 && mouseY <= 18) {
            if (mouseRight >= 100 && mouseRight <= 170) {
                return popup.click(button);
            } else if (mouseRight >= 10 && mouseRight <= 90) {
                return repeat.click(button);
            }

            if (mouseLeft <= 170 && mouseLeft > 20) {
                return name.click(button);
            } else if (mouseLeft > 0 && mouseLeft < 18 && mouseY >= 4 && mouseY <= 20) {
                return  FeatureItemSelector.INSTANCE.select(FilterTypeItem.INSTANCE, this, Position.TOP);
            }
        }

        if (mouseLeft >= 140 && mouseLeft <= 240) {
            if (mouseY >= 26 && mouseY <= 36) {
                return tasks.click(button);
            } else if (mouseY >= 123 && mouseY <= 133) {
                return rewards.click(button);
            }
        }

        return false;
    }

    private void drawHeader(boolean overlay, int mouseX, int mouseY) {
        String displayName = mode == EDIT ? translate("name.display") + ": " + name : name.toString();
        drawText(displayName, 21, 9, theme.criteriaEditDisplayNameColor);
        drawStack(criteria.getIcon(), 1, 4, 1F);

        if (mode == EDIT) drawText(translate("popup") + ": " + popup, core.screenWidth - 170, 9, theme.criteriaEditDisplayNameColor);
        drawText(translate("repeat") + ": " + (returnedBoolean(repeat) ? repeat : repeat + "x"), core.screenWidth - 90, 9, theme.criteriaEditDisplayNameColor);
        if (!overlay && mouseY >= 4 && mouseY <= 20) {
            if (mouseY >= 8 && mouseY <= 18) {
                if (mouseX >= 100 && mouseX <= 90) {
                    addCriteriaTooltip("repeat"); //Tooltip for repeatability
                    if (returnedBoolean(repeat)) addTooltip(ITALIC + "  " + translateCriteria("repeat.infinite"));
                    else addTooltip(ITALIC + "  " + translateCriteria("repeat.numbers"));
                } else if (mouseX >= 100 && mouseX <= 170) {
                    addCriteriaTooltip("popup"); //Tooltip for popup
                }
            }

            //Add the tooltip for the icon
            if (mouseX > 0 && mouseX < 18) {
                addTooltip(BOLD + "" + translateCriteria("icon"));
                addTooltip(ITALIC + "  " + translateCriteria("icon.tooltip"));
            }
        }
    }

    private boolean clickTriggers(int mouseX, int mouseY, int button) {
        return mouseX >= 140 && mouseX <= 240 && mouseY >= 26 && mouseY <= 36 ? tasks.click(button) : false;
    }

    private void drawTriggers(boolean overlay, int mouseX, int mouseY) {
        drawText("             " + translate("required") + ": " + tasks.getField(), 100, 29, theme.criteriaEditDisplayNameColor);
        if (!overlay) {
            if (mouseX >= 140 && mouseX <= 240 && mouseY >= 26 && mouseY <= 36) {
                addCriteriaTooltip("tasks");
                if (returnedBoolean(tasks)) addTooltip(ITALIC + "  " + translateCriteria("tasks.all"));
                else addTooltip(ITALIC + "  " + translateCriteria("tasks.amount"));
            }
        }
    }

    private boolean clickRewards(int mouseX, int mouseY, int button) {
        return mouseX >= 140 && mouseX <= 240 && mouseY >= 123 && mouseY <= 133 ? rewards.click(button) : false;
    }

    private void drawRewards(boolean overlay, int mouseX, int mouseY) {
        //Universal Mode
        if (mode == EDIT) {
            drawText(translate("given") + ": " + rewards.getField(), 140, 124, theme.criteriaEditDisplayNameColor);
            if (!overlay) {
                if (mouseX >= 140 && mouseX <= 240 && mouseY >= 123 && mouseY <= 133) {
                    addCriteriaTooltip("rewards");
                    if (returnedBoolean(rewards)) addTooltip(ITALIC + "  " + translateCriteria("rewards.all"));
                    else addTooltip(ITALIC + "  " + translateCriteria("rewards.amount"));
                }
            }
        } else  {
            for (ITriggerProvider provider: getCriteria().getTriggers()) {
                if (!provider.getProvided().isCompleted()) return; //Don't continue processing if we can't claim any rewards
            }

            int maximum = criteria.givesAllRewards() ? criteria.getRewards().size(): criteria.getAmountOfRewards();
            int standard = 0;
            for (IRewardProvider reward: criteria.getRewards()) {
                if (!reward.mustClaim()) standard++;
            }

            int current = selected.size() + standard;
            int number = maximum - current;
            if (number > 0) {
                drawText("Please Select " + number + " Rewards", 140, 124, 0xFFFFFFFF);
            }
        }
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {
        if (criteria == null) return; //Don't draw if no criteria!
        drawHeader(overlayvisible, core.screenWidth - mouseX, mouseY);
        drawTriggers(overlayvisible, mouseX, mouseY);
        drawRewards(overlayvisible, mouseX, mouseY);
    }

    @Override
    public boolean guiMouseClicked(boolean overlayvisible, int mouseLeft, int mouseY, int button) {
        if (mode == DISPLAY || overlayvisible) return false;
        if (clickHeader(mouseLeft, core.screenWidth - mouseLeft, mouseY, button)) return true;
        if (clickTriggers(mouseLeft, mouseY, button)) return true;
        if (clickRewards(mouseLeft, mouseY, button)) return true;
        else return false;
    }

    @Override
    public void setObject(Object object) {
        criteria.setIcon((ItemStack) object);
    }

    @Override
    public int getColorForBar(BarColorType type) {
        switch (type) {
            case BAR1_GRADIENT1:
                return theme.triggerBoxGradient1;
            case BAR1_GRADIENT2:
                return theme.triggerBoxGradient2;
            case BAR1_BORDER:
                return theme.triggerBoxUnderline1;
            case BAR1_FONT:
                return theme.triggerBoxFont;
            case BAR1_UNDERLINE:
                return theme.triggerBoxUnderline1;
            case BAR2_GRADIENT1:
                return theme.rewardBoxGradient1;
            case BAR2_GRADIENT2:
                return theme.rewardBoxGradient2;
            case BAR2_BORDER:
                return theme.rewardBoxBorder;
            case BAR2_FONT:
                return theme.rewardBoxFont;
            default:
                return 0;
        }
    }
}
