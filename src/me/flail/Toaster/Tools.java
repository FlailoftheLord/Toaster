package me.flail.Toaster;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Tools {

	private Toaster plugin;

	public Tools(Toaster plugin) {
		this.plugin = plugin;
	}

	public String chat(String msg, String cmd, Player player) {

		String reply = "toaster";

		FileConfiguration config = plugin.getConfig();

		String prefix = config.getString("Prefix");

		String eco = plugin.ecoPluginName;
		String version = plugin.version;
		String vaultVersion = plugin.vaultVersion;

		try {

			String pName = player.getName();
			reply = ChatColor.translateAlternateColorCodes('&',
					msg.replaceAll("<player>", pName).replaceAll("<prefix>", prefix)
							.replaceAll("<vaultVersion>", vaultVersion).replaceAll("<version>", version)
							.replaceAll("<eco>", eco).replaceAll("<command>", cmd));

		} catch (NullPointerException e) {

			reply = ChatColor.translateAlternateColorCodes('&',
					msg.replaceAll("<prefix>", prefix).replaceAll("<vaultVersion>", vaultVersion)
							.replaceAll("<version>", version).replaceAll("<eco>", eco).replaceAll("<command>", cmd));

		}

		return reply;

	}

}
