package selim.geyser.recipes.forge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import selim.geyser.core.shared.EnumComponent;
import selim.geyser.core.shared.GeyserCoreInfo;
import selim.geyser.recipes.shared.GeyserRecipesInfo;

@Mod(modid = GeyserRecipesInfo.ID, name = GeyserRecipesInfo.NAME, version = GeyserRecipesInfo.VERSION,
		dependencies = "required-after:" + GeyserCoreInfo.ID,
		clientSideOnly = true)
public class GeyserRecipesForge {

	@Mod.Instance(value = GeyserRecipesInfo.ID)
	public static GeyserRecipesForge instance;
	public static final Logger LOGGER = LogManager.getLogger(GeyserRecipesInfo.ID);
	public static SimpleNetworkWrapper network;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(GeyserRecipesInfo.CHANNEL);
		network.registerMessage(SpigotRecipeWrapperForge.Handler.class, SpigotRecipeWrapperForge.class,
				GeyserRecipesInfo.PacketDiscrimators.RECIPE, Side.CLIENT);
		network.registerMessage(ClearRecipesPacket.Handler.class, ClearRecipesPacket.class,
				GeyserRecipesInfo.PacketDiscrimators.CLEAR_RECIPES, Side.CLIENT);

		FMLInterModComms.sendMessage(GeyserCoreInfo.ID,
				GeyserCoreInfo.ID + ":components:" + GeyserCoreInfo.ID,
				EnumComponent.RECIPES.toString());
	}

	// @EventHandler
	// public void init(FMLInitializationEvent event) {
	// MinecraftForge.EVENT_BUS.register(this);
	// }

	// @ObjectHolder(GeyserRecipesInfo.ID + ":dummy_item")
	// public static final DummyItem DUMMY_ITEM = null;
	//
	// @Mod.EventBusSubscriber
	// public static class StupidRegistrySubclass {
	//
	// @SubscribeEvent
	// public static void registerItems(RegistryEvent.Register<Item> event) {
	// event.getRegistry().register(new DummyItem());
	// }
	//
	// }

}
