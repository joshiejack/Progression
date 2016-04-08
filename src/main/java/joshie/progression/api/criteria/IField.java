package joshie.progression.api.criteria;

import joshie.progression.gui.core.DrawHelper;

public interface IField<T> {
    /** Returns the name of the field **/
    public String getFieldName();

    /** Called first when attempting to click **/
    public boolean attemptClick(int mouseX, int mouseY);

    /** Performs a click of attempt click fails and it was clicked in a specific range **/
    public boolean click(int button);

    /** Draw this field **/
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY);

    /** Return contents of the field **/
    public T getField();
}
