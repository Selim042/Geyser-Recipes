package selim.geyser.recipes.forge;

import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import selim.geyser.recipes.shared.GeyserRecipesInfo;

public class GeyserRecipeSubtypeInterpreter implements ISubtypeInterpreter {

	@Override
	public String apply(ItemStack stack) {
		ItemStack wrappedStack = DummyItem.getWrappedStack(stack);
		NBTTagCompound nbt = wrappedStack.getTagCompound();
		if (nbt == null)
			return NONE;
		System.out.println("sorting by: " + nbt.getString(GeyserRecipesInfo.ID + ":recipe_name"));
		return nbt.getString(GeyserRecipesInfo.ID + ":recipe_name");
	}

}
