package me.flail.toaster.Utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.flail.toaster.ToasterCommand;
import me.flail.toaster.Cooker.CookCommand;
import me.flail.toaster.Oven.OvenCommand;
import me.flail.toaster.Roaster.RoastCommand;
import me.flail.toaster.Smelter.SmeltCommand;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String cmd = label.toLowerCase();

		switch (cmd) {

		case "toaster":

			new ToasterCommand().toasterCommand(sender, cmd, args);

			break;
		case "cook":

			new CookCommand().Cook(sender, cmd, args);

			break;
		case "oven":
			new OvenCommand().execute(sender, args);

			break;
		case "smelt":

			new SmeltCommand().Smelt(sender, cmd, args);

			break;
		case "roast":
			if (sender instanceof Player) {
				new RoastCommand().Roast((Player) sender);
			}

			break;
		case "toast":

			break;
		}

		return true;
	}

}
