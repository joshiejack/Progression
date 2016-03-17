package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IFilter.FilterType;

public interface IFilterSelectorFilter {
    /** Returns the filter type **/
    public FilterType getType();
    
    /** Whether this object is acceptable **/
    public boolean isAcceptable(Object object);

    public List getAllItems();

    public boolean searchMatches(Object object, String lowerCase);

    public void draw(DrawFeatureHelper helper, Object stack, int offsetX, int j, int yOffset, int k, int mouseX, int mouseY);

    public boolean mouseClicked(int mouseX, int mouseY, int j, int k);

    public double getScale();
}
