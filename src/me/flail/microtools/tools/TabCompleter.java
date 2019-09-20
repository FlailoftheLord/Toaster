package me.flail.microtools.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;


public class TabCompleter extends Logger {

	private Command command;

	public TabCompleter(Command command) {

		this.command = command;
	}

	public List<String> construct(String label, String[] args) {
		List<String> baseArgs = new ArrayList<>();
		if (!command.getName().equalsIgnoreCase("toaster")) {
			return baseArgs;
		}

		switch (args.length) {

		}

		for (String s : baseArgs.toArray(new String[] {})) {
			if (!s.startsWith(args[args.length - 1].toLowerCase())) {

				baseArgs.remove(s);
			}

		}

		return baseArgs;
	}

	public List<String> usernames() {
		List<String> list = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			list.add(player.getName());
		}

		return list;
	}

}
