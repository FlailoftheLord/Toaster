package me.flail.toaster.Utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.flail.toaster.Toaster;
import me.flail.toaster.Toaster.ToasterType;

public class ItemLoader {
	Toaster toaster;
	FileConfiguration itemConfig;

	public ItemLoader() {
		toaster = Toaster.toaster;
		itemConfig = toaster.getFile("ItemConfig");
	}

	public void loadItems(String toasterType) {
		ToasterType type = null;
		String key = "";
		for (String mainKey : itemConfig.getKeys(false)) {
			if (toasterType.equalsIgnoreCase("cookables") && mainKey.equalsIgnoreCase("cookables")) {
				type = ToasterType.COOK;
				key = mainKey;
				break;
			} else if (toasterType.equalsIgnoreCase("smeltables") && mainKey.equalsIgnoreCase("smeltables")) {
				type = ToasterType.SMELT;
				key = mainKey;
				break;
			}

			if (type != null) {
				break;
			}
		}

		if (!key.isEmpty() || (key != "")) {
			ConfigurationSection typeSection = itemConfig.getConfigurationSection(key);
			for (String mainKey : typeSection.getKeys(false)) {
				Map<String, Object> value = new HashMap<>();
				for (String itemKey : typeSection.getConfigurationSection(mainKey).getKeys(false)) {
					value.put(itemKey.toLowerCase(), typeSection.get(mainKey + "." + itemKey));
				}

				switch (type) {
				case COOK:
					toaster.cookables.put(mainKey, value);
					break;
				case SMELT:
					toaster.smeltables.put(mainKey, value);
					break;
				default:
					;
				}
			}
			return;
		}


		toaster.console.sendMessage(toaster.tools.chat(
				"&cERROR while initializing item names from config, please check that 'ItemConfig.yml' loaded properly, and that you have set the appropriate values.",
				"", null));
	}

}
