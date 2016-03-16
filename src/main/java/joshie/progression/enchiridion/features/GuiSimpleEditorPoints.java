package joshie.progression.enchiridion.features;

import java.lang.reflect.Field;
import java.util.HashMap;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.gui.book.GuiSimpleEditorAbstract;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.util.PenguinFont;
import joshie.enchiridion.util.TextEditor;
import joshie.progression.Progression;

public class GuiSimpleEditorPoints extends GuiSimpleEditorAbstract {
    public static final GuiSimpleEditorPoints INSTANCE = new GuiSimpleEditorPoints();
    private HashMap<String, ITextEditable> fieldCache = new HashMap();
    private static FeaturePoints points = null;

    protected GuiSimpleEditorPoints() {}

    public IBookEditorOverlay setPoints(FeaturePoints points) {
        this.points = points;
        this.fieldCache = new HashMap();
        return this;
    }

    private boolean isOverPosition(int x1, int y1, int x2, int y2, int mouseX, int mouseY) {
        if (mouseX >= EConfig.editorXPos + x1 && mouseX <= EConfig.editorXPos + x2) {
            if (mouseY >= EConfig.toolbarYPos + y1 && mouseY <= EConfig.toolbarYPos + y2) {
                return true;
            }
        }

        return false;
    }

    private static final int xPosStart = 4;

    private void drawBoxLabel(String name, int yPos) {
        drawBorderedRectangle(xPosStart - 2, yPos, 83, yPos + 10, 0xFFB0A483, 0xFF48453C);
        drawSplitScaledString("[b]" + name + "[/b]", xPosStart, yPos + 3, 0xFF48453C, 0.5F);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int xPos = xPosStart;
        int yPos = -11;

        //Draw the extra information for the actions
        drawBoxLabel("Extra Fields", yPos + 20);
        String[] fields = new String[] { "description", "variable", "wholeNumber" };
        for (String f : fields) {
            drawBorderedRectangle(2, yPos + 30, 83, yPos + 37, 0xFF312921, 0xFF191511);
            String name = Progression.translate("button.action.field." + f);
            drawSplitScaledString("[b]" + name + "[/b]", 4, yPos + 32, 0xFFFFFFFF, 0.5F);

            ITextEditable editable = null;
            if (!fieldCache.containsKey(f)) {
                editable = new WrappedEditable(f);
                fieldCache.put(f, editable);
            } else editable = fieldCache.get(f);

            String text = TextEditor.INSTANCE.getText(editable);
            if (text == null) {
                TextEditor.INSTANCE.setText("");
                text = TextEditor.INSTANCE.getText(editable);
            }

            int lines = getLineCount(text) - 1;
            drawSplitScaledString(text, 4, yPos + 39, 0xFF191511, 0.5F);
            yPos = yPos + 6 + lines;
        }

        drawBorderedRectangle(2, yPos + 30, 83, yPos + 31, 0xFF312921, 0xFF191511);
    }

    public int getLineCount(String text) {
        text = PenguinFont.INSTANCE.replaceFormatting(text);
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }

        return PenguinFont.INSTANCE.splitStringWidth(text, 155);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        int xPos = xPosStart;
        int yPos = -11;
        //Draw the extra information for the actions
        drawBoxLabel("Extra Fields", yPos + 20);
        String[] fields = new String[] { "description", "variable", "wholeNumber" };
        for (String f : fields) {
            ITextEditable editable = null;
            if (!fieldCache.containsKey(f)) {
                editable = new WrappedEditable(f);
                fieldCache.put(f, editable);
            } else editable = fieldCache.get(f);
            String text = TextEditor.INSTANCE.getText(editable);
            int lines = getLineCount(text) - 1;

            if (isOverPosition(2, yPos + 37, 83, yPos + 44 + (5 * lines), mouseX, mouseY)) {
                TextEditor.INSTANCE.setEditable(editable);
                return true;
            }

            yPos = yPos + 15 + (5 * lines);
        }

        return false;
    }

    //Helper Editable
    private static class WrappedEditable implements ITextEditable {
        private String temporaryField;
        private String fieldName;

        public WrappedEditable(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public String getTextField() {
            if (temporaryField == null) {
                try {
                    Field f = points.getClass().getField(fieldName);
                    if (fieldName.equals("pageNumber")) {
                        temporaryField = "" + (f.getInt(points) + 1);
                    } else temporaryField = "" + f.get(points);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Fix it up
            if (temporaryField == null) temporaryField = "";
            return temporaryField;
        }

        @Override
        public void setTextField(String text) {
            temporaryField = text;

            try {
                Field f = points.getClass().getField(fieldName);
                if (f.getType() == int.class) {
                    try {
                        Integer number = Integer.parseInt(temporaryField);
                        if (fieldName.equals("pageNumber")) {
                            number -= 1;
                        }

                        f.set(points, number);
                    } catch (Exception e) {
                        f.set(points, 0);
                    }
                } else f.set(points, text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
