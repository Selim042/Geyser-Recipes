package selim.geyserrecipes.forge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import selim.geyserrecipes.shared.GeyserRecipesInfo;

public class SpigotRecipeWrapperForge implements IMessage {

	private IRecipe recipe;

	public SpigotRecipeWrapperForge() {}

	public SpigotRecipeWrapperForge(IRecipe recipe) {
		this.recipe = recipe;
	}

	public IRecipe getRecipe() {
		return this.recipe;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String type = getRecipeType(recipe);
		if (type == null)
			return;
		ByteBufUtils.writeUTF8String(buf, type);
		switch (type) {
		case GeyserRecipesInfo.SHAPED_RECIPE:
			writeShaped(buf, (ShapedOreRecipe) this.recipe);
			break;
		case GeyserRecipesInfo.SHAPELESS_RECIPE:
			writeShapeless(buf, (ShapelessOreRecipe) this.recipe);
			break;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// System.out.println("START BUF CONTENTS");
		// String cont = "";
		// for (int i = 0; i < buf.readableBytes(); i++)
		// cont += " " + buf.readByte();
		// System.out.println(cont);
		// System.out.println("END BUF CONTENTS");
		String type = ByteBufUtils.readUTF8String(buf);
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
		System.out.println("from bytes, recipe name: " + this.recipe.getRegistryName());
	}

	private static ShapedOreRecipe readShaped(ByteBuf buf) {
		// ResourceLocation
		ResourceLocation key = getKey(ByteBufUtils.readUTF8String(buf));
		// Result
		ItemStack result = ByteBufUtils.readItemStack(buf);
		// Write recipe name to result NBT
		NBTTagCompound nbt = result.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			result.setTagCompound(nbt);
		}
		nbt.setString(GeyserRecipesInfo.ID + ":recipe_name", key.toString());
		List<Object> recipeParams = new ArrayList<>();
		List<String> shapeList = new ArrayList<>();
		// Recipe shape, line by line
		int shapeLength = buf.readInt();
		for (int row = 0; row < shapeLength; row++)
			shapeList.add(ByteBufUtils.readUTF8String(buf));
		String[] shape = shapeList.toArray(new String[0]);
		// String[] shape = new String[] { "a c", " e ", "g " };
		// Size of ingredient map
		int size = buf.readInt();
		HashMap<Character, ItemStack> map = new HashMap<>();
		for (int e = 0; e < size; e += 2) {
			char c = buf.readChar();
			ItemStack stack = ByteBufUtils.readItemStack(buf);
			if (!stack.isEmpty()) {
				// recipeParams.add(c);
				// recipeParams.add(stack);
				map.put(c, stack);
			}
		}
		for (int lineNum = 0; lineNum < shapeLength; lineNum++) {
			String line = shape[lineNum];
			for (int chNum = 0; chNum < line.length(); chNum++) {
				char ch = line.charAt(chNum);
				if (!map.containsKey(ch)) {
					line = line.replace(ch, ' ');
					shape[lineNum] = line;
				}
			}
		}
		// Add shape to params after checking for ings
		String blankLine = getEmptyString(getShapedWidth(shape));
		for (int i = 0; i < 3 && i < shape.length; i++) {
			String row = shape[i];
			if (row.equals(""))
				recipeParams.add(blankLine);
			else
				recipeParams.add(row);
		}
		// Add ings after the shape params
		for (Entry<Character, ItemStack> e : map.entrySet()) {
			recipeParams.add(e.getKey());
			recipeParams.add(e.getValue());
		}

		ShapedOreRecipe recipe = new ShapedOreRecipe(null, result, recipeParams.toArray(new Object[0]));
		recipe.setRegistryName(key);
		return recipe;
	}

	// Should never have to call this, so it isn't properly implemented
	private static void writeShaped(ByteBuf buf, ShapedOreRecipe recipe) {
		// // ResourceLocation
		// ByteBufUtils.writeUTF8String(buf,
		// recipe.getRegistryName().toString());
		// ItemStack result = recipe.getRecipeOutput();
		// // Result
		// ByteBufUtils.writeItemStack(buf, result);
		// // Recipe shape, line by line
		// String[] shape = recipe.getShape();
		// buf.writeInt(shape.length);
		// for (int row = 0; row < shape.length; row++)
		// ByteBufUtils.writeUTF8String(buf, shape[row]);
		// Map<Character, ItemStack> map = recipe.getIngredientMap();
		// // Size of ingredient map
		// buf.writeInt(map.size());
		// // Ingredient map, stack by stack
		// for (Entry<Character, ItemStack> e : map.entrySet()) {
		// buf.writeChar(e.getKey());
		// ByteBufUtils.writeItemStack(buf, e.getValue());
		// }
	}

	private static int getShapedWidth(String[] rows) {
		int width = 0;
		for (String row : rows)
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

	private static ShapelessOreRecipe readShapeless(ByteBuf buf) {
		// ResourceLocation
		ResourceLocation key = getKey(ByteBufUtils.readUTF8String(buf));
		// Result
		ItemStack result = ByteBufUtils.readItemStack(buf);
		// Write recipe name to result NBT
		NBTTagCompound nbt = result.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			result.setTagCompound(nbt);
		}
		nbt.setString(GeyserRecipesInfo.ID + ":recipe_name", key.toString());
		// Size of ingredient list
		List<Object> ings = new ArrayList<>();
		int size = buf.readInt();
		for (int stack = 0; stack < size; stack++)
			ings.add(ByteBufUtils.readItemStack(buf));
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(null, result, ings.toArray(new Object[0]));
		recipe.setRegistryName(key);
		return recipe;
	}

	// Should never have to call this, so it isn't properly implemented
	private static void writeShapeless(ByteBuf buf, ShapelessOreRecipe recipe) {
		// // ResourceLocation
		// ByteBufUtils.writeUTF8String(buf,
		// recipe.getRegistryName().toString());
		// ItemStack result = recipe.getRecipeOutput();
		// // Result
		// ByteBufUtils.writeItemStack(buf, result);
		// List<ItemStack> ings = recipe.getIngredientList();
		// // Size of ingredient list
		// buf.writeInt(ings.size());
		// // Ingredient list, stack by stack
		// for (ItemStack stack : ings)
		// ByteBufUtils.writeItemStack(buf, stack);
	}

	private static ResourceLocation getKey(String string) {
		return new ResourceLocation(string);
	}

	private static String getRecipeType(IRecipe recipe) {
		if (recipe instanceof ShapedRecipe)
			return GeyserRecipesInfo.SHAPED_RECIPE;
		if (recipe instanceof ShapelessRecipe)
			return GeyserRecipesInfo.SHAPELESS_RECIPE;
		return null;
	}

	public static class Handler implements IMessageHandler<SpigotRecipeWrapperForge, IMessage> {

		@Override
		public IMessage onMessage(SpigotRecipeWrapperForge message, MessageContext ctx) {
			// System.out.println("recieved recipe: " +
			// message.getRecipe().getRegistryName());
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					JeiPlugin.addRecipe(message);
					Minecraft.getMinecraft().player
							.addItemStackToInventory(message.recipe.getRecipeOutput().copy());
				}
			});
			return null;
		}

	}

}
