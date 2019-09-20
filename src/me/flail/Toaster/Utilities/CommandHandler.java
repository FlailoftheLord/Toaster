package me.flail.Toaster.Utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.flail.Toaster.Toaster;
import me.flail.Toaster.ToasterCommand;
import me.flail.Toaster.Cooker.CookCommand;
import me.flail.Toaster.Oven.OvenCommand;
import me.flail.Toaster.Roaster.RoastCommand;
import me.flail.Toaster.Smelter.SmeltCommand;

public class CommandHandler implements CommandExecutor {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

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
			new OvenCommand(plugin).execute(sender, args);

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
