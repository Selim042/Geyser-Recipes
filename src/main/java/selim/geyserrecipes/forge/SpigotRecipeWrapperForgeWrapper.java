package selim.geyserrecipes.forge;

import java.util.LinkedList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SpigotRecipeWrapperForgeWrapper implements IShapedCraftingRecipeWrapper {

	private final SpigotRecipeWrapperForge recipeWrapper;
	private final IJeiHelpers jeiHelpers;

	public SpigotRecipeWrapperForgeWrapper(IJeiHelpers jeiHelpers,
			SpigotRecipeWrapperForge recipeWrapper) {
		this.jeiHelpers = jeiHelpers;
		this.recipeWrapper = recipeWrapper;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper stackHelper = jeiHelpers.getStackHelper();
		ingredients.setInputLists(ItemStack.class, stackHelper
				.expandRecipeItemStackInputs(this.recipeWrapper.getRecipe().getIngredients()));
		ItemStack result = this.recipeWrapper.getRecipe().getRecipeOutput();
		ItemStack wrappedStack = DummyItem.getWrapperStack(result);
		// if (wrappedStack == ItemStack.EMPTY)
		List<ItemStack> output = new LinkedList<>();
		output.add(wrappedStack);
		output.add(result);
		ingredients.setOutput(ItemStack.class, output);
		// else
		// ingredients.setOutput(ItemStack.class,
		// Collections.singletonList(wrappedStack));
	}

	@Override
	public ResourceLocation getRegistryName() {
		return this.recipeWrapper.getRecipe().getRegistryName();
	}

	@Override
	public int getHeight() {
		return 3;
	}

	@Override
	public int getWidth() {
		return 3;
	}

}
