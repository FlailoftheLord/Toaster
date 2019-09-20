package me.flail.Toaster.Oven;

import org.bukkit.command.CommandSender;

import me.flail.microtools.tools.Logger;

public class OvenCommand extends Logger {

	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("toaster.oven")) {
			sender.sendMessage(chat(plugin.getConfig().getString("NoPermission")));

			return;
		}

	}

}
