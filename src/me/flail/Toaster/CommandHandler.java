package me.flail.Toaster;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.flail.Toaster.Cook.CookCommand;

public class CommandHandler implements CommandExecutor {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String cmd = label.toLowerCase();

		switch (cmd) {

		case "toaster":

			break;
		case "cook":

			new CookCommand().Cook(sender, cmd, args);

			break;
		case "oven":

			sender.sendMessage(tools.chat("&elol", cmd, null));

			break;
		case "smelt":

			break;
		case "roast":

			break;
		case "toast":

			break;
		}

		return true;
	}

}