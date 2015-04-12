package joshie.crafting.gui;

import joshie.crafting.json.Theme;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public abstract class TextEditable extends OverlayBase {
    protected static int position;
    protected static Theme theme = Theme.INSTANCE;

    public void setSelected() {
        position = getTextField().length();
    }

    public abstract String getTextField();

    public abstract void setTextField(String str);

    public String getText(String field) {
        return new StringBuilder(field).insert(Math.min(position, field.length()), "_").toString();
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
        if (text == null) return;
        int right = position + count;
        if (right > text.length()) {
            position = text.length();
        } else position = right;
    }

    private void add(String string) {
        String text = getTextField();
        if (text == null) return;
        StringBuilder builder = new StringBuilder(text);
        text = builder.insert(position, string).toString();
        setTextField(text);
        cursorRight(string.length());
    }

    private void delete(int count) {
        String text = getTextField(); 
        if (text == null) return;
        if ((count < 0 && position > 0) || (count >= 0 && position < text.length())) {
            StringBuilder builder = new StringBuilder(text);
            text = builder.deleteCharAt(position + count).toString();
            setTextField(text);
            if (count < 0) cursorLeft(-count);
            else if (count >= 0) cursorRight(count);
        }
    }

    public boolean keyTyped(char character, int key) {
        if (getTextField() != null) {
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

        return false;
    }
}
