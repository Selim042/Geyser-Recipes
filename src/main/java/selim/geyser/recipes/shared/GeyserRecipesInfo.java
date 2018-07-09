package selim.geyser.recipes.shared;

public class GeyserRecipesInfo {

	public static final String ID = "geyserrecipes";
	public static final String NAME = "Geyser Recipes";
	public static final String VERSION = "1.0.0";
	public static final String CHANNEL = "geyser_recipes";
	public static final String DOWNLOAD_LINK = "https://minecraft.curseforge.com/projects/geyser-recipes";
	public static final String SHAPED_RECIPE = "geyser_recipes:shaped";
	public static final String SHAPELESS_RECIPE = "geyser_recipes:shapeless";

	public static class PacketDiscrimators {
		public static final char RECIPE = (char) 'r';
		public static final char CLEAR_RECIPES = (char) 'c';
	}

}
