/* Toaster: the ultimate Toaster, which shall soon take over minecraft as we know it today.
    Copyright (C) 2018  FlailoftheLord

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */

package me.flail.Toaster;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Toaster extends JavaPlugin {

	public Tools tools = new Tools(this);
	public ConsoleCommandSender console = Bukkit.getConsoleSender();
	public PluginManager plugin = Bukkit.getPluginManager();
	public Server server = this.getServer();

	public Map<Player, Integer> cooldown = new HashMap<>();

	private Economy eco;

	private File itemConfigFile;
	private FileConfiguration itemConfig;

	private File ovenGuiFile;
	private FileConfiguration ovenGuiConfig;

	public String version = this.getDescription().getVersion();

	public String ecoPluginName = "Unknown";
	public String vaultVersion = "Unknown";

	public Economy getEconomy() {
		return eco;
	}

	private boolean setupEconomy() {
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

		if ((plugin.getPlugin("Vault") != null) && plugin.getPlugin("Vault").isEnabled()) {

			if (!setupEconomy()) {

				ecoPluginName = tools.chat(
						"&4&lToaster could not find an economy plugin!! \nIf you want to enable the Economy features of Toaster, you will need an economy plugin.\n Please install one and restart the server!",
						"", null);

			}

			// Load up files
			saveDefaultConfig();
			loadOvenGui();
			loadItemConfig();

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

		} else {
			getLogger().severe("Toaster Disabled, because Vault Was not Found!");
			getLogger().severe("You can download the Latest version of Vault here:");
			getLogger().severe("https://www.spigotmc.org/resources/vault.34315/");

			Bukkit.getPluginManager().disablePlugin(this);
		}

	}

	@Override
	public void onDisable() {
		console.sendMessage(" ");
		console.sendMessage(tools.chat("Toast you later!", "", null));
		console.sendMessage(" ");
	}

	public void doReload() {
		reloadConfig();
		loadOvenGui();
		loadItemConfig();
	}

	// JUST Register those commanddss!! yas!
	private void registerCommands() {

		Set<String> commands = this.getDescription().getCommands().keySet();

		for (String cmd : commands) {

			if (cmd != null) {
				getCommand(cmd).setExecutor(new CommandHandler());
			}
		}

	}

	public FileConfiguration getItemConfig() {
		return itemConfig;
	}

	public void loadItemConfig() {
		itemConfigFile = new File(getDataFolder(), "ItemConfig.yml");
		if (!itemConfigFile.exists()) {
			itemConfigFile.getParentFile().mkdirs();
			saveResource("ItemConfig.yml", false);
		}

		itemConfig = new YamlConfiguration();
		try {
			itemConfig.load(itemConfigFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public FileConfiguration getOvenGui() {
		if (ovenGuiConfig == null) {
			loadOvenGui();
		}
		return ovenGuiConfig;
	}

	public void loadOvenGui() {

		ovenGuiFile = new File(getDataFolder(), "OvenGui.yml");
		if (!ovenGuiFile.exists()) {
			ovenGuiFile.getParentFile().mkdirs();
			saveResource("OvenGui.yml", false);
		}
		ovenGuiConfig = new YamlConfiguration();
		try {
			ovenGuiConfig.load(ovenGuiFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
