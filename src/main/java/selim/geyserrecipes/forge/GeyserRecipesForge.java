package selim.geyserrecipes.forge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import selim.geyserrecipes.shared.GeyserRecipesInfo;

@Mod(modid = GeyserRecipesInfo.ID, name = GeyserRecipesInfo.NAME, version = GeyserRecipesInfo.VERSION,
		clientSideOnly = true)
public class GeyserRecipesForge {

	@Mod.Instance(value = GeyserRecipesInfo.ID)
	public static GeyserRecipesForge instance;
	public static final Logger LOGGER = LogManager.getLogger(GeyserRecipesInfo.ID);
	public static SimpleNetworkWrapper network;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(GeyserRecipesInfo.CHANNEL);
		network.registerMessage(SpigotRecipeWrapperForge.Handler.class, SpigotRecipeWrapperForge.class,
				GeyserRecipesInfo.PacketDiscrimators.RECIPE, Side.CLIENT);
		network.registerMessage(ClearRecipesPacket.Handler.class, ClearRecipesPacket.class,
				GeyserRecipesInfo.PacketDiscrimators.CLEAR_RECIPES, Side.CLIENT);
	}

	// @EventHandler
	// public void init(FMLInitializationEvent event) {
	// MinecraftForge.EVENT_BUS.register(this);
	// }

	@ObjectHolder(GeyserRecipesInfo.ID)
	public static class Items {

		public static final DummyItem DUMMY_ITEM = null;

	}

	@Mod.EventBusSubscriber
	public static class StupidRegistrySubclass {

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			event.getRegistry().register(new DummyItem());
		}

	}

}
