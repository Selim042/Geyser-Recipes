package selim.geyserrecipes.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import selim.geyserrecipes.shared.GeyserRecipesInfo;

public class GeyserRecipesSpigot extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		PluginManager manager = this.getServer().getPluginManager();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, GeyserRecipesInfo.CHANNEL);
		manager.registerEvents(this, this);
	}

	private int getPing(Player player) {
		try {
			Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
			return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			this.getLogger().log(Level.INFO, "Unable to get ping for " + player.getDisplayName()
					+ ", encountered a " + e.getClass().getName());
			e.printStackTrace();
			return -1;
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		int ping = getPing(player);
		if (ping <= 0)
			ping = 40;
		else
			ping = ping / 25;
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				boolean sent = sendRecipes(player);
				if (!sent) {
					player.sendMessage("This server has " + GeyserRecipesInfo.NAME + " installed. "
							+ "If you install the client companion Forge mod, "
							+ "you can see any custom recipes from Spigot plugins installed on the server in JEI.");
					TextComponent base = new TextComponent("You can find the mod ");
					TextComponent link = new TextComponent("here.");
					link.setUnderlined(true);
					link.setColor(ChatColor.BLUE);
					link.setClickEvent(new ClickEvent(Action.OPEN_URL, GeyserRecipesInfo.DOWNLOAD_LINK));
					base.addExtra(link);
					player.spigot().sendMessage(base);
					// player.sendMessage("You can find the mod here: " +
					// GeyserRecipesInfo.DOWNLOAD_LINK);
				}
			}
		}, ping);
		// Player player = event.getPlayer();
		// @SuppressWarnings("deprecation")
		// ShapedRecipe originalRecipe = new
		// ShapedRecipe(NamespacedKey.randomKey(),
		// new ItemStack(Material.APPLE));
		// originalRecipe.shape("t");
		// originalRecipe.setIngredient('t', new MaterialData(Material.APPLE));
		// SpigotRecipeWrapperSpigot packet1 = new
		// SpigotRecipeWrapperSpigot(originalRecipe);
		// ByteBuf buf = Unpooled.buffer();
		// buf.writeByte(SpigotRecipesInfo.PacketDiscrimators.RECIPE.toByte());
		// packet1.toBytes(buf);
		// byte[] arr = buf.array();
		// System.out.println("START BUF CONTENTS");
		// String cont = "";
		// for (byte b : arr)
		// cont += b + " ";
		// System.out.println(cont);
		// System.out.println("END BUF CONTENTS");
		// player.sendPluginMessage(this, SpigotRecipesInfo.CHANNEL, arr);
		// SpigotRecipeWrapperSpigot packet2 = new SpigotRecipeWrapperSpigot();
		// buf.readByte();
		// packet2.fromBytes(buf);
		// ShapedRecipe newRecipe = (ShapedRecipe) packet2.getRecipe();
		// System.out.println(originalRecipe);
		// System.out.println(originalRecipe.getKey());
		// System.out.println(originalRecipe.getResult());
		// for (String row : originalRecipe.getShape())
		// System.out.println(row);
		// for (Entry<Character, ItemStack> e :
		// originalRecipe.getIngredientMap().entrySet())
		// System.out.println(e.getKey() + ": " + e.getValue());
		// System.out.println(newRecipe);
		// System.out.println(newRecipe.getKey());
		// System.out.println(newRecipe.getResult());
		// for (String row : newRecipe.getShape())
		// System.out.println(row);
		// for (Entry<Character, ItemStack> e :
		// newRecipe.getIngredientMap().entrySet())
		// System.out.println(e.getKey() + ": " + e.getValue());
		// System.out.println(originalRecipe.equals(newRecipe));
	}

	private boolean sendRecipes(Player player) {
		if (!player.getListeningPluginChannels().contains(GeyserRecipesInfo.CHANNEL))
			return false;
		player.sendPluginMessage(this, GeyserRecipesInfo.CHANNEL,
				new byte[] { GeyserRecipesInfo.PacketDiscrimators.CLEAR_RECIPES });
		List<Recipe> customRecipes = new LinkedList<>();
		Iterator<Recipe> allRecipes = Bukkit.recipeIterator();
		while (allRecipes.hasNext()) {
			Recipe r = allRecipes.next();
			NamespacedKey key = null;
			if (r instanceof ShapedRecipe)
				key = ((ShapedRecipe) r).getKey();
			else if (r instanceof ShapelessRecipe)
				key = ((ShapelessRecipe) r).getKey();
			if (key == null)
				continue;
			if (!key.getNamespace().equals(NamespacedKey.MINECRAFT))
				customRecipes.add(r);
		}
		for (Recipe r : customRecipes) {
			SpigotRecipeWrapperSpigot packet = new SpigotRecipeWrapperSpigot(r);
			ByteBuf buf = Unpooled.buffer();
			buf.writeByte(GeyserRecipesInfo.PacketDiscrimators.RECIPE);
			packet.toBytes(buf);
			player.sendPluginMessage(this, GeyserRecipesInfo.CHANNEL, buf.array());
		}
		return true;
	}

}
