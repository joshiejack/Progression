package joshie.progression.helpers;

import joshie.progression.api.criteria.*;
import joshie.progression.gui.editors.GuiConditionEditor;
import joshie.progression.gui.editors.GuiCriteriaEditor;
import joshie.progression.gui.editors.GuiFilterEditor;
import joshie.progression.handlers.EventsManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
        if (drawing instanceof IReward) {
            EventsManager.onRemoved((IReward) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getRewards(), (IReward) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof ITrigger) {
            EventsManager.onRemoved((ITrigger) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getTriggers(), (ITrigger) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof IFilter) {
            EventsManager.onRemoved((IFilter) drawing);
            GuiFilterEditor.INSTANCE.getField().remove((IFilter) drawing);
            //GuiFilterEditor.INSTANCE.initGui();
        } else if (drawing instanceof ICondition) {
            CollectionHelper.remove(GuiConditionEditor.INSTANCE.getTrigger().getConditions(), (ICondition) drawing);
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
