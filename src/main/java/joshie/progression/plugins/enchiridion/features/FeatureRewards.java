package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IReward;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.api.special.ICustomTreeIcon;
import joshie.progression.api.special.IMiniIcon;
import joshie.progression.api.special.IStackSizeable;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.player.PlayerTracker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static joshie.progression.gui.core.GuiList.REWARDS;

public class FeatureRewards extends FeatureCriteria implements ISimpleEditorFieldProvider {
    public boolean text = true;
    public boolean showHidden = false;

    public FeatureRewards() {}

    public FeatureRewards(ICriteria criteria, boolean background) {
        super(criteria, background);
    }

    @Override
    public FeatureRewards copy() {
        return new FeatureRewards(getCriteria(), background);
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        if (getCriteria() != null) {
            ICriteria criteria = getCriteria();
            List<IRewardProvider> always = buildLists(criteria, true);
            List<IRewardProvider> claim = buildLists(criteria, false);
            int size = always.size() > claim.size() ? always.size(): claim.size();
            position.setWidth((size * 17D) + ((size - 1) * 3D));

            double height = always.size() != 0 && claim.size() != 0 ? 56D: 28D;
            position.setHeight(height);
        } else {
            double width = position.getWidth();
            position.setHeight(17D);
        }
    }

    @Override
    public boolean performClick(int gg, int gg2, int mouseButton) {
        ICriteria criteria = getCriteria();
        if (criteria == null) return false;
        for (ITriggerProvider trigger: criteria.getTriggers()) {
            if (!trigger.getProvided().isCompleted()) return false;
        }

        List<IRewardProvider> always = buildLists(criteria, true);
        List<IRewardProvider> claim = buildLists(criteria, false);
        int offsetMouseX = gg - position.getLeft();
        int offsetMouseY = gg2 - position.getTop();
        int offsetY = always.size() != 0 ? 30: 0;
        int x = 0;
        for (IRewardProvider reward : claim) {
            if (!PlayerTracker.getClientPlayer().getMappings().getUnclaimedRewards(PlayerHelper.getClientUUID()).contains(reward)) continue;
            if (offsetMouseX >= x && offsetMouseX <= x + 16 && offsetMouseY >= 10 + offsetY && offsetMouseY <= 10 + offsetY + 16) {
                //Clicked
                REWARDS.select(reward);
                return true;
            }

            x += 20;
        }

        return false;
    }

    private void drawList(List<IRewardProvider> providers, int y) {
        int x = 0;
        for (IRewardProvider provider : providers) {
            if (background) {
                int color = 0xFFD0BD92;
                if (REWARDS.isSelected(provider)) {
                    color = 0xFFCCCCCC;
                }

                EnchiridionAPI.draw.drawRectangle(position.getLeft() + x, position.getTop() + 10 + y, position.getLeft() + x + 16, position.getTop() + 10 + 16 + y, color);
            }

            IReward reward = provider.getProvided();
            boolean mini = reward instanceof IMiniIcon;
            int stackSize = reward instanceof IStackSizeable ? ((IStackSizeable)reward).getStackSize() : 1;
            if (reward instanceof ICustomTreeIcon) {
                ((ICustomTreeIcon)reward).draw(position.getLeft() + x, position.getTop() + 10 + y, 1F);
            } else {
                if (provider.getIcon() == null) continue;
                ItemStack stack = provider.getIcon().copy();
                if (!mini) stack.stackSize = stackSize;
                EnchiridionAPI.draw.drawStack(stack, position.getLeft() + x, position.getTop() + 10 + y, 1F);
            }

            if (reward instanceof IMiniIcon) {
                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                ItemStack miniIcon = ((IMiniIcon)reward).getMiniIcon();
                miniIcon.stackSize = stackSize;
                EnchiridionAPI.draw.drawStack(miniIcon, position.getLeft() + x, position.getTop() + y, 1F);
            }

            x += 20;

            if (x > 160) {
                x = 0;
                y += 20;
            }
        }
    }

    private List<IRewardProvider> buildLists(final ICriteria criteria, final boolean value) {
        List<IRewardProvider> list = new ArrayList<IRewardProvider>();
        if (value && (criteria.givesAllRewards() || criteria.getRewards().size() < criteria.getAmountOfRewards())) {
            for (IRewardProvider reward: criteria.getRewards()) {
                if (!reward.isVisible() && !showHidden) continue;
                else list.add(reward);
            }
        }

        if (!criteria.givesAllRewards()) {
            for (IRewardProvider reward : criteria.getRewards()) {
                if (!reward.isVisible() && !showHidden) continue;
                else if (!reward.mustClaim() && value) list.add(reward);
                else if (!value && reward.mustClaim()) list.add(reward);
            }
        }

        return list;
    }

    @Override
    public void drawFeature(ICriteria criteria, int mouseX, int mouseY) {
        update(position);

        List<IRewardProvider> always = buildLists(criteria, true);
        List<IRewardProvider> claim = buildLists(criteria, false);
        int yOffsetClaimable = 0;
        if (always.size() != 0) {
            if (text) EnchiridionAPI.draw.drawSplitScaledString("Rewards", position.getLeft() - 2, position.getTop(), 200, 0x555555, 1F);
            yOffsetClaimable = 30;
        }

        if (claim.size() > 0) {
            if (text) EnchiridionAPI.draw.drawSplitScaledString("Select " + criteria.getAmountOfRewards() + " Rewards", position.getLeft() - 2, position.getTop() + yOffsetClaimable, 200, 0x555555, 1F);
        }

        drawList(always, 0);
        drawList(claim, yOffsetClaimable);
    }

    public void addListTooltip(List<String> tooltip, List<IRewardProvider> list, int mouseX, int mouseY, int offsetY) {
        int x = 0;
        for (IRewardProvider reward : list) {
            ItemStack stack = reward.getIcon();
            if (mouseX >= x && mouseX <= x + 16 && mouseY >= 10 + offsetY && mouseY <= 10 + offsetY + 16) {
                //tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
                //tooltip.add("---");
                if (reward.getProvided() instanceof ICustomTooltip) ((ICustomTooltip)reward.getProvided()).addTooltip(tooltip);
                else{
                    for (String s : SplitHelper.splitTooltip(reward.getDescription(), 32)) {
                        tooltip.add(s);
                    }
                }
            }

            x += 20;
            if (x > 160) {
                x = 0;
                offsetY += 20;
            }
        }
    }
    
    @Override
    public void addFeatureTooltip(ICriteria criteria, List<String> tooltip, int mouseX, int mouseY) {
        int offsetMouseX = mouseX - position.getLeft();
        int offsetMouseY = mouseY - position.getTop();
        List<IRewardProvider> always = buildLists(criteria, true);
        List<IRewardProvider> claim = buildLists(criteria, false);
        int offsetY = always.size() != 0 ? 30: 0;
        addListTooltip(tooltip, always, offsetMouseX, offsetMouseY, 0);
        addListTooltip(tooltip, claim, offsetMouseX, offsetMouseY, offsetY);
    }
}
