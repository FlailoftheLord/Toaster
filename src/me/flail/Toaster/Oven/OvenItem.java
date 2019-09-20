package me.flail.Toaster.Oven;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.flail.microtools.tools.DataFile;
import me.flail.microtools.tools.Logger;

public class OvenItem extends Logger {

	protected OvenItem() {
	}

	public static ItemStack newItem() {
		ItemStack ovenItem = new ItemStack(Material.BLAST_FURNACE);

		DataFile settings = new DataFile("Oven.yml");
		if (settings.hasValue("OvenItem.item")) {
			try {

				ovenItem = new ItemStack(Material.matchMaterial(settings.getValue("OvenItem.item")));
			} catch (Exception e) {}
		}

		ItemMeta meta = ovenItem.getItemMeta();

		return ovenItem;
	}

}