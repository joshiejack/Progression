package joshie.progression.plugins.enchiridion.actions;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APICache;

public class ActionCompleteCriteria extends AbstractActionCriteria implements IButtonAction {
    public ActionCompleteCriteria() {
        super(null, "criteria.complete");
    }

    public ActionCompleteCriteria(ICriteria criteria) {
        super(criteria, "criteria.complete");
    }

    @Override
    public IButtonAction copy() {
        return copyAbstract(new ActionCompleteCriteria(getCriteria()));
    }

    @Override
    public IButtonAction create() {
        return new ActionCompleteCriteria(APICache.getClientCache().getRandomCriteria());
    }

    @Override
    public boolean performAction() {
        if (getCriteria() != null) {
            ProgressionAPI.registry.fireTriggerClientside("forced-complete", getCriteria());
            return true;
        }

        return false;
    }
}
