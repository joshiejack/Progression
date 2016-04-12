package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.IConditionProvider;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SplitHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FeatureTasks extends FeatureCriteria implements ISimpleEditorFieldProvider {
    public FeatureTasks() {}

    public FeatureTasks(String displayName, boolean background) {
        super(displayName, background);
    }

    @Override
    public FeatureTasks copy() {
        return new FeatureTasks(displayName, background);
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        if (criteria != null) {
            int size = criteria.getTriggers().size();
            for (ITriggerProvider trigger: criteria.getTriggers()) {
                size += trigger.getConditions().size();
            }

            position.setWidth((size * 17D) + ((size - 1) * 3D));
        }

        double width = position.getWidth();
        position.setHeight(17D);
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int x = 0;
        for (ITriggerProvider trigger : criteria.getTriggers()) {
            if (trigger.getIcon() == null) continue;
            ItemStack stack = trigger.getIcon().copy();
            int color = trigger.getConditions().size() > 0 ? trigger.getColor() : 0xFFD0BD92;
            if (background) EnchiridionAPI.draw.drawBorderedRectangle(position.getLeft() + x, position.getTop(), position.getLeft() + x + 16, position.getTop() + 16, 0xFFD0BD92, color);
            EnchiridionAPI.draw.drawStack(stack, position.getLeft() + x, position.getTop(), 1F);
            x += 20;

            for (IConditionProvider condition : trigger.getConditions()) {
                stack = condition.getIcon().copy();
                if (background) EnchiridionAPI.draw.drawBorderedRectangle(position.getLeft() + x, position.getTop(), position.getLeft() + x + 16, position.getTop() + 16, 0xFFD0BD92, condition.getColor());
                EnchiridionAPI.draw.drawStack(stack, position.getLeft() + x, position.getTop(), 1F);
                x += 20;
            }
        }
    }

    @Override
    public void addFeatureTooltip(List<String> tooltip, int mouseX, int mouseY) {
        int x = 0;
        int offsetMouseX = mouseX - position.getLeft();
        int offsetMouseY = mouseY - position.getTop();
        for (ITriggerProvider trigger : criteria.getTriggers()) {
            if (offsetMouseX >= x && offsetMouseX <= x + 17) {
                ItemStack stack = trigger.getIcon();
                tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
                tooltip.add("---");
                if (trigger.getProvided() instanceof ICustomTooltip) ((ICustomTooltip)trigger.getProvided()).addTooltip(tooltip);
                else{
                    for (String s : SplitHelper.splitTooltip(trigger.getDescription(), 32)) {
                        tooltip.add(s);
                    }
                }
            }

            x += 20;
            for (IConditionProvider condition: trigger.getConditions()) {
                if (offsetMouseX >= x && offsetMouseX <= x + 17) {
                    ItemStack stack = condition.getIcon();
                    tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
                    tooltip.add("---");
                    if (condition.getProvided() instanceof ICustomTooltip) ((ICustomTooltip)condition.getProvided()).addTooltip(tooltip);
                    else{
                        for (String s : SplitHelper.splitTooltip(condition.getDescription(), 32)) {
                            tooltip.add(s);
                        }
                    }
                }

                x += 20;
            }
        }
    }
}
