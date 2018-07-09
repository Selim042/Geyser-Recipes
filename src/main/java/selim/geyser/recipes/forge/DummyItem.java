package selim.geyser.recipes.forge;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.geyser.recipes.shared.GeyserRecipesInfo;

public class DummyItem extends Item {

	public DummyItem() {
		this.setRegistryName(GeyserRecipesInfo.ID, "dummy_item");
		this.setUnlocalizedName(GeyserRecipesInfo.ID + ":dummy_item");
		this.setTileEntityItemStackRenderer(new DummyItemRenderer());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		ItemStack wrappedStack = getWrappedStack(stack);
		wrappedStack.getItem().addInformation(wrappedStack, worldIn, tooltip, flagIn);
		NBTTagCompound nbt = stack.getOrCreateSubCompound("wrapped");
		for (String key : nbt.getKeySet())
			tooltip.add(key);
		// NBTTagCompound nbt = wrappedStack.getTagCompound();
		// if (nbt == null)
		// return NONE;
		// System.out.println("sorting by: " +
		// nbt.getString(GeyserRecipesInfo.ID + ":recipe_name"));
		tooltip.add(nbt.getString(GeyserRecipesInfo.ID + ":recipe_name"));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		ItemStack wrappedStack = getWrappedStack(stack);
		return wrappedStack.getItem().getUnlocalizedName(wrappedStack);
	}

	@Override
	public String getCreatorModId(ItemStack stack) {
		ItemStack wrappedStack = getWrappedStack(stack);
		return wrappedStack.getItem().getCreatorModId(wrappedStack);
	}

	public static ItemStack getWrappedStack(ItemStack stack) {
		if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof DummyItem)
				|| !stack.hasTagCompound())
			return ItemStack.EMPTY;
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return ItemStack.EMPTY;
		NBTTagCompound wrappedNbt = nbt.getCompoundTag("wrapped");
		if (wrappedNbt == null)
			return ItemStack.EMPTY;
		return new ItemStack(wrappedNbt);
	}

	// Commented because I disabled the dummy item
	// public static ItemStack getWrapperStack(ItemStack stack) {
	// if (stack == null || stack.isEmpty())
	// return ItemStack.EMPTY;
	// NBTTagCompound wrappedNbt = stack.serializeNBT();
	// ItemStack dummyStack = new ItemStack(GeyserRecipesForge.DUMMY_ITEM);
	// NBTTagCompound dummyNbt = dummyStack.getTagCompound();
	// if (dummyNbt == null) {
	// dummyNbt = new NBTTagCompound();
	// dummyStack.setTagCompound(dummyNbt);
	// }
	// dummyNbt.setTag("wrapped", wrappedNbt);
	// return dummyStack;
	// }

}
