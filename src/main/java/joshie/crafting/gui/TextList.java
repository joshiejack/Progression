package joshie.crafting.gui;

import joshie.crafting.api.DrawHelper;

public abstract class TextList {
    public String name;
    
    public TextList(String name) {
        this.name = name;
    }
    
    public abstract void click();
    public abstract void draw(int color, int yPos);
    
    public static class TextSelector extends TextList {
        protected TextFieldHelper data;

        public TextSelector(String name, TextFieldHelper data) {
            super(name);
            this.data = data;
        }

        @Override
        public void click() {
            data.select();
        }
        
        @Override
        public void draw(int color, int yPos) {
            DrawHelper.drawText(name + ": " + data, 4, yPos, color);
        }
    }
    
    public static class SplitTextSelector extends TextSelector {
        public SplitTextSelector(String name, TextFieldHelper data) {
            super(name, data);
        }
        
        @Override
        public void draw(int color, int yPos) {
            DrawHelper.drawSplitText(name + ": " + data, 4, yPos, 105, color);
        }
    }
}
