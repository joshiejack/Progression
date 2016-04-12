package joshie.progression.plugins.enchiridion.features;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SplitHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FeatureRewards extends FeatureCriteria implements ISimpleEditorFieldProvider {
    private transient Cache<Boolean, List<IRewardProvider>> cache = CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(1, TimeUnit.MINUTES).build();
    public boolean text = true;

    public FeatureRewards() {}

    public FeatureRewards(String displayName, boolean background) {
        super(displayName, background);
    }

    @Override
    public FeatureRewards copy() {
        return new FeatureRewards(displayName, background);
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        if (criteria != null) {
            List<IRewardProvider> always = buildLists(true);
            List<IRewardProvider> claim = buildLists(false);
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
    public void performClick(int mouseX, int mouseY) {
        List<IRewardProvider> always = buildLists(true);
        List<IRewardProvider> claim = buildLists(false);
        if (claim.size() > 0) {
            int yOffset = 0;
            if (always.size() != 0) {
                yOffset = 30;
            }
        }
    }

    private void drawList(List<IRewardProvider> provider, int offsetY) {
        int x = 0;
        for (IRewardProvider reward : provider) {
            ItemStack stack = reward.getIcon().copy();
            if (background) EnchiridionAPI.draw.drawRectangle(position.getLeft() + x, position.getTop() + 10 + offsetY, position.getLeft() + x + 16, position.getTop() + 10 + 16 + offsetY, 0xFFD0BD92);
            EnchiridionAPI.draw.drawStack(stack, position.getLeft() + x, position.getTop() + 10 + offsetY, 1F);
            x += 20;
        }
    }

    private List<IRewardProvider> buildLists(final boolean value) {
        try {
            return cache.get(value, new Callable<List<IRewardProvider>>() {
                @Override
                public List<IRewardProvider> call() throws Exception {
                    List<IRewardProvider> list = new ArrayList<IRewardProvider>();
                    if (value && (criteria.givesAllRewards() || criteria.getRewards().size() < criteria.getAmountOfRewards())) {
                        list.addAll(criteria.getRewards());
                    }

                    for (IRewardProvider reward: criteria.getRewards()) {
                        if (!reward.mustClaim() && value) list.add(reward);
                        else if (!value && reward.mustClaim()) list.add(reward);
                    }

                    return list;
                }
            });
        } catch (Exception e) { return new ArrayList<IRewardProvider>(); }
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        List<IRewardProvider> always = buildLists(true);
        List<IRewardProvider> claim = buildLists(false);
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
                tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
                tooltip.add("---");
                if (reward.getProvided() instanceof ICustomTooltip) ((ICustomTooltip)reward.getProvided()).addTooltip(tooltip);
                else{
                    for (String s : SplitHelper.splitTooltip(reward.getDescription(), 32)) {
                        tooltip.add(s);
                    }
                }
            }

            x += 20;
        }
    }
    
    @Override
    public void addFeatureTooltip(List<String> tooltip, int mouseX, int mouseY) {
        int x = 0;
        int offsetMouseX = mouseX - position.getLeft();
        int offsetMouseY = mouseY - position.getTop();
        List<IRewardProvider> always = buildLists(true);
        List<IRewardProvider> claim = buildLists(false);
        int offsetY = always.size() != 0 ? 30: 0;
        addListTooltip(tooltip, always, offsetMouseX, offsetMouseY, 0);
        addListTooltip(tooltip, claim, offsetMouseX, offsetMouseY, offsetY);
    }
}
