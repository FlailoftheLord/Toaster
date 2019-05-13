package me.flail.Toaster.Oven;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OvenGui {

	private final UUID uuid;
	private Inventory gui;

	public OvenGui(int size, String name) {
		uuid = UUID.randomUUID();
		gui = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', name));
	}

	public UUID getId() {
		return uuid;
	}

	public Inventory get() {
		return gui;
	}

	public void setItem(ItemStack item, int slot) {
		gui.setItem(slot, item);
	}

}
