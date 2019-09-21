package me.flail.toaster.Oven;

import org.bukkit.command.CommandSender;

import me.flail.toaster.Utilities.Logger;

public class OvenCommand extends Logger {

	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("toaster.oven")) {
			sender.sendMessage(chat(plugin.getConfig().getString("NoPermission")));

			return;
		}

	}

}
