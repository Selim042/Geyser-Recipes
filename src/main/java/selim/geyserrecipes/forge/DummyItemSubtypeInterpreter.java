package selim.geyserrecipes.forge;

import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import selim.geyserrecipes.shared.GeyserRecipesInfo;

public class DummyItemSubtypeInterpreter implements ISubtypeInterpreter {

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
