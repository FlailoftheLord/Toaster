package me.flail.Toaster.Utilities;

import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.flail.Toaster.Toaster;

public class Tools {

	private Toaster plugin;

	public Tools(Toaster plugin) {
		this.plugin = plugin;
	}

	public String chat(String msg, String cmd, Player player) {

		String reply = msg;

		FileConfiguration config = plugin.getConfig();

		String prefix = config.getString("Prefix", "&e&lToaster&8:");
		String pName = "";

		String eco = plugin.ecoPluginName;
		String version = plugin.version;
		String vaultVersion = plugin.vaultVersion;

		if (cmd == null) {
			cmd = "";
		}

		if (player != null) {
			pName = player.getName();
		}

		reply = ChatColor.translateAlternateColorCodes('&',
				msg.replace("<player>", pName).replaceAll("<prefix>", prefix)
				.replaceAll("<vaultVersion>", vaultVersion).replaceAll("<version>", version)
				.replaceAll("<eco>", eco).replaceAll("<command>", cmd));

		return reply;

	}

	public String toasterChat(String msg, Player player, String cmd, Map<String, Object> item, int itemAmount, String type) {

		String reply = msg;

		FileConfiguration config = plugin.getConfig();

		String prefix = config.getString("Prefix");

		String eco = plugin.ecoPluginName;
		String version = plugin.version;
		String vaultVersion = plugin.vaultVersion;

		switch (type.toLowerCase()) {
		case "cook":
			type = "Cookables";
			break;
		case "smelt":
			type = "Smeltables";
			break;
		case "friend":
			type = "Friend";
			break;

		}

		String rawItem = player.getInventory().getItemInMainHand().getType().toString().toLowerCase();
		String result = "";
		String permission = "";

		if (type.equalsIgnoreCase("friend")) {
			result = "";
		}

		double price = 0;

		if (type.equalsIgnoreCase("friend")) {
			price = plugin.getConfig().getDouble("Friend.Cost", 0);
		}

		double cost = price * itemAmount;

		int exp = 0 * itemAmount;

		String pName = "";
		if (player != null) {
			pName = player.getName();
		}

		if (item != null) {
			rawItem = item.get("item").toString();
			result = item.get("result").toString();
			price = Double.parseDouble(item.get("price").toString().replaceAll("[a-zA-Z]", ""));
			exp = Integer.parseInt(item.get("exp").toString().replaceAll("[^0-9]", ""));
			if (item.containsKey("permission")) {
				permission = item.get("permission").toString();
			}
		}


		reply = ChatColor.translateAlternateColorCodes('&',
				msg.replaceAll("<player>", pName).replaceAll("<prefix>", prefix)
				.replaceAll("<vaultVersion>", vaultVersion).replaceAll("<version>", version)
				.replaceAll("<eco>", eco).replaceAll("<command>", cmd).replaceAll("<permission>", permission)
				.replaceAll("<item>", rawItem).replaceAll("<price>", price + "").replaceAll("<cost>", cost + "")
				.replaceAll("<result>", result).replaceAll("<exp>", exp + "")
						.replaceAll("<amount>", itemAmount + "").replaceAll("<cooldown>", itemAmount + "")
						.replaceAll("<raw-item>", rawItem));

		return reply;
	}

	public String itemName(String defaultName) {
		String name = "";
		for (String s : defaultName.split("_")) {
			String firstChar = "" + s.charAt(0);

			name = name.concat(s.toLowerCase().replaceFirst(Pattern.quote("(?i)") + firstChar, firstChar.toUpperCase()));
		}

		return name;
	}

	public Map<String, Object> getItemSection(String rawItem, String type) {
		Map<String, Map<String, Object>> keys;
		if (type.contains("cook")) {
			keys = plugin.cookables;
		} else {
			keys = plugin.smeltables;
		}

		for (String itemSection : keys.keySet()) {
			Map<String, Object> itemSet = keys.get(itemSection);
			String item = itemSet.get("item").toString();
			if (item.equalsIgnoreCase(rawItem)) {
				return itemSet;
			}

		}

		return null;
	}

	public Material convertItem(String itemName, String type) {
		Map<String, Object> itemSet = this.getItemSection(itemName, type);

		if (itemSet != null) {
			String result = itemSet.get("result").toString();
			Material resultType = Material.matchMaterial(result);
			if (resultType != null) {
				return resultType;
			}

		}

		return null;
	}

}
