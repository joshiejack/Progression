package joshie.progression.gui.core;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class FeatureLastDraw extends FeatureAbstract {
    private Set<Callable> callables = new LinkedHashSet();

    public FeatureLastDraw() {}
    
    @Override
    public FeatureAbstract init() {
        clear();
        return this;
    }
    
    public void clear() {
        callables.clear();
    }

    public void add(Callable tooltip) {
        this.callables.add(tooltip);
    }

    public void add(List<Callable> list) {
        this.callables.addAll(list);
    }

    public void add(Callable[] split) {
        for (Callable s: split) this.callables.add(s);
    }
    
    @Override
    public void drawFeature(int x, int y) {
        for (Callable c: callables) {
            try {
                c.call();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    public boolean isOverlay() {
        return true;
    }
}