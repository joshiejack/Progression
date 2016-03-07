package joshie.progression.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderItemHelper {
    private final static RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
    
    public static void drawStack(ItemStack stack, int left, int top, float size) {
        try {
        	GlStateManager.pushMatrix();
            GlStateManager.scale(size, size, size);
            GlStateManager.disableLighting();
            GlStateManager.color(1F, 1F, 1F, 1F); //Forge: Reset color in case Items change it.
            GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
            GlStateManager.enableLighting();
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft mc = Minecraft.getMinecraft();
            itemRenderer.renderItemAndEffectIntoGUI(stack, (int) (left / size), (int) (top / size));
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) (left / size), (int) (top / size), null);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        } catch (Exception e) {}
    }

	public static void register(Item item, int meta, ModelResourceLocation location) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, location);
	}
}
