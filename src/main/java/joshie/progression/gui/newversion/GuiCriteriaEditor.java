package joshie.progression.gui.newversion;

import java.util.ArrayList;

import joshie.progression.Progression;
import joshie.progression.api.ICriteria;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.BooleanFieldHideable;
import joshie.progression.gui.fields.TextField;
import joshie.progression.gui.fields.TextFieldHideable;
import joshie.progression.gui.newversion.overlays.FeatureBarsX2;
import joshie.progression.gui.newversion.overlays.FeatureDrawable;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.newversion.overlays.FeatureNewReward;
import joshie.progression.gui.newversion.overlays.FeatureNewTrigger;
import joshie.progression.gui.newversion.overlays.FeatureTooltip;
import joshie.progression.gui.newversion.overlays.IBarProvider;
import joshie.progression.gui.newversion.overlays.TextEditor;
import joshie.progression.gui.selector.filters.ItemFilter;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.GuiIDs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class GuiCriteriaEditor extends GuiCore implements IBarProvider, IItemSelectable {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();
    private ICriteria criteria;

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
    public int getPreviousGuiID() {
        return GuiIDs.TREE;
    }

    @Override
    public void initGuiData() {
        switching = false;
        //Setup the features
        features.add(new FeatureDrawable(new ArrayList(criteria.getTriggers()), 45, 201, 201, 64, 119, FeatureNewTrigger.INSTANCE, theme.triggerGradient1, theme.triggerGradient2, theme.triggerFontColor));
        features.add(new FeatureDrawable(new ArrayList(criteria.getRewards()), 140, 0, 55, 201, 201, FeatureNewReward.INSTANCE, theme.rewardBoxGradient1, theme.rewardBoxGradient2, theme.rewardBoxFont));
        features.add(new FeatureBarsX2(this, "requirements", "results"));
        features.add(FeatureItemSelector.INSTANCE); //Add the item selector
        features.add(FeatureNewTrigger.INSTANCE); //Add new trigger popup
        features.add(FeatureNewReward.INSTANCE); //Add new reward popup

        //Create Fields
        fields.clear(); //Reset the fields
        fields.put("name", new TextField("displayName", criteria));

        if (MCClientHelper.isInEditMode()) {
            fields.put("popup", new BooleanField("achievement", criteria));
            fields.put("allRewards", new BooleanFieldHideable("allRewards", criteria));
            fields.put("allTasks", new BooleanFieldHideable("allTasks", criteria));
            fields.put("infinite", new BooleanFieldHideable("infinite", criteria));
            fields.put("rewardsGiven", new TextFieldHideable((BooleanFieldHideable) fields.get("allRewards"), "rewardsGiven", criteria));
            fields.put("tasksRequired", new TextFieldHideable((BooleanFieldHideable) fields.get("allTasks"), "tasksRequired", criteria));
            fields.put("repeatAmount", new TextFieldHideable((BooleanFieldHideable) fields.get("infinite"), "isRepeatable", criteria));
        }
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {
        drawStack(criteria.getIcon(), 1, 4, 1F);
        String unformatted = fields.get("name").getField();
        String displayName = MCClientHelper.isInEditMode() ? Progression.translate("name.display") + ": " + unformatted : unformatted;
        drawText(displayName, 21, 9, theme.criteriaEditDisplayNameColor);

        if (MCClientHelper.isInEditMode()) {
            drawText(Progression.translate("popup") + ": " + fields.get("popup").getField(), screenWidth - 170, 9, theme.criteriaEditDisplayNameColor);
            String repeatAmount = fields.get("repeatAmount").getField();
            drawText(Progression.translate("repeat") + ": " + (repeatAmount.equals("") ? fields.get("infinite").getField() : repeatAmount + "x"), screenWidth - 90, 9, theme.criteriaEditDisplayNameColor);
            int rightAdjustedMouse = screenWidth - mouseX;
            if (mouseY >= 8 && mouseY <= 18) {
                if (rightAdjustedMouse >= 100 && rightAdjustedMouse <= 170) {
                    FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Popup Displayed");
                    FeatureTooltip.INSTANCE.addTooltip("  Whether an achievement style");
                    FeatureTooltip.INSTANCE.addTooltip("  popup will appear for this");
                    FeatureTooltip.INSTANCE.addTooltip("  criteria or not.");
                } else if (rightAdjustedMouse >= 10 && rightAdjustedMouse <= 90) {
                    FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Repeatability");
                    FeatureTooltip.INSTANCE.addTooltip("  How many times this criteria");
                    FeatureTooltip.INSTANCE.addTooltip("  can be completed to obtain");
                    FeatureTooltip.INSTANCE.addTooltip("  the rewards given.");
                    FeatureTooltip.INSTANCE.addTooltip("  ");
                    if (fields.get("infinite").getField().equals("")) FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Right click for infinite");
                    else FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Right click to edit numbers");
                }
                
                //Edit Name
                if (mouseX <= 170 && mouseX > 20) {
                    FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Criteria Name");
                    FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Click to Edit");
                }
            }
            
            //Edit Icon
            if (mouseX > 0 && mouseX < 18 && mouseY >= 4 && mouseY <= 20) {
                FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Criteria Icon");
                FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Click to Edit");
            }

            drawText("     |       " + Progression.translate("required") + ": " + fields.get("tasksRequired").getField() + fields.get("allTasks").getField(), 100, 29, theme.criteriaEditDisplayNameColor);
            drawText("     |       " + Progression.translate("required") + ": " + fields.get("rewardsGiven").getField() + fields.get("allRewards").getField(), 100, 124, theme.criteriaEditDisplayNameColor);
            if (mouseX >= 140 && mouseX <= 240) {
                if (mouseY >= 26 && mouseY <= 36) {
                    FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Tasks Required");
                    FeatureTooltip.INSTANCE.addTooltip("  Number of tasks required to");
                    FeatureTooltip.INSTANCE.addTooltip("  complete this criteria.");
                    FeatureTooltip.INSTANCE.addTooltip("  ");
                    if (fields.get("allTasks").getField().equals("")) FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Right click to make all required");
                    else FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Right click to edit amount");
                } else if (mouseY >= 123 && mouseY <= 133) {
                    FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Rewards Given");
                    FeatureTooltip.INSTANCE.addTooltip("  Number of rewards to give to");
                    FeatureTooltip.INSTANCE.addTooltip("  players on completion of tasks.");
                    FeatureTooltip.INSTANCE.addTooltip("  ");
                    if (fields.get("allRewards").getField().equals("")) FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Right click to give all rewards");
                    else FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.ITALIC + "  Right click to edit amount");
                }
            }
        }
    }

    @Override
    public boolean guiMouseClicked(int mouseX, int mouseY, int button) {
        if (MCClientHelper.isInEditMode()) {
            if (mouseY >= 8 && mouseY <= 18) {
                int rightAdjustedMouse = screenWidth - mouseX;
                if (rightAdjustedMouse >= 100 && rightAdjustedMouse <= 170) {
                    fields.get("popup").click();
                    return true;
                } else if (rightAdjustedMouse >= 10 && rightAdjustedMouse <= 90) {
                    if (button == 1) {
                        fields.get("infinite").click();
                        TextEditor.INSTANCE.clearEditable();
                    } else fields.get("repeatAmount").click();
                    return true;
                }
                
                if (mouseX <= 170 && mouseX > 20) {
                    fields.get("name").click();
                    return true;
                } else if (mouseX > 0 && mouseX < 18 && mouseY >= 4 && mouseY <= 20) {
                    FeatureItemSelector.INSTANCE.select(ItemFilter.INSTANCE, this, Type.REWARD);
                    return true;
                }
            }

            if (mouseX >= 140 && mouseX <= 240) {
                if (mouseY >= 26 && mouseY <= 36) {
                    if (button == 1) {
                        fields.get("allTasks").click();
                        TextEditor.INSTANCE.clearEditable();
                    } else fields.get("tasksRequired").click();
                    return true;
                } else if (mouseY >= 123 && mouseY <= 133) {
                    if (button == 1) {
                        fields.get("allRewards").click();
                        TextEditor.INSTANCE.clearEditable();
                    } else fields.get("rewardsGiven").click();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void setObject(Object object) {
        criteria.setIcon((ItemStack)object);
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
