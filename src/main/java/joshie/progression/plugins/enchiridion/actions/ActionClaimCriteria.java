package joshie.progression.plugins.enchiridion.actions;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.player.PlayerTracker;

import static joshie.progression.gui.core.GuiList.REWARDS;

public class ActionClaimCriteria extends AbstractActionCriteria implements IButtonAction {
    public ActionClaimCriteria() {
        super(null, "criteria.claim");
    }

    public ActionClaimCriteria(ICriteria criteria) {
        super(criteria, "criteria.claim");
    }

    @Override
    public IButtonAction copy() {
        return copyAbstract(new ActionClaimCriteria());
    }

    @Override
    public IButtonAction create() {
        return new ActionClaimCriteria();
    }

    @Override
    public boolean isVisible() {
        ICriteria criteria = getCriteria();

        if (criteria == null || criteria.getRewards().size() == 0) return true;
        for (ITriggerProvider trigger: criteria.getTriggers()) {
            if (!trigger.getProvided().isCompleted()) return false;
        }

        if (!criteria.canRepeatInfinite() && PlayerTracker.getClientPlayer().getMappings().getCriteriaCount(criteria) >= criteria.getRepeatAmount()) return false;

        return REWARDS.select(criteria.getRewards().get(0), true);
    }

    @Override
    public boolean performAction() {
        REWARDS.sendToServer();
        return true;
    }
}
