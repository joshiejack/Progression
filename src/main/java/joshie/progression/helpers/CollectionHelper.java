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

    public static void removeAndUpdate(List<IRuleProvider> drawable, IRuleProvider drawing) {
        if (drawing instanceof IRewardProvider) {
            EventsManager.onRemoved(((IRewardProvider) drawing).getProvided());
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getRewards(), (IRewardProvider) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof ITriggerProvider) {
            EventsManager.onRemoved(((ITriggerProvider) drawing).getProvided());
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getTriggers(), (ITriggerProvider) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof IFilterProvider) {
            EventsManager.onRemoved(((IFilterProvider) drawing).getProvided());
            GuiFilterEditor.INSTANCE.getField().remove((IFilterProvider) drawing);
            //GuiFilterEditor.INSTANCE.initGui();
        } else if (drawing instanceof IConditionProvider) {
            EventsManager.onRemoved(((IConditionProvider) drawing).getProvided());
            CollectionHelper.remove(GuiConditionEditor.INSTANCE.getTrigger().getConditions(), (IConditionProvider) drawing);
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
