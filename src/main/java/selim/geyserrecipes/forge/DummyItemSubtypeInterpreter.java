package selim.geyserrecipes.forge;

import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;


public class DummyItemSubtypeInterpreter implements ISubtypeInterpreter {

	@Override
	public String apply(ItemStack stack) {
		ItemStack wrappedStack = DummyItem.getWrappedStack(stack);
		return null;
	}

}
