package joshie.crafting.gui;

import java.util.List;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.ListHelper;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class GuiCriteriaEditor extends GuiOffset {
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
        drawText("Unique Name: " + nameEdit.getText(), 9 - offsetX, 9, 0xFFFFFFFF);
        drawText("Repeatability: " + repeatEdit.getText() + "x", fullWidth - 130, 9, 0xFFFFFFFF);
        drawBox(-1, 210, fullWidth, 1, 0xFFFFFFFF, 0xFFFFFFFF);
        drawText("Use arrow keys to scroll sideways, or use the scroll wheel. (Down to go right)", 9 - offsetX, 215, 0xFFFFFFFF);
        drawText("Hold shift with arrow keys to scroll faster.", 9 - offsetX, 225, 0xFFFFFFFF);

        //Triggers
        drawGradient(-1, 25, fullWidth, 15, 0xFF0080FF, 0xFF00468C, 0xFF00468C);
        drawBox(-1, 40, fullWidth, 1, 0xFF003366, 0x00000000);
        drawText("Requirements", 9 - offsetX, 29, 0xFFFFFFFF);
        int xCoord = 0;
        List<ITrigger> triggers = selected.getTriggers();
        int mouseX = GuiCriteriaEditor.INSTANCE.mouseX - offsetX;
        int mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
        for (int i = 0; i < triggers.size(); i++) {
            ITrigger trigger = triggers.get(i);
            int xPos = 100 * xCoord;
            trigger.draw(mouseX, mouseY, xPos);
            xCoord++;
        }

        if (ClientHelper.canEdit()) {
            int crossX = 0;
            if (!NewTrigger.INSTANCE.isVisible() && !NewReward.INSTANCE.isVisible()) {
                if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                    if (mouseY >= 49 && mouseY <= 49 + 55) {
                        crossX = 110;
                    }
                }
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            ClientHelper.getMinecraft().getTextureManager().bindTexture(textures);
            drawTexture(15 + 100 * xCoord, 49, crossX, 125, 55, 55);
        }

        //Rewards
        drawGradient(-1, 120, fullWidth, 15, 0xFFB20000, 0xFF660000, 0xFF660000);
        drawText("Result", 9 - offsetX, 124, 0xFFFFFFFF);
        xCoord = 0;
        List<IReward> rewards = selected.getRewards();
        for (int i = 0; i < rewards.size(); i++) {
            int xPos = 100 * xCoord;
            IReward reward = rewards.get(i);
            reward.draw(mouseX, mouseY, xPos);
            xCoord++;
        }

        if (ClientHelper.canEdit()) {
            int crossX = 55;
            int color2 = 0xFF400000;
            if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                if (mouseY >= 144 && mouseY <= 144 + 55) {
                    crossX = 165;
                }
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            ClientHelper.getMinecraft().getTextureManager().bindTexture(textures);
            drawTexture(15 + 100 * xCoord, 144, crossX, 125, 55, 55);
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
    protected void mouseClicked(int x, int y, int button) {
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
                SelectTextEdit.INSTANCE.reset();
            }
        }

        //If we are trying to go back
        if (visible <= 1 && !clicked) {
            if (button == 1) {
                GuiTreeEditor.INSTANCE.currentTab = GuiCriteriaEditor.INSTANCE.selected.getTabID();
                GuiTreeEditor.INSTANCE.currentTabName = GuiTreeEditor.INSTANCE.currentTab.getUniqueName();
                SelectTextEdit.INSTANCE.reset();
                GuiTreeEditor.INSTANCE.selected = null;
                GuiTreeEditor.INSTANCE.previous = null;
                GuiTreeEditor.INSTANCE.lastClicked = null;
                ClientHelper.getPlayer().openGui(CraftingMod.instance, 0, null, 0, 0, 0);
            }
        }

        super.mouseClicked(x, y, button);
    }

    private boolean onCriteriaClicked(boolean isDoubleClick) {
        boolean hasClicked = false;
        //Name and repeat
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        if (ClientHelper.canEdit()) {
            if (mouseY >= 6 && mouseY <= 19) {
                if (mouseX >= 0 && mouseX <= 200) {
                    nameEdit.select();
                    hasClicked = true;
                }

                if (mouseX <= res.getScaledWidth() && mouseX >= res.getScaledWidth() - 250) {
                    repeatEdit.select();
                    hasClicked = true;
                }
            }
        }

        //Triggers
        int xCoord = 0;
        List<ITrigger> triggers = selected.getTriggers();
        for (int i = 0; i < triggers.size(); i++) {
            Result result = triggers.get(i).onClicked();
            if (result != Result.DEFAULT) {
                hasClicked = true;
            }

            if (result == Result.DENY) {
                ListHelper.remove(triggers, triggers.get(i));
                break;
            }

            xCoord++;
        }

        if (ClientHelper.canEdit()) {
            mouseX = GuiCriteriaEditor.INSTANCE.mouseX - offsetX;
            mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
            int color = 0xFF0080FF;
            if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                if (mouseY >= 49 && mouseY <= 49 + 55) {
                    NewTrigger.INSTANCE.select(selected);
                    hasClicked = true;
                }
            }

            //Rewards
            List<IReward> rewards = selected.getRewards();
            xCoord = 0;
            for (int i = 0; i < rewards.size(); i++) {
                Result result = rewards.get(i).onClicked();
                if (result != Result.DEFAULT) {
                    hasClicked = true;
                }

                if (result == Result.DENY) {
                    ListHelper.remove(rewards, rewards.get(i));
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

    private static class NameEdit extends TextFieldHelper {
        @Override
        public String getTextField() {
            return INSTANCE.selected.getDisplayName();
        }

        @Override
        public void setTextField(String str) {
            INSTANCE.selected.setDisplayName(str);
        }
    }

    private static class RepeatEdit extends IntegerFieldHelper {
        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + INSTANCE.selected.getRepeatAmount();
            }

            return "" + INSTANCE.selected.getRepeatAmount();
        }

        @Override
        public void setNumber(int amount) {
            INSTANCE.selected.setRepeatAmount(amount);
        }
    }
}
