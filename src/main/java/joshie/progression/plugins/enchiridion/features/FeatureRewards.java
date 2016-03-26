package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FeatureRewards extends FeatureCriteria implements ISimpleEditorFieldProvider {
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
            int size = criteria.getRewards().size();
            position.setWidth((size * 17D) + ((size - 1) * 3D));
        }

        double width = position.getWidth();
        position.setHeight(17D);
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int x = 0;
        for (IProgressionReward reward : criteria.getRewards()) {
            ItemStack stack = reward.getIcon();
            if (background) EnchiridionAPI.draw.drawRectangle(position.getLeft() + x, position.getTop(), position.getLeft() + x + 17, position.getTop() + 17, 0xFFD2C9B5);
            EnchiridionAPI.draw.drawStack(stack, position.getLeft() + x, position.getTop(), 1F);
            x += 20;
        }
    }
    
    @Override
    public void addFeatureTooltip(List<String> tooltip, int mouseX, int mouseY) {
        int x = 0;
        int offsetMouseX = mouseX - position.getLeft();
        int offsetMouseY = mouseY - position.getTop();
        for (IProgressionReward reward : criteria.getRewards()) {
            ItemStack stack = reward.getIcon();
            if (offsetMouseX >= x && offsetMouseX <= x + 17) {
                tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
            }
            
            x += 20;
        }
    }
}
