package me.flail.toaster.Oven;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.flail.toaster.Tools.DataFile;
import me.flail.toaster.Utilities.Logger;

public class OvenGui extends Logger {

	private Location location;
	private Inventory inventory;
	private DataFile settings;

	public OvenGui(Location ovenLoc) {
		location = ovenLoc;
		settings = Oven.settings();

		generate();
	}

	public void open(Player player) {

	}

	protected void generate() {
		inventory = Bukkit.createInventory(null, 45, chat(settings.getValue("OvenUI.Name")));

	}

}
