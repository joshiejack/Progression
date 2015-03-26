package joshie.crafting.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public abstract class TextEditable {
    public boolean isSelected;
    protected int position;
    private int tick;
    private boolean white;

    public void setSelected() {
        position = getTextField().length();
    }

    public abstract String getTextField();

    public abstract void setTextField(String str);

    public String getText(String field) {
        if (isSelected) {
            tick++;
            if (tick % 60 == 0) {
                if (white) {
                    white = false;
                } else {
                    white = true;
                }
            }

            if (white) {
                return new StringBuilder(field).insert(Math.min(position, field.length()), "_").toString();
            } else {
                return new StringBuilder(field).insert(Math.min(position, field.length()), "_").toString();
            }
        } else return getTextField();
    }

    public String getText() {
        return getText(getTextField());
    }

    private void cursorLeft(int count) {
        int left = position - count;
        if (left < 0) {
            position = 0;
        } else position = left;
    }

    private void cursorRight(int count) {
        String text = getTextField();
        int right = position + count;
        if (right > text.length()) {
            position = text.length();
        } else position = right;
    }

    private void add(String string) {
        String text = getTextField();
        StringBuilder builder = new StringBuilder(text);
        text = builder.insert(position, string).toString();
        setTextField(text);
        cursorRight(string.length());
    }

    private void delete(int count) {
        String text = getTextField();
        if ((count < 0 && position > 0) || (count >= 0 && position < text.length())) {
            StringBuilder builder = new StringBuilder(text);
            text = builder.deleteCharAt(position + count).toString();
            setTextField(text);
            if (count < 0) cursorLeft(-count);
            else if (count >= 0) cursorRight(count);
        }
    }

    public void keyTyped(char character, int key) {
        if (getTextField() != null && isSelected) {
            if (key == 203) {
                cursorLeft(1);
            } else if (key == 205) {
                cursorRight(1);
            } else if (character == 22) {
                add(GuiScreen.getClipboardString());
            } else if (key == 14) {
                delete(-1);
            } else if (key == 211) {
                delete(0);
            } else if (key == 28 || key == 156) {
                add("\n");
            } else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                add(Character.toString(character));
            }
        }
    }
}
