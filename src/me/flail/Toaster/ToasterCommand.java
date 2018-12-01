package me.flail.Toaster;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ToasterCommand {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

	public void toasterCommand(CommandSender sender, String command, String[] args) {

		FileConfiguration config = plugin.getConfig();

		if (sender instanceof Player) {

			Player player = (Player) sender;

			String noPermission = tools.chat(config.getString("NoPermissionMessage"), command, player);

			String reloadMessage = tools.chat(config.getString("ReloadMessage"), command, player);

			if (command.equals("toaster")) {

			}

		}

	}

}
