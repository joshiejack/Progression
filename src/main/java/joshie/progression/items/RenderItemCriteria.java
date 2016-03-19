package joshie.progression.items;

import java.util.List;

import joshie.progression.ClientProxy;
import joshie.progression.Progression;
import joshie.progression.api.ICriteria;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderItemCriteria implements ISmartItemModel {
	private ItemStack broken = new ItemStack(Items.book);
	private ItemModelMesher mesher;
	
	@SubscribeEvent
	public void onCookery(ModelBakeEvent event) {
		event.modelRegistry.putObject(ClientProxy.criteria, this);
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		if (mesher == null) mesher  = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ICriteria criteria = ItemCriteria.getCriteriaFromStack(stack);
		if (criteria != null && criteria.getIcon().getItem() != stack.getItem() && criteria.getIcon().getItem() != Progression.item) {
			return mesher.getItemModel(criteria.getIcon());
		}
		
		return mesher.getItemModel(broken);
	} 
	
	/** Redundant crap below :/ **/
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing facing) {
		return null;
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return null;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return null;
	}
}