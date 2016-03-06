package joshie.progression.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import joshie.progression.Progression;
import joshie.progression.criteria.Reward;
import joshie.progression.criteria.Trigger;
import joshie.progression.gui.base.GuiOffset;
import joshie.progression.gui.base.IRenderOverlay;
import joshie.progression.gui.editors.EditText;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.editors.SelectItem;
import joshie.progression.gui.editors.SelectItem.Type;
import joshie.progression.gui.fields.FieldHelper;
import joshie.progression.gui.fields.FieldHelper.IntegerFieldHelper;
import joshie.progression.handlers.EventsManager;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.ListHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class GuiCriteriaEditor extends GuiOffset implements IItemSelectable {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();
    private static final NameEdit nameEdit = new NameEdit();
    private static final RepeatEdit repeatEdit = new RepeatEdit();

    public static void registerOverlay(IRenderOverlay overlay) {
        INSTANCE.overlays.add(overlay);
    }

    @Override
    public void drawForeground() {
        ScaledResolution res = GuiCriteriaEditor.INSTANCE.res;
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        //Title and Repeatability Box
        drawStack(selected.stack, 1, 4, 1F);
        drawText("Display Name: " + nameEdit.getText(), 21 - offsetX, 9, theme.criteriaEditDisplayNameColor);
        drawText("Popup: " + INSTANCE.selected.achievement, fullWidth - 210, 9, theme.criteriaEditDisplayNameColor);
        drawText("Repeatability: " + repeatEdit.getText() + "x", fullWidth - 130, 9, theme.criteriaEditDisplayNameColor);
        drawBox(-1, 215, fullWidth, 1, theme.blackBarUnderLineBorder, theme.blackBarUnderLineBorder);
        drawText("Use arrow keys to scroll sideways, or use the scroll wheel. (Down to go right)", 9 - offsetX, 218, theme.scrollTextFontColor);
        drawText("Hold shift with arrow keys to scroll faster.", 9 - offsetX, 228, theme.scrollTextFontColor);

        //Triggers
        drawGradient(-1, 25, fullWidth, 15, theme.triggerBoxGradient1, theme.triggerBoxGradient2, theme.triggerBoxBorder);
        drawBox(-1, 40, fullWidth, 1, theme.triggerBoxUnderline1, theme.invisible);
        drawText("Requirements", 9 - offsetX, 29, theme.triggerBoxFont);
        int xCoord = 0;
        List<Trigger> triggers = selected.triggers;
        int mouseX = GuiCriteriaEditor.INSTANCE.mouseX - offsetX;
        int mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
        for (int i = 0; i < triggers.size(); i++) {
            Trigger trigger = triggers.get(i);
            int xPos = 100 * xCoord;
            trigger.draw(mouseX, mouseY, xPos);
            xCoord++;
        }

        if (ClientHelper.canEdit()) {
            int crossY = 64;
            if (!NewTrigger.INSTANCE.isVisible() && !NewReward.INSTANCE.isVisible()) {
                if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                    if (mouseY >= 49 && mouseY <= 49 + 55) {
                        crossY = 119;
                    }
                }
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            ClientHelper.getMinecraft().getTextureManager().bindTexture(ProgressionInfo.textures);
            drawTexture(15 + 100 * xCoord, 49, 201, crossY, 55, 55);
        }

        //Rewards
        drawGradient(-1, 120, fullWidth, 15, theme.rewardBoxGradient1, theme.rewardBoxGradient2, theme.rewardBoxBorder);
        drawText("Result", 9 - offsetX, 124, theme.rewardBoxFont);
        xCoord = 0;
        List<Reward> rewards = selected.rewards;
        for (int i = 0; i < rewards.size(); i++) {
            int xPos = 100 * xCoord;
            Reward reward = rewards.get(i);
            reward.draw(mouseX, mouseY, xPos);
            xCoord++;
        }

        if (ClientHelper.canEdit()) {
            int crossX = 0;
            if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                if (mouseY >= 144 && mouseY <= 144 + 55) {
                    crossX = 55;
                }
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            ClientHelper.getMinecraft().getTextureManager().bindTexture(ProgressionInfo.textures);
            drawTexture(15 + 100 * xCoord, 144, crossX, 201, 55, 55);
        }

        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.draw(0, y);
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);

        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.keyTyped(character, key);
            }
        }
    }

    private static long lastClick;

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = difference <= 150;
        lastClick = System.currentTimeMillis();

        boolean clicked = false;
        int visible = 0;
        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                if (overlay.mouseClicked(mouseX, mouseY, button)) {
                    clicked = true;
                    break;
                }

                visible++;
            }
        }

        if (!clicked) {
            if (!onCriteriaClicked(isDoubleClick)) {
                EditText.INSTANCE.reset();
            }
        }

        //If we are trying to go back
        if (visible <= 1 && !clicked) {
            if (button == 1) {
                GuiTreeEditor.INSTANCE.currentTab = GuiCriteriaEditor.INSTANCE.selected.tab;
                GuiTreeEditor.INSTANCE.currentTabName = GuiTreeEditor.INSTANCE.currentTab.getUniqueName();
                EditText.INSTANCE.reset();
                GuiTreeEditor.INSTANCE.selected = null;
                GuiTreeEditor.INSTANCE.previous = null;
                GuiTreeEditor.INSTANCE.lastClicked = null;
                ClientHelper.getPlayer().openGui(Progression.instance, 0, null, 0, 0, 0);
            }
        }

        super.mouseClicked(x, y, button);
    }

    private boolean onCriteriaClicked(boolean isDoubleClick) {
        boolean hasClicked = false;
        //Name and repeat
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        if (ClientHelper.canEdit()) {            
            if (mouseY >= 4 && mouseY <= 19) {
                if (mouseX <= 15) {
                    SelectItem.INSTANCE.select(this, Type.REWARD);
                    hasClicked = true;
                }
                
                if (mouseX >= 16 && mouseX <= 200) {
                    nameEdit.select();
                    hasClicked = true;
                }
                               
                if (mouseX >= res.getScaledWidth() - 205 && mouseX <= res.getScaledWidth() - 140) {
                    INSTANCE.selected.achievement = !INSTANCE.selected.achievement;
                    hasClicked = true;
                }

                if (mouseX <= res.getScaledWidth() && mouseX >= res.getScaledWidth() - 135) {
                    repeatEdit.select();
                    hasClicked = true;
                }
            }
        }

        //Triggers
        int xCoord = 0;
        List<Trigger> triggers = selected.triggers;
        for (int i = 0; i < triggers.size(); i++) {
            Result result = triggers.get(i).onClicked();
            if (result != Result.DEFAULT) {
                hasClicked = true;
            }

            if (result == Result.DENY) {
                Trigger trigger = triggers.get(i);
                EventsManager.onTriggerRemoved(trigger);
                ListHelper.remove(triggers, trigger);
                break;
            }

            xCoord++;
        }

        if (ClientHelper.canEdit()) {
            mouseX = GuiCriteriaEditor.INSTANCE.mouseX - offsetX;
            mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
            if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                if (mouseY >= 49 && mouseY <= 49 + 55) {
                    NewTrigger.INSTANCE.select(selected);
                    hasClicked = true;
                }
            }

            //Rewards
            List<Reward> rewards = selected.rewards;
            xCoord = 0;
            for (int i = 0; i < rewards.size(); i++) {
                Result result = rewards.get(i).onClicked();
                if (result != Result.DEFAULT) {
                    hasClicked = true;
                }

                if (result == Result.DENY) {
                    Reward reward = rewards.get(i);
                    EventsManager.onRewardRemoved(reward);
                    reward.getType().onRemoved();
                    ListHelper.remove(rewards, reward);
                    break;
                }

                xCoord++;
            }

            if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                if (mouseY >= 144 && mouseY <= 144 + 55) {
                    NewReward.INSTANCE.select(selected);
                    hasClicked = true;
                }
            }
        }

        return hasClicked;
    }

    private static class NameEdit extends FieldHelper {
        @Override
        public String getTextField() {
            return INSTANCE.selected.displayName;
        }

        @Override
        public void setTextField(String name) {
            INSTANCE.selected.displayName = name;
        }
    }

    private static class RepeatEdit extends IntegerFieldHelper {
        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + INSTANCE.selected.isRepeatable;
            }

            return "" + INSTANCE.selected.isRepeatable;
        }

        @Override
        public void setNumber(int amount) {
            INSTANCE.selected.isRepeatable = amount;
        }
    }

    @Override
    public void setItemStack(ItemStack stack) {
        selected.stack = stack;
    }
}
