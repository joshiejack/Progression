package joshie.progression.items;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import joshie.progression.criteria.Criteria;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.RenderItemHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderItemCriteria implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return stack.hasTagCompound() && stack.getItemDamage() == ItemCriteria.CRITERIA;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack2, Object... data) {
        Criteria criteria = ItemCriteria.getCriteriaFromStack(stack2);
        if (criteria != null) {
            ItemStack stack = criteria.stack;
            if (type == ItemRenderType.INVENTORY) {
                RenderItemHelper.drawStack(stack, 0, 0, 1F);
                RenderHelper.enableGUIStandardItemLighting();
            } else {
                RenderBlocks blocks = (RenderBlocks) data[0];
                /** Item rendering code, lifted from botania by vazkii **/
                ClientHelper.getMinecraft().renderEngine.bindTexture(stack.getItem() instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
                GL11.glScalef(2F, 2F, 2F);
                if (!ForgeHooksClient.renderEntityItem(new EntityItem(ClientHelper.getWorld(), 0D, 0D, 0D, stack), stack, 0F, 0F, ClientHelper.getWorld().rand, ClientHelper.getMinecraft().renderEngine, blocks, 1)) {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                    if (stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                        GL11.glTranslatef(1F, 1.1F, 0F);
                        blocks.renderBlockAsItem(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 1F);
                        GL11.glTranslatef(-1F, -1.1F, 0F);
                        GL11.glScalef(2F, 2F, 2F);
                    } else {
                        int renderPass = 0;
                        do {
                            IIcon icon = stack.getItem().getIcon(stack, renderPass);
                            if (icon != null) {
                                Color color = new Color(stack.getItem().getColorFromItemStack(stack, renderPass));
                                GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                                float f = icon.getMinU();
                                float f1 = icon.getMaxU();
                                float f2 = icon.getMinV();
                                float f3 = icon.getMaxV();
                                ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
                                GL11.glColor3f(1F, 1F, 1F);
                            }
                            renderPass++;
                        } while (renderPass < stack.getItem().getRenderPasses(stack.getItemDamage()));
                    }
                }
            }
        }
    }
}