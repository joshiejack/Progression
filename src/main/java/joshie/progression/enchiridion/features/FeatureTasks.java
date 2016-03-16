package joshie.progression.enchiridion.features;

import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.api.ITriggerType;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.item.ItemStack;

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
    public void drawFeature(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
        int x = 0;
        for (ITriggerType trigger : criteria.getTriggers()) {
            ItemStack stack = trigger.getIcon();
            if (background) EnchiridionAPI.draw.drawRectangle(xPos + x, yPos, xPos + x + 17, yPos + 17, 0xFFD2C9B5);
            EnchiridionAPI.draw.drawStack(stack, xPos + x, yPos, 1F);
            x += 20;
        }
    }

    @Override
    public void addFeatureTooltip(List<String> tooltip, int mouseX, int mouseY) {
        int x = 0;
        int offsetMouseX = mouseX - provider.getX();
        int offsetMouseY = mouseY - provider.getY();
        for (ITriggerType trigger : criteria.getTriggers()) {
            ItemStack stack = trigger.getIcon();
            if (offsetMouseX >= x && offsetMouseX <= x + 17) {
                tooltip.add(trigger.getDescription());
                //tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
            }
            
            x += 20;
        }
    }
}
