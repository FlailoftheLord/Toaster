package me.flail.Toaster.Smelter;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.flail.Toaster.Toaster;
import me.flail.Toaster.Utilities.Tools;
import me.flail.microtools.tools.Logger;
import net.milkbowl.vault.economy.Economy;

public class SmeltCommand extends Logger {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

	private Economy eco = plugin.getEconomy();

	public void Smelt(CommandSender sender, String command, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			FileConfiguration config = plugin.getConfig();

			String noPermission = config.getString("NoPermissionMessage");
			String noMoney = config.get("Smelt.DontHaveEnoughMoney").toString();
			String invalidItem = config.get("Smelt.InvalidItem").toString();

			if (args.length == 0) {

				if (player.hasPermission("toaster.smelt")) {

					ItemStack pHand = player.getInventory().getItemInMainHand();

					String pItemName = pHand.getType().toString();

					if (pItemName.isEmpty() || pItemName.equalsIgnoreCase("air") || (pHand == null)) {
						player.sendMessage(tools.chat("<prefix> &cYou must have an item in your hand!", "smelt", player));

						return;
					}

					Map<String, Object> itemSection = tools.getItemSection(pItemName, "smelt");

					if (itemSection == null) {
						player.sendMessage(tools.toasterChat(invalidItem, player, "smelt",
								itemSection, pHand.getAmount(), "smelt"));
						return;
					}

					String permission = "";
					if (itemSection.containsKey("permission")) {
						permission = itemSection.get("permission").toString();
					} else {
						permission = "toaster.smelt";
					}

					if (player.hasPermission("toaster.item.all") || player.hasPermission(permission)) {

						int amount = pHand.getAmount();
						double cost = 0.0;
						if (itemSection.containsKey("price")) {
							cost = Double.parseDouble(itemSection.get("price").toString().replaceAll("[a-zA-Z]", ""));
						}
						double totalPrice = cost * amount;



						Material smeltedType = tools.convertItem(pItemName, "smelt");
						if (smeltedType == Material.AIR) {
							player.sendMessage(tools.chat(
									"<prefix> &cAn invalid item was detected. Cancelling... Error at: Result:"
											+ itemSection.get("result").toString(),
											"smelt", player));

							plugin.console.sendMessage(
									tools.chat("<prefix> &cAn invalid item was detected. Cancelling... Error at: Result:"
											+ itemSection.get("result").toString(), "", player));

							return;
						}
						// Once all checks are done, give the player the converted item.
						ItemStack smeltedItem = new ItemStack(smeltedType, amount);
						player.getInventory().setItemInMainHand(smeltedItem);

						// Then give appropriate experience
						String experience = itemSection.get("exp").toString().replaceAll("[^L0-9]", "");
						boolean isExpLevel = false;
						int expAmount = Integer.parseInt(experience.replaceAll("[^0-9]", ""));
						int totalExp = expAmount * amount;

						if (experience.endsWith("L")) {
							isExpLevel = true;
						}

						if (isExpLevel) {
							player.giveExpLevels(totalExp);
						} else {
							player.giveExp(totalExp);
						}


						if (plugin.getEconomy() != null) {

							if ((eco.getBalance(player) < totalPrice) && !player.hasPermission("toaster.bypasscost")) {
								player.sendMessage(tools.toasterChat(noMoney, player, "smelt", itemSection, amount, "smelt"));
								return;
							}

							double maxSpendings = config.getDouble("MaxMoneyWithdraw", 100.0);
							if ((totalPrice > maxSpendings) && !player.hasPermission("toaster.bypasslimits")) {
								player.sendMessage(tools.toasterChat(
										config.get("CannotSpend").toString().replace("<maxWithdraw>", maxSpendings + ""), player,
										"smelt", itemSection, amount, "smelt"));
								return;
							}

							// Withdraw the cost from their balance.
							if (!player.hasPermission("toaster.bypasscost")) {
								eco.withdrawPlayer(player, totalPrice);
							}

						}

						// And send the messages.
						player.sendMessage(tools.toasterChat(config.get("Smelt.Success").toString(), player, "smelt",
								itemSection, amount, "smelt"));

						return;
					} else {
						player.sendMessage(tools.toasterChat(invalidItem, player, "smelt",
								itemSection, pHand.getAmount(), "smelt"));
						return;
					}

				} else {
					player.sendMessage(tools.chat(noPermission, command, player));
				}


			} else {
				player.sendMessage(
						tools.chat("<prefix> &cHold an item in your hand and type &7/smelt", command, player));

			}

		} else {
			plugin.console
			.sendMessage(tools.chat("&eLol, trying to smelt stuffs in the console?? ... smh", command, null));
		}

	}

}
