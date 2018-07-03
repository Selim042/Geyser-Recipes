package selim.geyserrecipes.forge;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

	private static final List<SpigotRecipeWrapperForgeWrapper> SHOWN_RECIPES = new CopyOnWriteArrayList<>();
	private static final List<SpigotRecipeWrapperForgeWrapper> HIDDEN_RECIPES = new CopyOnWriteArrayList<>();
	private static IModRegistry MOD_REGISTRY;
	private static IRecipeRegistry RECIPE_REGISTRY;
	private static IIngredientBlacklist INGREDIENT_BLACKLIST;

	@Override
	public void register(IModRegistry registry) {
		MOD_REGISTRY = registry;
		INGREDIENT_BLACKLIST = registry.getJeiHelpers().getIngredientBlacklist();
		registry.handleRecipes(SpigotRecipeWrapperForge.class,
				new IRecipeWrapperFactory<SpigotRecipeWrapperForge>() {

					@Override
					public IRecipeWrapper getRecipeWrapper(SpigotRecipeWrapperForge recipeWrapper) {
						return new SpigotRecipeWrapperForgeWrapper(MOD_REGISTRY.getJeiHelpers(),
								recipeWrapper);
					}
				}, VanillaRecipeCategoryUid.CRAFTING);
		// registry.addRecipeRegistryPlugin(new
		// SpigotRecipeRegistryProvider(registry));
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.registerSubtypeInterpreter(GeyserRecipesForge.DUMMY_ITEM,
				new DummyItemSubtypeInterpreter());
		// subtypeRegistry.useNbtForSubtypes(GeyserRecipesForge.DUMMY_ITEM);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		RECIPE_REGISTRY = jeiRuntime.getRecipeRegistry();
		IRecipeRegistry recipeRegistry = jeiRuntime.getRecipeRegistry();

		for (Object o : recipeRegistry.getRecipeWrappers(
				recipeRegistry.getRecipeCategory(VanillaRecipeCategoryUid.CRAFTING))) {

			if (o instanceof SpigotRecipeWrapperForgeWrapper) {
				SpigotRecipeWrapperForgeWrapper wrapper = (SpigotRecipeWrapperForgeWrapper) o;
				if (HIDDEN_RECIPES.contains(wrapper))
					recipeRegistry.hideRecipe(wrapper);
			}
		}
	}

	public static void hideRecipes() {
		for (SpigotRecipeWrapperForgeWrapper wrapper : SHOWN_RECIPES) {
			SHOWN_RECIPES.remove(wrapper);
			HIDDEN_RECIPES.add(wrapper);
		}
	}

	@SuppressWarnings("deprecation")
	public static void addRecipe(SpigotRecipeWrapperForge recipe) {
		if (!Loader.isModLoaded("jei"))
			return;
		System.out.println("adding " + recipe.getRecipe().getRegistryName());
		SpigotRecipeWrapperForgeWrapper wrapper = new SpigotRecipeWrapperForgeWrapper(
				MOD_REGISTRY.getJeiHelpers(), recipe);
		RECIPE_REGISTRY.addRecipe(wrapper, VanillaRecipeCategoryUid.CRAFTING);
		SHOWN_RECIPES.add(wrapper);
	}

	public static void addRecipes(Collection<SpigotRecipeWrapperForge> recipes) {
		if (!Loader.isModLoaded("jei"))
			return;
		for (SpigotRecipeWrapperForge wrapper : recipes)
			addRecipe(wrapper);
	}

}
