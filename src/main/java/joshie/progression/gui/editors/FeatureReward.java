package joshie.progression.gui.editors;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.gui.core.GuiList;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSelectRewards;
import joshie.progression.player.PlayerTracker;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static joshie.progression.api.special.DisplayMode.EDIT;
import static joshie.progression.gui.core.GuiList.*;

public class FeatureReward extends FeatureDrawable<IRewardProvider> {
    private Set<IRewardProvider> selected;

    public FeatureReward() {
        super("reward", 140, NEW_REWARD, THEME.rewardBoxGradient1, THEME.rewardBoxGradient2, THEME.rewardBoxFont, THEME.rewardBoxGradient2);
    }

    public void reset(ICriteria criteria) {
        if (selected == null) {
            selected = new LinkedHashSet<IRewardProvider>();
        } else {
            if (selected.size() > 0) {
                IRewardProvider first = null;
                for (IRewardProvider reward: selected) {
                    first = reward;
                    break;
                }

                //If we are a different criteria, then reset everything
                if (!first.getCriteria().getUniqueID().equals(criteria.getUniqueID())) {
                    selected = new LinkedHashSet<IRewardProvider>();
                }
            }
        }
    }

    @Override
    public List<IRewardProvider> getList() {
        return CRITERIA_EDITOR.get().getRewards();
    }

    public Set<IRewardProvider> getSelected() {
        return selected;
    }

    @Override
    public int drawSpecial(IRewardProvider drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        boolean allTrue = true;
        for (ITriggerProvider provider: drawing.getCriteria().getTriggers()) {
            if (!provider.getProvided().isCompleted()) allTrue = false;
        }

        if (allTrue) {
            if (selected.contains(drawing) || !drawing.mustClaim()) {
                offset.drawGradient(offsetX, offsetY, 1, 2, drawing.getWidth(GuiList.MODE) - 1, 75, 0x33222222, 0x00CCCCCC, 0x00000000);
            }
        }

        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IRewardProvider provider, int mouseOffsetX, int mouseOffsetY) {
        if (MODE == EDIT) return false; //Don't you dare!

        if (mouseOffsetY < 2 || mouseOffsetY > 73) return false;
        for (ITriggerProvider trigger: provider.getCriteria().getTriggers()) {
            if (!trigger.getProvided().isCompleted()) return false;
        }

        ICriteria criteria = provider.getCriteria();
        if (!criteria.canRepeatInfinite() && PlayerTracker.getClientPlayer().getMappings().getCriteriaCount(criteria) >= criteria.getRepeatAmount()) return false;

        if (mouseOffsetX > 0 && mouseOffsetX < provider.getWidth(MODE) && provider.mustClaim()) {
            if (select(provider)) sendToServer();
            return true;
        }

        return false;
    }

    public void sendToServer() {
        PacketHandler.sendToServer(new PacketSelectRewards(selected));
        selected = new LinkedHashSet<IRewardProvider>(); //Reset the hashset
    }

    public boolean isSelected(IRewardProvider provider) {
        return selected == null? false: selected.contains(provider);
    }

    public boolean select(IRewardProvider provider) {
        return select(provider, false);
    }

    public boolean select(IRewardProvider provider, boolean simulate) {
        if (selected == null) return false; //You are not allowed to be visible if selections can't even happen
        //Click processed as this item must be claimed, now we check the side of selected, vs other things
        if (provider != null && !simulate) {
            if (selected.contains(provider)) {
                CollectionHelper.remove(selected, provider);
                return false;
            } //If we already had it, screw validation
        }

        int standard = 0;
        ICriteria criteria = provider.getCriteria();
        int maximum = criteria.givesAllRewards() ? criteria.getRewards().size() : criteria.getAmountOfRewards();
        for (IRewardProvider reward : criteria.getRewards()) {
            if (!reward.mustClaim()) standard++;
        }

        int current = selected.size() + standard;
        if (current < maximum && !simulate) {
            selected.add(provider);
            current++;
        }

        if (current >= maximum) { //TODO: Automatic claiming, replace wth a button
            return true;
        }

        return false;
    }
}
