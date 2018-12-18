package me.flail.Toaster;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Tools {

	private Toaster plugin;

	public Tools(Toaster plugin) {
		this.plugin = plugin;
	}

	public String chat(String msg, String cmd, Player player) {

		String reply = msg;

		FileConfiguration config = plugin.getConfig();

		String prefix = config.getString("Prefix");

		String eco = plugin.ecoPluginName;
		String version = plugin.version;
		String vaultVersion = plugin.vaultVersion;

		try {

			String pName = player.getName();
			reply = ChatColor.translateAlternateColorCodes('&',
					msg.replace("<player>", pName).replaceAll("<prefix>", prefix)
							.replaceAll("<vaultVersion>", vaultVersion).replaceAll("<version>", version)
							.replaceAll("<eco>", eco).replaceAll("<command>", cmd));

		} catch (IllegalArgumentException | NullPointerException e) {

			reply = ChatColor.translateAlternateColorCodes('&',
					msg.replace("<prefix>", prefix).replaceAll("<vaultVersion>", vaultVersion)
							.replaceAll("<version>", version).replaceAll("<eco>", eco).replaceAll("<command>", cmd));

		}

		return reply;

	}

	public String toasterChat(String msg, Player player, String cmd, String item, int itemAmount, String type) {

		String reply = msg;

		FileConfiguration config = plugin.getConfig();
		FileConfiguration itemConfig = plugin.getItemConfig();

		String prefix = config.getString("Prefix");

		String eco = plugin.ecoPluginName;
		String version = plugin.version;
		String vaultVersion = plugin.vaultVersion;

		if (type.equalsIgnoreCase("cook")) {

			ConfigurationSection itemSection = itemConfig.getConfigurationSection("Cookables" + item);

			String rawItem = itemSection.getString("Item");

			String result = itemSection.getString("Result");

			double price = itemSection.getDouble("Price");

			double cost = price * itemAmount;

			int exp = Integer.parseInt(itemSection.getString("Exp").toUpperCase().replaceAll("L", "")) * itemAmount;

			try {

				String pName = player.getName();
				reply = ChatColor.translateAlternateColorCodes('&',
						msg.replaceAll("<player>", pName).replaceAll("<prefix>", prefix)
								.replaceAll("<vaultVersion>", vaultVersion).replaceAll("<version>", version)
								.replaceAll("<eco>", eco).replaceAll("<command>", cmd).replaceAll("<item>", item)
								.replaceAll("<price>", price + "").replaceAll("<cost>", cost + "")
								.replaceAll("<result>", result).replaceAll("<exp>", exp + "")
								.replaceAll("<amount>", itemAmount + "").replaceAll("<raw-item>", rawItem));

			} catch (Exception e) {

				reply = ChatColor.translateAlternateColorCodes('&',
						msg.replaceAll("<prefix>", prefix).replaceAll("<vaultVersion>", vaultVersion)
								.replaceAll("<version>", version).replaceAll("<eco>", eco).replaceAll("<command>", cmd)
								.replaceAll("<item>", item).replaceAll("<price>", price + "")
								.replaceAll("<cost>", cost + "").replaceAll("<result>", result)
								.replaceAll("<exp>", exp + "").replaceAll("<amount>", itemAmount + "")
								.replaceAll("<raw-item>", rawItem));

			}

		} else if (type.equalsIgnoreCase("smelt")) {

			ConfigurationSection itemSection = itemConfig.getConfigurationSection("Smeltables" + item);

			String rawItem = itemSection.getString("Item");

			String result = itemSection.getString("Result");

			double price = itemSection.getDouble("Price");

			double cost = price * itemAmount;

			int exp = Integer.parseInt(itemSection.getString("Exp").toUpperCase().replaceAll("L", "")) * itemAmount;

			try {

				String pName = player.getName();
				reply = ChatColor.translateAlternateColorCodes('&',
						msg.replaceAll("<player>", pName).replaceAll("<prefix>", prefix)
								.replaceAll("<vaultVersion>", vaultVersion).replaceAll("<version>", version)
								.replaceAll("<eco>", eco).replaceAll("<command>", cmd).replaceAll("<item>", item)
								.replaceAll("<price>", price + "").replaceAll("<cost>", cost + "")
								.replaceAll("<result>", result).replaceAll("<exp>", exp + "")
								.replaceAll("<amount>", itemAmount + "").replaceAll("<raw-item>", rawItem));

			} catch (Exception e) {

				reply = ChatColor.translateAlternateColorCodes('&',
						msg.replaceAll("<prefix>", prefix).replaceAll("<vaultVersion>", vaultVersion)
								.replaceAll("<version>", version).replaceAll("<eco>", eco).replaceAll("<command>", cmd)
								.replaceAll("<item>", item).replaceAll("<price>", price + "")
								.replaceAll("<cost>", cost + "").replaceAll("<result>", result)
								.replaceAll("<exp>", exp + "").replaceAll("<amount>", itemAmount + "")
								.replaceAll("<raw-item>", rawItem));

			}

		} else if (type.equalsIgnoreCase("friend")) {

			ConfigurationSection itemSection = config.getConfigurationSection("Friend");

			String result = itemSection.getString("Item");

			double price = itemSection.getDouble("Price");

			double cost = price * itemAmount;

			int exp = Integer.parseInt(itemSection.getString("Exp").toUpperCase().replaceAll("L", "")) * itemAmount;

			try {

				String pName = player.getName();
				reply = ChatColor.translateAlternateColorCodes('&',
						msg.replaceAll("<player>", pName).replaceAll("<prefix>", prefix)
								.replaceAll("<vaultVersion>", vaultVersion).replaceAll("<version>", version)
								.replaceAll("<eco>", eco).replaceAll("<command>", cmd).replaceAll("<item>", item)
								.replaceAll("<price>", price + "").replaceAll("<cost>", cost + "")
								.replaceAll("<result>", result).replaceAll("<exp>", exp + "")
								.replaceAll("<amount>", itemAmount + "").replaceAll("<raw-item>", result));

			} catch (Exception e) {

				reply = ChatColor.translateAlternateColorCodes('&',
						msg.replaceAll("<prefix>", prefix).replaceAll("<vaultVersion>", vaultVersion)
								.replaceAll("<version>", version).replaceAll("<eco>", eco).replaceAll("<command>", cmd)
								.replaceAll("<item>", item).replaceAll("<price>", price + "")
								.replaceAll("<cost>", cost + "").replaceAll("<result>", result)
								.replaceAll("<exp>", exp + "").replaceAll("<amount>", itemAmount + "")
								.replaceAll("<raw-item>", result));

			}

		}

		return reply;
	}

}
