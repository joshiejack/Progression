package joshie.progression.gui.editors;


public class EditText extends TextEditable {
    public static EditText INSTANCE = new EditText();
    private static ITextEditable text;

    public static ITextEditable getEditable() {
        return text;
    }

    public String getText(ITextEditable editable) {
        return text == editable ? getText(editable.getTextField()) : editable.getTextField();
    }

    public void select(ITextEditable text) {
        if (reset()) {
            EditText.text = text;
            position = EditText.text.getTextField().length();
        }
    }

    @Override
    public void clear() {
        EditText.text = null;
    }

    @Override
    public boolean isVisible() {
        return text != null;
    }

    @Override
    public String getTextField() {
        return text != null? text.getTextField(): null;
    }

    @Override
    public void setTextField(String str) {
        if (text != null) {
            text.setTextField(str);
        }
    }

    public static interface ITextEditable {
        public String getTextField();

        public void setTextField(String str);
    }
}
