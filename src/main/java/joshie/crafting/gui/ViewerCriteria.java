package joshie.crafting.gui;

import joshie.crafting.Criteria;
import joshie.crafting.api.ICriteriaViewer;

public class ViewerCriteria implements ICriteriaViewer {
    private Criteria criteria;
    
    public ViewerCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void draw(int x, int y) {
        // TODO Auto-generated method stub
        
    }
}
