package me.flail.Toaster.Smelter;

import java.util.Set;

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

public class SmeltCommand {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

	private Economy eco = plugin.getEconomy();

	public void Smelt(CommandSender sender, String command, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			FileConfiguration config = plugin.getConfig();

			String noPermission = config.getString("NoPermissionMessage");

			if (args.length == 0) {

				if (player.hasPermission("toaster.smelt") || player.hasPermission("toaster.op")) {

					FileConfiguration itemConfig = plugin.getItemConfig();

					ConfigurationSection itemList = itemConfig.getConfigurationSection("Smeltables");

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

							String smeltedItem = itemList.getString(item + ".Result").toUpperCase();

							String expVariable = itemList.get(item + ".Exp").toString().toUpperCase();

							double cost = itemList.getDouble(item + ".Price");

							String smeltSuccess = tools.chat(
									config.getString("Smelt.Success").replaceAll("<item>", item)
											.replaceAll("<result>", smeltedItem)
											.replaceAll("<amount>", pItemAmount + "")
											.replaceAll("<cost>", (cost * pItemAmount) + "")
											.replaceAll("<price>", cost + "").replaceAll("<exp>", expVariable),
									command, player);

							String smeltNoMoney = tools.chat(
									config.getString("Smelt.DontHaveEnoughMoney").replaceAll("<item>", item)
											.replaceAll("<result>", smeltedItem)
											.replaceAll("<amount>", pItemAmount + "").replaceAll("<exp>", expVariable),
									command, player);

							if (pItemName.equalsIgnoreCase(rawItem)) {

								ItemStack smeltedResult = new ItemStack(Material.getMaterial(smeltedItem), pItemAmount);

								double totalCost = cost * pItemAmount;

								if (!(totalCost > playerBalance)) {

									if ((totalCost > maxWithdraw) && !player.hasPermission("toaster.op")
											&& !player.hasPermission("toaster.bypasslimits")) {

										String cantSpendThatMuch = tools.chat(config.getString("CannotSpend")
												.replaceAll("<item>", item).replaceAll("<result>", smeltedItem)
												.replaceAll("<amount>", pItemAmount + "")
												.replaceAll("<cost>", (totalCost) + "").replaceAll("<price>", cost + "")
												.replaceAll("<exp>", expVariable)
												.replaceAll("<maxWithdraw>", maxWithdraw + ""), command, player);

										player.sendMessage(cantSpendThatMuch);

										validItem = true;
										break;

									} else {

										if (expVariable.endsWith("L")) {
											int exp = Integer.parseInt(expVariable.replace("L", ""));
											player.giveExpLevels(exp * pItemAmount);
										} else {
											int exp = Integer.parseInt(expVariable);
											player.giveExp(exp * pItemAmount);
										}

										eco.withdrawPlayer(player, totalCost);

										player.getInventory().setItemInMainHand(smeltedResult);

										player.playSound(player.getLocation(), Sound.BLOCK_FURNACE_FIRE_CRACKLE, 2, 2);

										player.sendMessage(smeltSuccess);

										validItem = true;
										break;

									}

								} else {

									player.sendMessage(smeltNoMoney.replaceAll("<cost>", totalCost + "")
											.replaceAll("<price>", totalCost + ""));

									break;
								}

							} else {
								validItem = false;

							}

						}

						if (!validItem) {
							String cantsmelt = tools.chat(config.getString("Smelt.InvalidItem")
									.replaceAll("<item>", pItemName).replaceAll("<amount>", pItemAmount + ""), command,
									player);

							player.sendMessage(cantsmelt);
						}

					}

				} else {
					player.sendMessage(tools.chat(noPermission, command, player));
				}

			} else {

				player.sendMessage(
						tools.chat("<prefix> &cHold your item in your hand and type &7/smelt", command, player));

			}

		} else {
			plugin.console.sendMessage(tools.chat("&eSeriously?? O_O", command, null));
		}

	}

}
