package joshie.progression.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import joshie.progression.api.IConditionType;
import joshie.progression.api.IFilter;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITriggerType;
import joshie.progression.api.fields.IFieldProvider;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
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
        if (drawing instanceof IRewardType) {
            EventsManager.onRewardRemoved((IRewardType) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getRewards(), (IRewardType) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof ITriggerType) {
            EventsManager.onTriggerRemoved((ITriggerType) drawing);
            CollectionHelper.remove(GuiCriteriaEditor.INSTANCE.getCriteria().getTriggers(), (ITriggerType) drawing);
            //GuiCriteriaEditor.INSTANCE.initGui();
        } else if (drawing instanceof IFilter) {
            GuiItemFilterEditor.INSTANCE.field.remove((IFilter) drawing);
            //GuiItemFilterEditor.INSTANCE.initGui();
        } else if (drawing instanceof IConditionType) {
            CollectionHelper.remove(GuiConditionEditor.INSTANCE.getTrigger().getConditions(), (IConditionType) drawing);
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
