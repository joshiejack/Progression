package joshie.progression.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import joshie.progression.api.criteria.IFieldProvider;
import joshie.progression.api.criteria.IProgressionCondition;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.gui.editors.GuiConditionEditor;
import joshie.progression.gui.editors.GuiCriteriaEditor;
import joshie.progression.gui.editors.GuiItemFilterEditor;
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

    public static void removeAndUpdate(List<IFieldProvider> drawable, IFieldProvider drawing) {
        if (drawing instanceof IProgressionReward) {
            EventsManager.onRewardRemoved((IProgressionReward) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getRewards(), (IProgressionReward) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof IProgressionTrigger) {
            EventsManager.onTriggerRemoved((IProgressionTrigger) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getTriggers(), (IProgressionTrigger) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof IProgressionFilter) {
            GuiItemFilterEditor.INSTANCE.getField().remove((IProgressionFilter) drawing);
            //GuiItemFilterEditor.INSTANCE.initGui();
        } else if (drawing instanceof IProgressionCondition) {
            CollectionHelper.remove(GuiConditionEditor.INSTANCE.getTrigger().getConditions(), (IProgressionCondition) drawing);
            //GuiConditionEditor.INSTANCE.initGui();
        }

        CollectionHelper.remove(drawable, drawing);
    }

    public static void removeAll(Collection collection, List list) {
        for (Object object: list) {
            remove(collection, object);
        }
    }
}
