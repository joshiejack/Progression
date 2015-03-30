package joshie.crafting.gui;

import java.util.Iterator;
import java.util.List;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ICriteriaEditor;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class EditorCriteria extends TextEditable implements ICriteriaEditor {
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
    public void drawText(String text, int x, int y, int color) {
        GuiTreeEditorEdit.INSTANCE.mc.fontRenderer.drawString(text, xCoord + x, yCoord + y, color);
    }

    @Override
    public void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(xCoord + x, yCoord + y, xCoord + x + width, yCoord + y + height, color, border);
    }

    @Override
    public void drawStack(ItemStack stack, int x, int y, float scale) {
        StackHelper.drawStack(stack, xCoord + x, yCoord + y, scale);
    }

    private static int triggerStart = 0;
    private static int rewardStart = 0;
    private int offsetX = 0;

    @Override
    public void draw(int x, int y, int offsetX) {
        this.offsetX = offsetX;
        this.xCoord = x + offsetX;
        this.yCoord = y;

        ScaledResolution res = new ScaledResolution(GuiTreeEditorEdit.INSTANCE.mc, GuiTreeEditorEdit.INSTANCE.mc.displayWidth, GuiTreeEditorEdit.INSTANCE.mc.displayHeight);
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        //Title and Repeatability Box
        String title = criteria.getUniqueName();
        String repeatibility = criteria.getRepeatAmount() + "x";
        drawBox(-1, 5, fullWidth, 15, 0xFFCCCCCC, 0xFF000000);
        drawText("Unique Name: " + SelectTextEdit.INSTANCE.getText(editName), 9 - offsetX, 9, 0xFF000000);
        drawText("Repeatability: " + SelectTextEdit.INSTANCE.getText(editRepeat), fullWidth - 130, 9, 0xFF000000);

        //Triggers
        drawBox(-1, 25, fullWidth, 15, 0xFF0080FF, 0xFF00468C);
        drawText("Triggers", 9 - offsetX, 29, 0xFFFFFFFF);
        drawBox(-1, 39, fullWidth, 75, 0xFFFFFFFF, 0xFF000000);
        int xCoord = 0;
        List<ITrigger> triggers = criteria.getTriggers();
        int size = triggerStart + 7;
        if (size > triggers.size()) {
            size = triggers.size();
        }

        int mouseX = GuiCriteriaEditor.INSTANCE.mouseX - offsetX;
        int mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
        for (int i = triggerStart; i < size; i++) {
            ITrigger trigger = triggers.get(i);
            int xPos = 100 * xCoord;
            trigger.draw(mouseX, mouseY, xPos);
            xCoord++;
        }

        int color = 0xFF0080FF;
        if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
            if (mouseY >= 49 && mouseY <= 49 + 55) {
                color = 0xFF4DA6FF;
            }
        }

        drawBox(15 + 100 * xCoord, 69, 55, 15, color, 0xFF00468C);
        drawBox(35 + 100 * xCoord, 49, 15, 55, color, 0xFF00468C);
        drawBox(30 + 100 * xCoord, 70, 25, 13, color, color);
        //Rewards
        drawBox(-1, 120, fullWidth, 15, 0xFFB20000, 0xFF660000);
        drawText("Rewards", 9 - offsetX, 124, 0xFFFFFFFF);
        drawBox(-1, 134, fullWidth, 75, 0xFFFFFFFF, 0xFF000000);
        xCoord = 0;
        List<IReward> rewards = criteria.getRewards();
        size = rewardStart + 7;
        if (size > rewards.size()) {
            size = rewards.size();
        }

        for (int i = rewardStart; i < size; i++) {
            int xPos = 100 * xCoord;
            IReward reward = rewards.get(i);
            reward.draw(mouseX, mouseY, xPos);
            xCoord++;
        }

        color = 0xFFB20000;
        if (mouseX >= 15 + 100 * xCoord && mouseX <= 15 + 100 * xCoord + 55) {
            if (mouseY >= 144 && mouseY <= 144 + 55) {
                color = 0xFFFF4D4D;
            }
        }

        drawBox(15 + 100 * xCoord, 164, 55, 15, color, 0xFF660000);
        drawBox(35 + 100 * xCoord, 144, 15, 55, color, 0xFF660000);
        drawBox(30 + 100 * xCoord, 165, 25, 13, color, color);
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
        ScaledResolution res = new ScaledResolution(GuiTreeEditorEdit.INSTANCE.mc, GuiTreeEditorEdit.INSTANCE.mc.displayWidth, GuiTreeEditorEdit.INSTANCE.mc.displayHeight);
        int fullWidth = (res.getScaledWidth()) - offsetX + 5;
        //Title and Repeatability Box
        String title = criteria.getUniqueName();
        String repeatibility = criteria.getRepeatAmount() + "x";
        drawBox(-1, 5, fullWidth, 15, 0xFFCCCCCC, 0xFF000000);
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

        //Triggers
        xCoord = 0;
        List<ITrigger> triggers = criteria.getTriggers();
        int size = triggerStart + 7;
        if (size > triggers.size()) {
            size = triggers.size();
        }

        for (int i = triggerStart; i < size; i++) {
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
        size = rewardStart + 7;
        if (size > rewards.size()) {
            size = rewards.size();
        }

        int xCoord = 0;
        for (int i = rewardStart; i < size; i++) {
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

        return hasClicked;
    }

    @Override
    public boolean keyTyped(char character, int key) {
        return super.keyTyped(character, key);
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
            return editor.criteria.getUniqueName();
        }

        @Override
        public void setTextField(String str) {
            editor.criteria.setUniqueName(str);
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

            return textField + "x";
        }

        @Override
        public void setTextField(String text) {
            String fixed = text.replaceAll("[^0-9]", "");
            this.textField = fixed;

            try {
                editor.criteria.setRepeatAmount(Integer.parseInt(textField));
            } catch (Exception e) {
                editor.criteria.setRepeatAmount(1);
            }
        }
    }
}
