package selim.geyserrecipes.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class DummyItemRenderer extends TileEntityItemStackRenderer {

	@Override
	public void renderByItem(ItemStack stack) {
		ItemStack wrapperStack = DummyItem.getWrappedStack(stack);
		if (wrapperStack == null || stack.isEmpty())
			return;

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem()
				.getItemModelWithOverrides(wrapperStack, null, null);
		// boolean flag1 = ibakedmodel.isGui3d();
		//
		// if (!flag1) {
		// float f3 = -0.0F * (float) (j - 1) * 0.5F;
		// float f4 = -0.0F * (float) (j - 1) * 0.5F;
		// float f5 = -0.09375F * (float) (j - 1) * 0.5F;
		// GlStateManager.translate(f3, f4, f5);
		// }

		// for (int k = 0; k < j; ++k) {
		// if (flag1) {
		// GlStateManager.pushMatrix();
		//
		// IBakedModel transformedModel =
		// net.minecraftforge.client.ForgeHooksClient
		// .handleCameraTransforms(ibakedmodel,
		// ItemCameraTransforms.TransformType.GROUND,
		// false);
		// Minecraft.getMinecraft().getRenderItem().renderItem(wrapperStack,
		// transformedModel);
		// GlStateManager.popMatrix();
		// } else {
		GlStateManager.pushMatrix();

		IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient
				.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GUI, false);
		Minecraft.getMinecraft().getRenderItem().renderItem(wrapperStack, transformedModel);
		GlStateManager.popMatrix();
		GlStateManager.translate(0.0F, 0.0F, 0.09375F);
		// }
		// }

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

}
