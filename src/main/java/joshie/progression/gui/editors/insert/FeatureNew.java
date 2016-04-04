package joshie.progression.gui.editors.insert;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IRuleProvider;
import joshie.progression.gui.core.FeatureAbstract;
import joshie.progression.gui.core.GuiCore;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.*;

public abstract class FeatureNew<T> extends FeatureAbstract {
    public static boolean IS_OPEN = false;
    private final List<IRuleProvider> sorted;

    public String text;

    public FeatureNew(String text) {
        this.text = text;
        this.sorted = new ArrayList(getFields());
        Collections.sort(this.sorted, new Alphabetical());
    }

    private static class Alphabetical implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            IRuleProvider provider1 = ((IRuleProvider) o1);
            IRuleProvider provider2 = ((IRuleProvider) o2);
            return provider1.getLocalisedName().compareTo(provider2.getLocalisedName());
        }
    }

    public abstract Collection<T> getFields();

    public abstract void clone(T t);
    
    public boolean isValid(T t) {
        return true;
    }

    @Override
    public FeatureAbstract init(GuiCore core) {
        super.init(core);
        setVisibility(false);
        return this;
    }

    @Override
    public boolean isOverlay() {
        return true;
    }

    public int getColor() {
        return 0xFF333333;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        int maxY = ((sorted.size() - 3) / 2);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        offset.drawRectangle(150, 50, 200, 43 + maxY * 12, 0xFF222222, theme.newBox2);
        offset.drawGradient(150, 50, 200, 15, getColor(), 0xFF111111, theme.newBorder);
        offset.drawText(Progression.translate("new." + text), 155, 54, theme.newFont);
        drawForeground(mouseX, mouseY);
    }

    public void drawForeground(int mouseX, int mouseY) {
        int maxY = ((sorted.size() - 1) / 2);
        int yPos = 0;
        int xPos = 0;
        for (IRuleProvider provider : sorted) {
            if (!isValid((T) provider)) continue;
            int color = theme.newFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 67 + (yPos * 12) && mouseY < 67 + (yPos * 12) + 12) {
                    color = 0xFF555555;
                }
            }

            offset.drawText(provider.getLocalisedName(), (xPos * 100) + 155, 67 + (yPos * 12), color);

            yPos++;

            if (yPos > maxY) {
                xPos++;
                yPos = 0;
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int maxY = ((sorted.size() - 1) / 2);
        int yPos = 0;
        int xPos = 0;
        for (IRuleProvider provider : sorted) {
            if (!isValid((T) provider)) continue;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 67 + (yPos * 12) && mouseY < 67 + (yPos * 12) + 12) {
                    IS_OPEN = false;
                    clone((T) provider);
                    setVisibility(false);
                    return true;
                }
            }

            yPos++;

            if (yPos > maxY) {
                xPos++;
                yPos = 0;
            }
        }

        return false;
    }

    @Override
    public void setVisibility(boolean value) {
        super.setVisibility(value);
        IS_OPEN = value;
    }
}
