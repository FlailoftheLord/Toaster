package me.flail.Toaster.Roaster;

import org.bukkit.entity.Player;

import me.flail.Toaster.Toaster;
import me.flail.Toaster.Utilities.Tools;

public class RoastCommand {
	Toaster plugin = Toaster.toaster;

	public boolean Roast(Player player) {
		if (player.hasPermission("toaster.roast")) {
			player.sendMessage(new Tools(plugin).chat("<prefix> &cUhm... sorry, can't get roasted yet!", "roast", player));
		} else {
			player.sendMessage(new Tools(plugin).chat(plugin.getConfig().get("NoPermissionMessage").toString(), "roast", player));
		}

		return true;
	}

}
