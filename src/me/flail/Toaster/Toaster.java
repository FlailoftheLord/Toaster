/**
 * Toaster: the ultimate Toaster, which shall soon take over minecraft as we know it today.
 * Copyright (C) 2018 FlailoftheLord
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.flail.Toaster;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.Toaster.Utilities.CommandHandler;
import me.flail.Toaster.Utilities.ItemLoader;
import me.flail.Toaster.Utilities.Tools;
import net.milkbowl.vault.economy.Economy;

public class Toaster extends JavaPlugin {
	public static Toaster toaster;

	public Tools tools = new Tools(this);
	public ConsoleCommandSender console = Bukkit.getConsoleSender();
	public PluginManager plugin = Bukkit.getPluginManager();
	public Server server = this.getServer();

	public Map<UUID, Integer> cooldown = new HashMap<>();
	public final Map<String, Map<String, Object>> cookables = new LinkedHashMap<>(32);
	public final Map<String, Map<String, Object>> smeltables = new LinkedHashMap<>(32);

	public enum ToasterType {
		COOK, SMELT, ROAST, BAKE
	}

	private Economy eco;

	public String version = this.getDescription().getVersion();

	public String ecoPluginName = "Unknown";
	public String vaultVersion = "Unknown";

	public Economy getEconomy() {
		return eco;
	}

	protected boolean setupEconomy() {
		if (plugin.getPlugin("Vault") == null) {
			return false;
		}

		RegisteredServiceProvider<Economy> vaultEconomy = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (vaultEconomy == null) {
			return false;
		}
		eco = vaultEconomy.getProvider();

		vaultVersion = Bukkit.getPluginManager().getPlugin("Vault").getDescription().getVersion();

		ecoPluginName = eco.getName();

		return eco != null;
	}

	@Override
	public void onEnable() {
		toaster = this;
		cooldown.clear();

		if ((plugin.getPlugin("Vault") != null) && plugin.getPlugin("Vault").isEnabled()) {

			if (!setupEconomy()) {

				ecoPluginName = tools.chat(
						"&4&lToaster could not find an economy plugin!! \nIf you want to enable the Economy features of Toaster, you will need an economy plugin.\n Please install one and restart the server!",
						"", null);

			}

			// Load up files
			saveDefaultConfig();
			loadFile("ItemConfig");

			cookables.clear();
			smeltables.clear();
			new ItemLoader().loadItems("cookables");
			new ItemLoader().loadItems("smeltables");

			// Register Commands
			registerCommands();

			// Register Events

			// Friendly console spam :')
			console.sendMessage(" ");
			console.sendMessage(tools.chat("&aSuccessfully hooked into Vault <vaultVersion>", "", null));
			console.sendMessage(tools.chat("&aEconomy Plugin&7: &f<eco>", "", null));
			console.sendMessage(" ");
			console.sendMessage(tools.chat("&8-----=-----||-----=-----", "", null));
			console.sendMessage(tools.chat(" &eToaster version <version>", "", null));
			console.sendMessage(tools.chat("   &2by FlailoftheLord", "", null));
			console.sendMessage(tools.chat("  &eToasty times call for toasty measures!", "", null));
			console.sendMessage(tools.chat("&8-----=-----||-----=-----", "", null));

			server.getScheduler().scheduleSyncRepeatingTask(this, () -> {
				for (UUID uuid : cooldown.keySet()) {
					cooldown.put(uuid, Integer.valueOf(cooldown.get(uuid).intValue() - 1));
				}

			}, 20L, 32L);

		} else {
			getLogger().severe("Toaster Disabled, because Vault Was not Found!");
			getLogger().severe("You can download the Latest version of Vault here:");
			getLogger().severe("https://www.spigotmc.org/resources/vault.34315/");

			Bukkit.getPluginManager().disablePlugin(this);
		}


	}

	@Override
	public void onDisable() {
		cooldown.clear();

		console.sendMessage(" ");
		console.sendMessage(tools.chat("Toast you later!", "", null));
		console.sendMessage(" ");
	}

	public void doReload() {
		cookables.clear();
		smeltables.clear();
		new ItemLoader().loadItems("cookables");
		new ItemLoader().loadItems("smeltables");

		reloadConfig();
		this.loadFile("ItemConfig");

	}

	// JUST Register those commanddss!! yas!
	private void registerCommands() {
		Set<String> commands = this.getDescription().getCommands().keySet();
		for (String cmd : commands) {
			getCommand(cmd).setExecutor(new CommandHandler());
		}

	}

	@Deprecated
	public FileConfiguration getItemConfig() {
		return this.getFile("ItemConfig");
	}

	@Deprecated
	public void loadItemConfig() {
		this.loadFile("ItemConfig");
	}

	@Deprecated
	public FileConfiguration getOvenGui() {
		return this.getFile("OvenGui");
	}

	@Deprecated
	public void loadOvenGui() {
		this.loadFile("OvenGui");
	}

	public FileConfiguration getFile(String name) {
		File file = new File(getDataFolder(), name + ".yml");
		if (!file.exists()) {
			this.loadFile(name);
		}
		new YamlConfiguration();
		return YamlConfiguration.loadConfiguration(file);
	}

	public void loadFile(String name) {
		File file = new File(getDataFolder(), name + ".yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			saveResource(name + ".yml", false);
		}
		FileConfiguration config = new YamlConfiguration();
		if (name.equals("ItemConfig")) {
			config.options().header(
					"#===============================================================================================================#\r\n"
							+
							"# Here you can customize what items toaster can cook.\r\n" +
							"# if you want to add a new item simply create a friendly name as the title\r\n" +
							"# then add the Item: \"\" with its appropriate BUKKIT item  name which you can get from here:\r\n" +
							"#           https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html\r\n" +
							"# -\r\n" +
							"# then add the price which is how much the player will be charged for converting ONE of this item.\r\n"
							+
							"# then set the Exp amount which the player will be given per item, if you want to use levels\r\n" +
							"# simpy set it as Exp: 3.0L instead of Exp: 3.0 \r\n" +
							"# and finally set the resulting item type, just follow this template and you will be fine!\r\n" +
							"# FancyFood:\r\n" +
							"#   Item: \"GRASS_BLOCK\"\r\n" +
							"#   Price: 12.57\r\n" +
							"#   Exp: 2\r\n" +
							"#   Result: \"GOLD_BLOCK\"\r\n" +
							"# -\r\n" +
							"# And finally to set permissions for an item.\r\n" +
							"# use  'toaster.item.<whatever-you-want-here>'\r\n" +
							"# You can also apply the permission 'toaster.item.all'\r\n" +
							"# which will allow the conversion of all items from both /smelt & /cook\r\n" +
					"#================================================================================================================#\r\n");
		}

		try {
			config.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
