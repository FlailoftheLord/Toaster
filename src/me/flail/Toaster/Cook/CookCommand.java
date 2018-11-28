package me.flail.Toaster.Cook;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.flail.Toaster.Toaster;
import me.flail.Toaster.Tools;

public class CookCommand {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

	public void Cook(CommandSender sender, String command, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			FileConfiguration config = plugin.getConfig();

			String noPermission = config.getString("NoPermissionMessage");

			if (args.length == 0) {

				if (player.hasPermission("toaster.cook") || player.hasPermission("toaster.op")) {

					FileConfiguration itemConfig = plugin.getItemConfig();

					ConfigurationSection itemList = itemConfig.getConfigurationSection("Cookables");

					if (itemList != null) {

						Set<String> items = itemList.getKeys(false);

						for (String item : items) {

							String rawItem = itemList.getString(item + ".Item").toUpperCase();

							String cookedItem = itemList.getString(item + ".Result").toUpperCase();

							String expVariable = itemList.get(item + ".Exp").toString().toUpperCase();

							double cost = itemList.getDouble(item + ".Price");

							double exp = 0.0;

							if (expVariable.endsWith("L")) {

							}

						}

					}

				} else {
					player.sendMessage(tools.chat(noPermission, command, player));
				}

			}

		} else {
			plugin.console
					.sendMessage(tools.chat("&eLol, trying to cook food in the console?? ... smh", command, null));
		}

	}

}
