package selim.geyser.recipes.bukkit;

import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtType;

import selim.geyser.recipes.shared.IItemStack;

public class SpigotItemStack implements IItemStack {

	private final ItemStack stack;

	public SpigotItemStack(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public String getItemNamespace() {
		return MiscUtils.getNamespaceForMat(stack.getType());
	}

	@Override
	public String getItemKey() {
		return MiscUtils.getKeyForMat(stack.getType());
	}

	@Override
	public int getCount() {
		return stack.getAmount();
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getMetadata() {
		return stack.getData().getData();
	}

	@Override
	public Object getNbt() {
		return NbtFactory
				.ofWrapper(NbtType.TAG_COMPOUND, "", (NbtCompound) NbtFactory.fromItemTag(stack))
				.getHandle();
	}

}
