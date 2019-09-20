package me.flail.Toaster.Roaster;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.flail.Toaster.Toaster;
import me.flail.microtools.tools.Logger;

public class RoastCommand extends Logger {
	Toaster plugin = Toaster.toaster;

	public boolean Roast(Player player) {
		if (player.hasPermission("toaster.roast")) {
			if (player.getInventory().firstEmpty() >= 0) {
				ItemStack steakItem = new ItemStack(Material.COOKED_BEEF);
				ItemMeta steakMeta = steakItem.getItemMeta();

				steakMeta.setDisplayName(chat("&6&lRoast Steak"));
				steakMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

				List<String> steakLore = new ArrayList<>();
				steakLore.add(" ");
				steakLore.add(chat("&7A slice of steak"));
				steakLore.add(chat("&7from King Steven's larder."));
				steakLore.add("");

				steakMeta.setLore(steakLore);
				steakItem.setItemMeta(steakMeta);

				player.getInventory().addItem(steakItem);

				player.sendMessage(chat("&2&lYou got a fresh roast steak!"));
				return true;
			}

			player.sendMessage(chat("&e&lYou are too full to eat another slice of steak..."));
			return true;
		} else {
			player.sendMessage(chat("&c&lYou were caught trying to steal a slab of beef from King Steven's storehouse!"));
			player.damage(0.1);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 6, true));
		}

		return true;
	}

}
