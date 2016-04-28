package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.progression.ItemProgression.ItemMeta;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.*;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.SplitHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static joshie.progression.ItemProgression.getStackFromMeta;
import static joshie.progression.Progression.translate;

public class FeatureTasks extends FeatureCriteria implements ISimpleEditorFieldProvider {
    private transient static final ItemStack COMPLETED = getStackFromMeta(ItemMeta.completed);
    public boolean text = true;
    public boolean showHidden = false;

    public FeatureTasks() {}

    public FeatureTasks(ICriteria criteria, boolean background) {
        super(criteria, background);
    }

    @Override
    public FeatureTasks copy() {
        return new FeatureTasks(getCriteria(), background);
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        ICriteria criteria = getCriteria();
        if (criteria != null) {
            int size = criteria.getTriggers().size();
            for (ITriggerProvider trigger: criteria.getTriggers()) {
                size += trigger.getConditions().size();
            }

            double xWidth = Math.max(size, 9D);
            double yHeight = Math.ceil(size / 9D);
            position.setWidth((xWidth * 17D) + ((xWidth - 1) * 3D));
            double height = 28D + ((yHeight - 1D) * 20D);
            position.setHeight(height);
        } else {
            double width = position.getWidth();
            position.setHeight(17D);
        }
    }

    @Override
    public boolean performClick(int mouseX, int mouseY, int mouseButton) {
        ICriteria criteria = getCriteria();
        if (criteria == null) return false;

        int x = 0;
        int offsetMouseX = mouseX - position.getLeft();
        int offsetMouseY = mouseY - position.getTop();
        int offsetY = 10;
        for (ITriggerProvider trigger : criteria.getTriggers()) {
            if (trigger.isVisible() || showHidden) {
                if (trigger.getProvided() instanceof IClickable) {
                    if (offsetMouseY >= offsetY && offsetMouseY <= offsetY + 16) {
                        if (offsetMouseX >= x && offsetMouseX <= x + 17) {
                            return (((IClickable) trigger.getProvided()).onClicked(trigger.getIcon()));
                        }
                    }
                }

                x += 20;
                if (x > 160) {
                    x = 0;
                    offsetY += 20;
                }
            }

            for (IConditionProvider condition : trigger.getConditions()) {
                if (condition.isVisible() || showHidden) {
                    x += 20;
                    if (x > 160) {
                        x = 0;
                        offsetY += 20;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void drawFeature(ICriteria criteria, int mouseX, int mouseY) {
        update(position);

        int x = 0;
        int y = 10;
        for (ITriggerProvider triggerProvider : criteria.getTriggers()) {
            if (triggerProvider.isVisible() || showHidden) {
                ITrigger trigger = triggerProvider.getProvided();
                int color = triggerProvider.getConditions().size() > 0 ? triggerProvider.getColor() : 0xFFD0BD92;
                boolean mini = trigger instanceof IMiniIcon;
                int stackSize = trigger instanceof IStackSizeable ? ((IStackSizeable)trigger).getStackSize() : 1;
                if (background)
                    EnchiridionAPI.draw.drawBorderedRectangle(position.getLeft() + x, position.getTop() + y, position.getLeft() + x + 16, position.getTop() + 16 + y, 0xFFD0BD92, color);
                if (trigger instanceof ICustomTreeIcon) {
                    ((ICustomTreeIcon) trigger).draw(position.getLeft() + x, position.getTop() + y, 1F);
                } else {
                    if (triggerProvider.getIcon() == null) continue;
                    ItemStack stack = triggerProvider.getIcon().copy();
                    if (!mini) stack.stackSize = stackSize;
                    EnchiridionAPI.draw.drawStack(stack, position.getLeft() + x, position.getTop() + y, 1F);
                }

                if (mini) {
                    GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                    ItemStack miniIcon = ((IMiniIcon)trigger).getMiniIcon();
                    miniIcon.stackSize = stackSize;
                    EnchiridionAPI.draw.drawStack(miniIcon, position.getLeft() + x, position.getTop() + y, 1F);
                }

                if (trigger.isCompleted()) {
                    EnchiridionAPI.draw.drawStack(COMPLETED, position.getLeft() + x, position.getTop() + y, 1F);
                }

                x += 20;
                if (x > 160) {
                    x = 0;
                    y += 20;
                }
            }

            for (IConditionProvider conditionProvider : triggerProvider.getConditions()) {
                if (conditionProvider.isVisible() || showHidden) {
                    ICondition condition = conditionProvider.getProvided();
                    boolean mini = condition instanceof IMiniIcon;
                    int stackSize = condition instanceof IStackSizeable ? ((IStackSizeable)condition).getStackSize() : 1;
                    if (background) EnchiridionAPI.draw.drawBorderedRectangle(position.getLeft() + x, position.getTop() + y, position.getLeft() + x + 16, position.getTop() + y + 16, 0xFFD0BD92, conditionProvider.getColor());
                    if (condition instanceof ICustomTreeIcon) {
                        ((ICustomTreeIcon) condition).draw(position.getLeft() + x, position.getTop() + y, 1F);
                    } else {
                        if (conditionProvider.getIcon() == null) continue;
                        ItemStack stack = conditionProvider.getIcon().copy();
                        if (!mini) stack.stackSize = stackSize;

                        EnchiridionAPI.draw.drawStack(stack, position.getLeft() + x, position.getTop() + y, 1F);
                    }

                    if (condition instanceof IMiniIcon) {
                        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                        ItemStack miniIcon = ((IMiniIcon)condition).getMiniIcon();
                        miniIcon.stackSize = stackSize;
                        EnchiridionAPI.draw.drawStack(miniIcon, position.getLeft() + x, position.getTop() + y, 1F);
                    }

                    boolean completed = triggerProvider.getProvided().isCompleted();
                    if (!completed && conditionProvider.isInverted() && !conditionProvider.isSatisfied()) completed = true;
                    else if (!completed && !conditionProvider.isInverted() && conditionProvider.isSatisfied())  completed = true;

                    if (completed) {
                        EnchiridionAPI.draw.drawStack(COMPLETED, position.getLeft() + x, position.getTop() + y, 1F);
                    }

                    x += 20;

                    if (x > 160) {
                        x = 0;
                        y += 20;
                    }
                }
            }
        }

        if (criteria.getTriggers().size() != 0) {
            if (text) EnchiridionAPI.draw.drawSplitScaledString(translate("tasks"), position.getLeft() - 2, position.getTop(), 200, 0x555555, 1F);
        }
    }

    @Override
    public void addFeatureTooltip(ICriteria criteria, List<String> tooltip, int mouseX, int mouseY) {
        int x = 0;
        int offsetMouseX = mouseX - position.getLeft();
        int offsetMouseY = mouseY - position.getTop();
        int offsetY = 10;
        for (ITriggerProvider trigger : criteria.getTriggers()) {
            if (trigger.isVisible() || showHidden) {
                if (offsetMouseY >= offsetY && offsetMouseY <= offsetY + 16) {
                    if (offsetMouseX >= x && offsetMouseX <= x + 17) {
                        ItemStack stack = trigger.getIcon();
                        if (stack != null) {
                            tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
                            if (trigger.getProvided() instanceof IAdditionalTooltip) {
                                ((IAdditionalTooltip) trigger.getProvided()).addHoverTooltip("filters", stack, tooltip);
                            }

                            tooltip.add("---");
                            if (trigger.getProvided() instanceof ICustomTooltip)
                                ((ICustomTooltip) trigger.getProvided()).addTooltip(tooltip);
                            else {
                                for (String s : SplitHelper.splitTooltip(trigger.getDescription(), 32)) {
                                    tooltip.add(s);
                                }
                            }
                        }
                    }
                }

                x += 20;
                if (x > 160) {
                    x = 0;
                    offsetY += 20;
                }
            }

            for (IConditionProvider condition: trigger.getConditions()) {
                if (condition.isVisible() || showHidden) {
                    if (offsetMouseY >= offsetY && offsetMouseY <= offsetY + 16) {
                        if (offsetMouseX >= x && offsetMouseX <= x + 17) {
                            ItemStack stack = condition.getIcon();
                            tooltip.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
                            if (condition.getProvided() instanceof IAdditionalTooltip) {
                                ((IAdditionalTooltip) condition.getProvided()).addHoverTooltip("filters", stack, tooltip);
                            }

                            tooltip.add("---");
                            if (condition.getProvided() instanceof ICustomTooltip)
                                ((ICustomTooltip) condition.getProvided()).addTooltip(tooltip);
                            else {
                                for (String s : SplitHelper.splitTooltip(condition.getDescription(), 32)) {
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
            }
        }
    }
}
