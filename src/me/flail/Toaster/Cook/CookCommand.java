package me.flail.Toaster.Cook;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.flail.Toaster.Toaster;
import me.flail.Toaster.Tools;
import net.milkbowl.vault.economy.Economy;

public class CookCommand {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

	private Economy eco = plugin.getEconomy();

	public void Cook(CommandSender sender, String command, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			FileConfiguration config = plugin.getConfig();

			String noPermission = config.getString("NoPermissionMessage");

			if (args.length == 0) {

				if (player.hasPermission("toaster.cook") || player.hasPermission("toaster.op")) {

					FileConfiguration itemConfig = plugin.getItemConfig();

					ConfigurationSection itemList = itemConfig.getConfigurationSection("Cookables");

					if (itemList != null) {

						Set<String> items = itemList.getKeys(false);

						ItemStack pHand = player.getInventory().getItemInMainHand();

						String pItemName = pHand.getType().toString();

						double playerBalance = eco.getBalance(player);

						double maxWithdraw = config.getDouble("MaxMoneyWithdraw");

						int pItemAmount = pHand.getAmount();

						boolean validItem = true;

						for (String item : items) {

							String rawItem = itemList.getString(item + ".Item").toUpperCase();

							String cookedItem = itemList.getString(item + ".Result").toUpperCase();

							String expVariable = itemList.get(item + ".Exp").toString().toUpperCase();

							double cost = itemList.getDouble(item + ".Price");

							String cookSuccess = tools.chat(
									config.getString("Cook.Success").replaceAll("<item>", item)
											.replaceAll("<result>", cookedItem).replaceAll("<amount>", pItemAmount + "")
											.replaceAll("<cost>", (cost * pItemAmount) + "")
											.replaceAll("<price>", cost + "").replaceAll("<exp>", expVariable),
									command, player);

							String cookNoMoney = tools.chat(config.getString("Cook.DontHaveEnoughMoney")
									.replaceAll("<item>", item).replaceAll("<result>", cookedItem)
									.replaceAll("<amount>", pItemAmount + "").replaceAll("<exp>", expVariable), command,
									player);

							if (pItemName.equalsIgnoreCase(rawItem)) {

								ItemStack cookedResult = new ItemStack(Material.getMaterial(cookedItem), pItemAmount);

								double totalCost = cost * pItemAmount;

								if (!(totalCost > playerBalance)) {

									if (!(totalCost > maxWithdraw)) {

										if (expVariable.endsWith("L")) {
											int exp = Integer.parseInt(expVariable.replace("L", ""));
											player.giveExpLevels(exp * pItemAmount);
										} else {
											int exp = Integer.parseInt(expVariable);
											player.giveExp(exp * pItemAmount);
										}

										eco.withdrawPlayer(player, totalCost);

										player.getInventory().setItemInMainHand(cookedResult);

										player.playSound(player.getLocation(), Sound.BLOCK_FURNACE_FIRE_CRACKLE, 2, 2);

										player.sendMessage(cookSuccess);

										validItem = true;
										break;
									} else {

										String cantSpendThatMuch = tools.chat(config.getString("CannotSpend")
												.replaceAll("<item>", item).replaceAll("<result>", cookedItem)
												.replaceAll("<amount>", pItemAmount + "")
												.replaceAll("<cost>", (totalCost) + "").replaceAll("<price>", cost + "")
												.replaceAll("<exp>", expVariable)
												.replaceAll("<maxWithdraw>", maxWithdraw + ""), command, player);

										player.sendMessage(cantSpendThatMuch);

										validItem = true;
										break;

									}

								} else {

									player.sendMessage(cookNoMoney.replaceAll("<cost>", totalCost + "")
											.replaceAll("<price>", totalCost + ""));

									break;
								}

							} else {
								validItem = false;

							}

						}

						if (!validItem) {
							String cantCook = tools.chat(config.getString("Cook.InvalidItem")
									.replaceAll("<item>", pItemName).replaceAll("<amount>", pItemAmount + ""), command,
									player);

							player.sendMessage(cantCook);
						}

					}

				} else {
					player.sendMessage(tools.chat(noPermission, command, player));
				}

			} else if (args.length == 1) {

				String subject = args[0];

				if (subject.equalsIgnoreCase("items")) {

				} else if (subject.equalsIgnoreCase("help")) {

				} else {

					for (Player p : Bukkit.getOnlinePlayers()) {

						String pName = p.getName();

						if (subject.equalsIgnoreCase(pName)) {

							break;
						}

					}

				}

			}

		} else {
			plugin.console
					.sendMessage(tools.chat("&eLol, trying to cook food in the console?? ... smh", command, null));
		}

	}

}
