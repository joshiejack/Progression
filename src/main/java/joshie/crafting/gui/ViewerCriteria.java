package joshie.crafting.gui;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ICriteriaViewer;

public class ViewerCriteria implements ICriteriaViewer {
    private ICriteria criteria;
    
    public ViewerCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void draw(int x, int y) {
        // TODO Auto-generated method stub
        
    }
}
