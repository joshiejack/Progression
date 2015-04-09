package joshie.crafting.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderItemHelper {
    private final static RenderItem itemRenderer = new RenderItem();

    public static void drawStack(ItemStack stack, int left, int top, float size) {
        try {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPushMatrix();
            GL11.glScalef(size, size, size);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
            GL11.glEnable(GL11.GL_BLEND); //Forge: Make sure blend is enabled else tabs show a white border.
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft mc = ClientHelper.getMinecraft();
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) (left / size), (int) (top / size));
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) (left / size), (int) (top / size));
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
        } catch (Exception e) {}
    }
}
