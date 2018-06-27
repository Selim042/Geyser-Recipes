package selim.geyserrecipes.bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.netty.buffer.ByteBuf;
import selim.geyserrecipes.shared.GeyserRecipesInfo;

public class SpigotRecipeWrapperSpigot {

	private Recipe recipe;

	public SpigotRecipeWrapperSpigot() {}

	public SpigotRecipeWrapperSpigot(Recipe recipe) {
		this.recipe = recipe;
	}

	public Recipe getRecipe() {
		return this.recipe;
	}

	public void toBytes(ByteBuf buf) {
		String type = getRecipeType(recipe);
		if (type == null)
			return;
		BukkitByteBufUtils.writeUTF8String(buf, type);
		switch (type) {
		case GeyserRecipesInfo.SHAPED_RECIPE:
			writeShaped(buf, (ShapedRecipe) this.recipe);
			break;
		case GeyserRecipesInfo.SHAPELESS_RECIPE:
			writeShapeless(buf, (ShapelessRecipe) this.recipe);
			break;
		}
	}

	public void fromBytes(ByteBuf buf) {
		String type = BukkitByteBufUtils.readUTF8String(buf);
		if (type == null)
			return;
		switch (type) {
		case GeyserRecipesInfo.SHAPED_RECIPE:
			this.recipe = readShaped(buf);
			break;
		case GeyserRecipesInfo.SHAPELESS_RECIPE:
			this.recipe = readShapeless(buf);
			break;
		}
	}

	private static ShapedRecipe readShaped(ByteBuf buf) {
		// ResourceLocation
		NamespacedKey key = getKey(BukkitByteBufUtils.readUTF8String(buf));
		// Result
		ItemStack result = BukkitByteBufUtils.readItemStack(buf);
		ShapedRecipe recipe = new ShapedRecipe(key, result);
		// String[] shape = new String[] { "", "", "" };
		List<String> shapeList = new ArrayList<>();
		// Recipe shape, line by line
		int shapeLength = buf.readInt();
		for (int row = 0; row < shapeLength; row++)
			shapeList.add(BukkitByteBufUtils.readUTF8String(buf));
		String[] shape = shapeList.toArray(new String[0]);
		// HashMap<Character, ItemStack> map = new HashMap<>();
		// Size of ingredient map
		int size = buf.readInt();
		recipe.shape(shape);
		String blankLine = getEmptyString(getShapedWidth(recipe));
		for (int i = 0; i < 3 && i < shape.length; i++) {
			String row = shape[i];
			if (row.equals(""))
				shape[i] = blankLine;
		}
		for (int e = 0; e < size; e += 2) {
			char c = buf.readChar();
			ItemStack stack = BukkitByteBufUtils.readItemStack(buf);
			recipe.setIngredient(c, stack.getData());
		}
		return recipe;
	}

	private static void writeShaped(ByteBuf buf, ShapedRecipe recipe) {
		// ResourceLocation
		BukkitByteBufUtils.writeUTF8String(buf, recipe.getKey().toString());
		ItemStack result = recipe.getResult();
		// Result
		BukkitByteBufUtils.writeItemStack(buf, result);
		// Recipe shape, line by line
		String[] shape = recipe.getShape();
		buf.writeInt(shape.length);
		for (int row = 0; row < shape.length; row++)
			BukkitByteBufUtils.writeUTF8String(buf, shape[row]);
		Map<Character, ItemStack> map = recipe.getIngredientMap();
		// Size of ingredient map
		buf.writeInt(map.size());
		// Ingredient map, stack by stack
		for (Entry<Character, ItemStack> e : map.entrySet()) {
			ItemStack stack = e.getValue();
			if (stack != null && stack.getAmount() != 0 && stack.getType() != Material.AIR) {
				buf.writeChar(e.getKey());
				BukkitByteBufUtils.writeItemStack(buf, stack);
			}
		}
	}

	private static int getShapedWidth(ShapedRecipe recipe) {
		int width = 0;
		for (String row : recipe.getShape())
			if (row.length() > width)
				width = row.length();
		return width;
	}

	private static String getEmptyString(int length) {
		String retVal = "";
		for (int i = 0; i < length; i++)
			retVal += " ";
		return retVal;
	}

	private static ShapelessRecipe readShapeless(ByteBuf buf) {
		// ResourceLocation
		NamespacedKey key = getKey(BukkitByteBufUtils.readUTF8String(buf));
		// Result
		ItemStack result = BukkitByteBufUtils.readItemStack(buf);
		ShapelessRecipe recipe = new ShapelessRecipe(key, result);
		// Size of ingredient list
		int size = buf.readInt();
		for (int stack = 0; stack < size; stack++)
			recipe.addIngredient(BukkitByteBufUtils.readItemStack(buf).getData());
		return recipe;
	}

	private static void writeShapeless(ByteBuf buf, ShapelessRecipe recipe) {
		// ResourceLocation
		BukkitByteBufUtils.writeUTF8String(buf, recipe.getKey().toString());
		ItemStack result = recipe.getResult();
		// Result
		BukkitByteBufUtils.writeItemStack(buf, result);
		List<ItemStack> ings = recipe.getIngredientList();
		// Size of ingredient list
		buf.writeInt(ings.size());
		// Ingredient list, stack by stack
		for (ItemStack stack : ings)
			BukkitByteBufUtils.writeItemStack(buf, stack);
	}

	@SuppressWarnings("deprecation")
	private static NamespacedKey getKey(String string) {
		int colon = string.indexOf(':');
		String namespace = string.substring(0, colon);
		String key = string.substring(colon + 1);
		return new NamespacedKey(namespace, key);
	}

	private static String getRecipeType(Recipe recipe) {
		if (recipe instanceof ShapedRecipe)
			return GeyserRecipesInfo.SHAPED_RECIPE;
		if (recipe instanceof ShapelessRecipe)
			return GeyserRecipesInfo.SHAPELESS_RECIPE;
		return null;
	}

}
