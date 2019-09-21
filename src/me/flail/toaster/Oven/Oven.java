package me.flail.toaster.Oven;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import me.flail.toaster.Tools.DataFile;
import me.flail.toaster.Utilities.Logger;

@SuppressWarnings("unchecked")
public class Oven extends Logger {

	private Location location;
	private static DataFile ovenData;

	public Oven(Location location) {
		this.location = location;
		ovenData = new DataFile(location.getWorld().getName() + "/oven-data.yml", true);
	}

	public static DataFile settings() {
		return plugin.ovenSettings;
	}

	public static ItemStack newOvenItem() {
		return OvenItem.newItem();
	}

	public Location getLocation() {
		return location;
	}

	public OvenGui getInventory() {

		return new OvenGui(location);
	}

	public void save() {
		Set<Location> ovens = new HashSet<>();

		if (ovenData.hasValue("Ovens")) {
			ovens.addAll((Set<Location>) ovenData.getObj("Ovens"));
		}

		if (!ovens.contains(location)) {
			ovens.add(location);
		}

		ovenData.setValue("Ovens", ovens);
	}

	public void remove() {
		Set<Location> ovens = new HashSet<>();

		if (ovenData.hasValue("Ovens")) {
			ovens.addAll((Set<Location>) ovenData.getObj("Ovens"));
		}

		if (ovens.contains(location)) {
			ovens.remove(location);
		}

		ovenData.setValue("Ovens", ovens);

	}

	public static void loadOvens(World world) {
		ovenData = new DataFile(world.getName() + "/oven-data.yml", true);

		if (ovenData.hasValue("Ovens")) {

			plugin.activeOvens.put(world, (Set<Location>) ovenData.getObj("Ovens"));
			return;
		}

		plugin.activeOvens.remove(world);
	}

}
