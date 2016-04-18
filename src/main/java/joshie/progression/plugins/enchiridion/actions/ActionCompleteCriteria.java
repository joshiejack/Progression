package joshie.progression.plugins.enchiridion.actions;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;

public class ActionCompleteCriteria extends AbstractActionCriteria implements IButtonAction {
    public boolean mustBeCompleted = true;

    public ActionCompleteCriteria() {
        super(null, "criteria.complete");
    }

    public ActionCompleteCriteria(ICriteria criteria, boolean mustBeCompleted) {
        super(criteria, "criteria.complete");
        this.mustBeCompleted = mustBeCompleted;
    }

    @Override
    public IButtonAction copy() {
        return copyAbstract(new ActionCompleteCriteria(getCriteria(), mustBeCompleted));
    }

    @Override
    public IButtonAction create() {
        return new ActionCompleteCriteria(APIHandler.getCache(true).getRandomCriteria(), true);
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
