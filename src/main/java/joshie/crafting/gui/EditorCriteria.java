package joshie.crafting.gui;

import java.util.Iterator;
import java.util.List;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ICriteriaEditor;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class EditorCriteria extends TextEditable implements ICriteriaEditor {
    private static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
    private final ICriteria criteria;
    private ITextField selectedText;
    private int xCoord;
    private int yCoord;

    private NameEdit editName;
    private RepeatEdit editRepeat;

    public EditorCriteria(ICriteria criteria) {
        this.criteria = criteria;
        this.editName = new NameEdit(this);
        this.editRepeat = new RepeatEdit(this);
    }

    @Override
    public void drawSplitText(String text, int x, int y, int color, int width) {
        GuiCriteriaEditor.INSTANCE.mc.fontRenderer.drawSplitString(text, xCoord + x, yCoord + y, width, color);
    }

    @Override
    public void drawText(String text, int x, int y, int color) {
        GuiCriteriaEditor.INSTANCE.mc.fontRenderer.drawString(text, xCoord + x, yCoord + y, color);
    }

    @Override
    public void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.drawRectWithBorder(xCoord + x, yCoord + y, xCoord + x + width, yCoord + y + height, color, border);
    }

    @Override
    public void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
        GuiCriteriaEditor.INSTANCE.drawGradientRectWithBorder(xCoord + x, yCoord + y, xCoord + x + width, yCoord + y + height, color, color2, border);
    }

    @Override
    public void drawStack(ItemStack stack, int x, int y, float scale) {
        StackHelper.drawStack(stack, xCoord + x, yCoord + y, scale);
    }

    @Override
    public void drawTexture(int x, int y, int u, int v, int width, int height) {
        GuiCriteriaEditor.INSTANCE.drawTexturedModalRect(xCoord + x, yCoord + y, u, v, width, height);
    }

    private int offsetX = 0;

    @Override
    public void draw(int x, int y, int offsetX) {
        this.offsetX = offsetX;
        this.xCoord = x + offsetX;
        this.yCoord = y;

        ScaledResolution res = GuiCriteriaEditor.INSTANCE.res;
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        //Title and Repeatability Box
        drawText("Unique Name: " + SelectTextEdit.INSTANCE.getText(editName), 9 - offsetX, 9, 0xFFFFFFFF);
        drawText("Repeatability: " + SelectTextEdit.INSTANCE.getText(editRepeat) + "x", fullWidth - 130, 9, 0xFFFFFFFF);
        drawBox(-1, 210, fullWidth, 1, 0xFFFFFFFF, 0xFFFFFFFF);
        drawText("Use arrow keys to scroll sideways, or use the scroll wheel. (Down to go right)", 9 - offsetX, 215, 0xFFFFFFFF);
        drawText("Hold shift with arrow keys to scroll faster.", 9 - offsetX, 225, 0xFFFFFFFF);

        //Triggers
        drawGradient(-1, 25, fullWidth, 15, 0xFF0080FF, 0xFF00468C, 0xFF00468C);
        drawBox(-1, 40, fullWidth, 1, 0xFF003366, 0x00000000);
        drawText("Requirements", 9 - offsetX, 29, 0xFFFFFFFF);
        int xCoord = 0;
        List<ITrigger> triggers = criteria.getTriggers();
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
        List<IReward> rewards = criteria.getRewards();
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
    }

    private void remove(List list, Object object) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o.equals(object)) {
                it.remove();
                break;
            }
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, boolean isDoubleClick) {
        boolean hasClicked = false;
        //Name and repeat
        ScaledResolution res = GuiCriteriaEditor.INSTANCE.res;
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        if (ClientHelper.canEdit()) {
            if (mouseY >= 6 && mouseY <= 19) {
                if (mouseX >= 0 && mouseX <= 200) {
                    SelectTextEdit.INSTANCE.select(editName);
                    hasClicked = true;
                }

                if (mouseX <= res.getScaledWidth() && mouseX >= res.getScaledWidth() - 250) {
                    SelectTextEdit.INSTANCE.select(editRepeat);
                    hasClicked = true;
                }
            }
        }

        //Triggers
        xCoord = 0;
        List<ITrigger> triggers = criteria.getTriggers();
        for (int i = 0; i < triggers.size(); i++) {
            Result result = triggers.get(i).onClicked();
            if (result != Result.DEFAULT) {
                hasClicked = true;
            }

            if (result == Result.DENY) {
                remove(triggers, triggers.get(i));
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
                    NewTrigger.INSTANCE.select(criteria);
                    hasClicked = true;
                }
            }

            //Rewards
            List<IReward> rewards = criteria.getRewards();
            int xCoord = 0;
            for (int i = 0; i < rewards.size(); i++) {
                Result result = rewards.get(i).onClicked();
                if (result != Result.DEFAULT) {
                    hasClicked = true;
                }

                if (result == Result.DENY) {
                    remove(rewards, rewards.get(i));
                    break;
                }

                xCoord++;
            }

            if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
                if (mouseY >= 144 && mouseY <= 144 + 55) {
                    NewReward.INSTANCE.select(criteria);
                    hasClicked = true;
                }
            }
        }

        return hasClicked;
    }

    private static interface ITextField {
        public String getText();

        public void setText(String str);
    }

    @Override
    public String getTextField() {
        if (selectedText == null) return "";
        return selectedText.getText();
    }

    @Override
    public void setTextField(String str) {
        if (selectedText != null) selectedText.setText(str);
    }

    private class NameEdit implements ITextEditable {
        private EditorCriteria editor;

        public NameEdit(EditorCriteria editor) {
            this.editor = editor;
        }

        @Override
        public String getTextField() {
            return editor.criteria.getDisplayName();
        }

        @Override
        public void setTextField(String str) {
            editor.criteria.setDisplayName(str);
        }
    }

    private class RepeatEdit implements ITextEditable {
        private EditorCriteria editor;
        String textField;

        public RepeatEdit(EditorCriteria editor) {
            this.editor = editor;
        }

        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + editor.criteria.getRepeatAmount();
            }

            return textField;
        }

        @Override
        public void setTextField(String text) {
            String fixed = text.replaceAll("[^0-9]", "");
            this.textField = fixed;

            try {
                editor.criteria.setRepeatAmount(Integer.parseInt(textField));
            } catch (Exception e) {
                this.textField = "1";
                editor.criteria.setRepeatAmount(1);
            }
        }
    }
}
