package joshie.progression.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import joshie.progression.api.IConditionType;
import joshie.progression.api.IItemFilter;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITriggerType;
import joshie.progression.criteria.Condition;
import joshie.progression.criteria.Reward;
import joshie.progression.criteria.Trigger;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.gui.newversion.overlays.IDrawable;
import joshie.progression.handlers.EventsManager;

public class CollectionHelper {    
    public static void remove(Collection collection, Object object) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o.equals(object)) {
                it.remove();
                break;
            }
        }
    }

    public static void removeAndUpdate(List<IDrawable> drawable, IDrawable drawing) {
        if (drawing.getProvider() instanceof IRewardType) {
            EventsManager.onRewardRemoved((Reward) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.criteria.rewards, (Reward) drawing);
            GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing.getProvider() instanceof ITriggerType) {
            EventsManager.onTriggerRemoved((Trigger) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.criteria.triggers, (Trigger) drawing);  
            GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof IItemFilter) {
            GuiItemFilterEditor.INSTANCE.field.remove((IItemFilter) drawing);
            GuiItemFilterEditor.INSTANCE.initGui();
        } else if (drawing.getProvider() instanceof IConditionType) {
            CollectionHelper.remove(GuiConditionEditor.INSTANCE.trigger.getConditions(), (Condition) drawing); 
            GuiConditionEditor.INSTANCE.initGui();
        }
        
        CollectionHelper.remove(drawable, drawing);
    }
}
