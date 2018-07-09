package selim.geyser.recipes.shared;

import io.netty.buffer.ByteBuf;

public interface ISpigotRecipe {

	public void toBytes(ByteBuf buf);

	public void fromBytes(ByteBuf buf);

}
