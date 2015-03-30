package joshie.crafting.gui;

import java.util.ArrayList;

import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.helpers.EntityHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public class SelectEntity extends TextEditable implements IRenderOverlay {
    public static SelectEntity INSTANCE = new SelectEntity();
    private static IEntitySelectable selectable = null;
    private static ArrayList<EntityLivingBase> sorted;
    private static String search = "";
    private static int position;
    private static Type type;

    public void select(IEntitySelectable selectable, Type type) {
        if (reset()) {
            //Setup the info
            SelectEntity.type = type;
            SelectEntity.selectable = selectable;
            super.position = search.length();
        }
    }

    @Override
    void clear() {
        SelectEntity.selectable = null;
        SelectEntity.search = "";
        SelectEntity.position = 0;
    }

    public static interface IEntitySelectable {
        public void setEntity(Entity entity);
    }

    public void updateSearch() {
        if (search == null || search.equals("")) {
            sorted = new ArrayList(EntityHelper.getEntities());
        } else {
            position = 0;
            sorted = new ArrayList();
            for (EntityLivingBase entity : EntityHelper.getEntities()) {
                if (EntityList.getEntityString(entity).toLowerCase().contains(search.toLowerCase())) {
                    sorted.add(entity);
                }
            }
        }
    }

    @Override
    public void setTextField(String text) {
        this.search = text;
        this.updateSearch();
    }

    @Override
    public String getTextField() {
        return this.search;
    }

    @Override
    public boolean isVisible() {
        return selectable != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (sorted == null) {
            updateSearch();
        }

        mouseY -= type.yOffset;

        ScaledResolution res = new ScaledResolution(GuiTreeEditorEdit.INSTANCE.mc, GuiTreeEditorEdit.INSTANCE.mc.displayWidth, GuiTreeEditorEdit.INSTANCE.mc.displayHeight);
        int fullWidth = res.getScaledWidth() - 10;
        int j = 0;
        int k = 0;
        for (int i = position; i < position + (fullWidth * 4); i++) {
            if (i >= 0 && i < sorted.size()) {
                int offX = GuiCriteriaEditor.INSTANCE.offsetX;
                if (mouseX >= 8 + (j * 32) + offX && mouseX <= 8 + (j * 32) + 32 + offX) {
                    if (mouseY >= 45 + (k * 32) && mouseY <= 45 + (k * 32) + 64) {
                        selectable.setEntity(sorted.get(i));
                        clear();
                        return true;
                    }
                }

                j++;

                if (j > fullWidth) {
                    j = 0;
                    k++;
                }
            }
        }

        return false;
    }

    @Override
    public void draw(int x, int y) {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        if (selectable != null) {
            if (sorted == null) {
                updateSearch();
            }

            int yPos = GuiCriteriaEditor.INSTANCE.y;
            int offX = GuiCriteriaEditor.INSTANCE.offsetX;
            drawBox(0, 40 + type.yOffset, GuiTreeEditorEdit.INSTANCE.mc.displayWidth, 73, 0xFFFFFFFF, 0xFFFFFFFF);

            ScaledResolution res = new ScaledResolution(GuiTreeEditorEdit.INSTANCE.mc, GuiTreeEditorEdit.INSTANCE.mc.displayWidth, GuiTreeEditorEdit.INSTANCE.mc.displayHeight);
            int fullWidth = res.getScaledWidth() - 10;
            drawText(getText(), 190, 29 + type.yOffset, 0xFF000000);

            int j = 0;
            int k = 0;
            for (int i = position; i < position + (fullWidth * 4); i++) {
                if (i >= 0 && i < sorted.size()) {
                    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    GuiInventory.func_147046_a(offX + 24 + (j * 32), yPos + 105 + (k * 32) + type.yOffset, EntityHelper.getSizeForEntity(sorted.get(i)), 25F, -5F, sorted.get(i));
                    //deaw
                    j++;

                    if (j > fullWidth) {
                        j = 0;
                        k++;
                    }
                }
            }
        }
    }
}