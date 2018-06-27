package selim.geyserrecipes.shared;

public class GeyserRecipesInfo {

	public static final String ID = "geyserrecipes";
	public static final String NAME = "Geyser Recipes";
	public static final String VERSION = "0.0.1";
	public static final String CHANNEL = "geyser_recipes";
	public static final String SHAPED_RECIPE = "geyser_recipes:shaped";
	public static final String SHAPELESS_RECIPE = "geyser_recipes:shapeless";

	public static class PacketDiscrimators {
		public static final char RECIPE = (char) 'r';
		public static final char CLEAR_RECIPES = (char) 'c';
	}

}
