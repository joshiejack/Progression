package joshie.crafting.gui;

import java.util.List;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITreeEditor;

import org.lwjgl.opengl.GL11;

public class GuiTreeEditorDisplay extends GuiBase {
    public static final GuiTreeEditorDisplay INSTANCE = new GuiTreeEditorDisplay();

    @Override
    public void drawScreen(int i, int j, float f) {
        int x = 0;
        int y = (height - ySize) / 2;
        super.drawScreen(i, j, f);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            ITreeEditor editor = criteria.getTreeEditor();
            List<ICriteria> prereqs = criteria.getRequirements();
            for (ICriteria p : prereqs) {
                int y1 = p.getTreeEditor().getY();
                int y2 = editor.getY();
                int x1 = p.getTreeEditor().getX();
                int x2 = editor.getX();
                drawLine(x + offsetX + 95 + x1, y + 12 + y1, x + offsetX + 5 + x2, y + 12 + y2, 2, 0xFF006DD9);
            }
        }

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            ITreeEditor editor = criteria.getTreeEditor();
            editor.draw(x, y, offsetX);
        }

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        if (previous != null) {
            List<ICriteria> conflicts = previous.getConflicts();
            for (ICriteria p : conflicts) {
                p.getTreeEditor().draw(x, y, offsetX, 0xFFB20000);
            }
        }
    }
}
