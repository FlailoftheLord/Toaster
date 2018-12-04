package me.flail.Toaster;

import org.bukkit.ChatColor;
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

			String defaultMessage = tools.chat(
					"<prefix> &7Running &e&lToaster &r&eversion &6<version> &2by FlailoftheLord.", command, player);

			String about = tools.chat(
					"<prefix> &6the new, ultimate Toaster, which shall take over minecraft as we know it today!",
					command, player);

			String usage = tools.chat("<prefix> &cProper usage: &7/<command> [reload:about]", command, player);

			if (command.equals("toaster")) {

				if (player.hasPermission("toaster.command")) {

					if (args.length == 1) {

						if (args[0].equalsIgnoreCase("reload")) {

							if (player.hasPermission("toaster.reload")) {

								plugin.reloadConfig();
								plugin.loadItemConfig();
								plugin.loadOvenGui();

								player.sendMessage(reloadMessage);

							} else {
								player.sendMessage(noPermission);
							}

						} else if (args[0].equalsIgnoreCase("about")) {
							player.sendMessage(about);
						} else {
							player.sendMessage(usage);
						}

					} else {
						player.sendMessage(defaultMessage);
					}

				} else {
					player.sendMessage(defaultMessage);
				}

			}

		} else {

			if (command.equals("toaster")) {

				plugin.console.sendMessage(ChatColor.GOLD + "WHat are you tryin to accomplish in console... o.O?");

			}

		}

	}

}
