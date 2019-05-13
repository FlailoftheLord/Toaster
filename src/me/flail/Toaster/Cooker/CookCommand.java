package me.flail.Toaster.Cooker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.flail.Toaster.Toaster;
import me.flail.Toaster.Utilities.Tools;
import net.milkbowl.vault.economy.Economy;

public class CookCommand {

	private Toaster plugin = Toaster.toaster;

	private Tools tools = plugin.tools;

	private Economy eco = plugin.getEconomy();

	private Map<UUID, Integer> cooldown = plugin.cooldown;

	public void Cook(CommandSender sender, String command, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			FileConfiguration config = plugin.getConfig();

			String noPermission = config.getString("NoPermissionMessage");
			String noMoney = config.get("Cook.DontHaveEnoughMoney").toString();
			String invalidItem = config.get("Cook.InvalidItem").toString();

			if (args.length == 0) {

				if (player.hasPermission("toaster.cook")) {

					ItemStack pHand = player.getInventory().getItemInMainHand();

					String pItemName = pHand.getType().toString();

					if (pItemName.isEmpty() || pItemName.equalsIgnoreCase("air") || (pHand == null)) {
						player.sendMessage(tools.chat("<prefix> &cYou must have an item in your hand!", "cook", player));

						return;
					}

					Map<String, Object> itemSection = tools.getItemSection(pItemName, "cook");

					if (itemSection == null) {
						player.sendMessage(tools.toasterChat(invalidItem + "null item", player, "cook",
								itemSection, pHand.getAmount(), "cook"));
						return;
					}

					String permission = "";
					if (itemSection.containsKey("permission")) {
						permission = itemSection.get("permission").toString();
					} else {
						permission = "toaster.cook";
					}

					if (player.hasPermission("toaster.item.all") || player.hasPermission(permission)) {

						int amount = pHand.getAmount();
						double cost = 0.0;
						if (itemSection.containsKey("price")) {
							cost = Double.parseDouble(itemSection.get("price").toString().replaceAll("[a-zA-Z]", ""));
						}
						double totalPrice = cost * amount;

						if ((eco.getBalance(player) < totalPrice) && !player.hasPermission("toaster.bypasscost")) {
							player.sendMessage(tools.toasterChat(noMoney, player, "cook", itemSection, amount, "cook"));
							return;
						}

						double maxSpendings = config.getDouble("MaxMoneyWithdraw", 100.0);
						if ((totalPrice > maxSpendings) && !player.hasPermission("toaster.bypasslimits")) {
							player.sendMessage(tools.toasterChat(
									config.get("CannotSpend").toString().replace("<maxWithdraw>", maxSpendings + ""), player,
									"cook", itemSection, amount, "cook"));
							return;
						}

						Material cookedType = tools.convertItem(pItemName, "cook");
						if (cookedType == Material.AIR) {
							player.sendMessage(tools.chat(
									"<prefix> &cAn invalid item was detected. Cancelling... Error at: Result:"
											+ itemSection.get("result").toString(),
											"cook", player));

							plugin.console.sendMessage(
									tools.chat("<prefix> &cAn invalid item was detected. Cancelling... Error at: Result:"
											+ itemSection.get("result").toString(), "", player));

							return;
						}
						// Once all checks are done, give the player the converted item.
						ItemStack cookedItem = new ItemStack(cookedType, amount);
						player.getInventory().setItemInMainHand(cookedItem);

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

						// Withdraw the cost from their balance.
						if (!player.hasPermission("toaster.bypasscost")) {
							eco.withdrawPlayer(player, totalPrice);
						}

						// And send the messages.
						player.sendMessage(tools.toasterChat(config.get("Cook.Success").toString(), player, "cook",
								itemSection, amount, "cook"));

						return;
					} else {
						player.sendMessage(tools.toasterChat(invalidItem, player, "cook",
								itemSection, pHand.getAmount(), "cook"));
						return;
					}

				} else {
					player.sendMessage(tools.chat(noPermission, command, player));
				}

			} else if (args.length == 1) {

				String subject = args[0];

				if (subject.equalsIgnoreCase("items")) {

				} else if (subject.equalsIgnoreCase("help")) {

				} else {

					if (player.hasPermission("toaster.friend") || player.hasPermission("toaster.friend.cook")
							|| player.hasPermission("toaster.op")) {

						int cooldownTime = config.getInt("Friend.Cooldown");

						int cooldownLeft = 0;
						if (cooldown.containsKey(player.getUniqueId())) {
							cooldownLeft = Integer.valueOf(cooldown.get(player.getUniqueId()));
						}


						if ((!player.hasPermission("toaster.friend.bypasscooldown")
								&& (cooldownLeft > 0))) {

							String cooldownMsg = tools.toasterChat(
									config.getString("Friend.CooldownMessage"), player, "cook", null,
									cooldownLeft,
									"friend");

							player.sendMessage(tools.chat(cooldownMsg, command, player));

						} else {

							boolean validPlayer = false;

							for (Player p : Bukkit.getOnlinePlayers()) {

								String pName = p.getName();

								if (subject.equalsIgnoreCase(pName)) {
									validPlayer = true;
									break;
								}

							}

							double price = config.getDouble("Friend.Cost");
							String expVar = config.getString("Friend.Exp").toUpperCase();

							String friendItem = config.getString("Friend.Item").toUpperCase();

							String itemName = tools.toasterChat(
									config.getString("Friend.NameFormat"), player, "cook", null,
									Integer.parseInt(cooldownLeft + ""),
									"friend")
									.replace("<friend-name>", subject);

							List<String> itemLore = config.getStringList("Friend.Lore");

							Material itemMaterial = Material.matchMaterial(friendItem);

							ItemStack item = new ItemStack(itemMaterial);

							ItemMeta itemM = item.getItemMeta();

							List<String> lore = new ArrayList<>();

							for (String s : itemLore) {

								lore.add(
										tools.chat(s.replaceAll("<item>", friendItem).replaceAll("<result>", friendItem)
												.replaceAll("<amount>", 1 + "").replaceAll("<cost>", (price * 1) + "")
												.replaceAll("<price>", price + "").replaceAll("<exp>", expVar)
												.replaceAll("<friend-name>", subject), command, player));

							}

							itemM.setDisplayName(itemName);
							itemM.setLore(lore);

							item.setItemMeta(itemM);

							String friendCook = tools.toasterChat(
									config.getString("Friend.Success").replaceAll("<friend-name>", subject), player,
									command, null, 1, "friend");
							String tooPoor = tools.toasterChat(
									config.getString("Friend.TooPoor").replaceAll("<friend-name>", subject), player,
									command, null, 1, "friend");

							double playerBal = eco.getBalance(player);

							if (!player.hasPermission("toaster.op") && !(price > playerBal)) {

								boolean broadcastMsg = config.getBoolean("Friend.Broadcast.Enabled");
								if (validPlayer && broadcastMsg) {
									String broadcast = config.getString("Friend.Broadcast.Message");
									plugin.server.broadcastMessage(
											tools.toasterChat(broadcast.replaceAll("<friend-name>", subject), player,
													command, null, 1, "friend"));

								}
								player.getInventory().addItem(new ItemStack(item));
								player.sendMessage(friendCook);
								eco.withdrawPlayer(player, price);

								if (expVar.endsWith("L")) {
									int exp = Integer.parseInt(expVar.replace("L", ""));
									player.giveExpLevels(exp);
								} else {
									int exp = Integer.parseInt(expVar);
									player.giveExp(exp);
								}

								cooldown.put(player.getUniqueId(), Integer.valueOf(cooldownTime));

							} else if (player.hasPermission("toaster.op")) {

								boolean broadcastMsg = config.getBoolean("Friend.Broadcast.Enabled");
								if (validPlayer && broadcastMsg) {

									String broadcast = config.getString("Friend.Broadcast.Message");
									plugin.server.broadcastMessage(
											tools.toasterChat(broadcast.replaceAll("<friend-name>", subject), player,
													command, null, 1, "cook"));
								}

								player.getInventory().addItem(new ItemStack(item));
								player.sendMessage(friendCook);

								if (expVar.endsWith("L")) {
									int exp = Integer.parseInt(expVar.replace("L", ""));
									player.giveExpLevels(exp);
								} else {
									int exp = Integer.parseInt(expVar);
									player.giveExp(exp);
								}

								cooldown.put(player.getUniqueId(), Integer.valueOf(cooldownTime));

							} else {

								player.sendMessage(tooPoor);

							}

						}

					} else {

						player.sendMessage(tools.chat(noPermission, command + " " + subject, player));

					}

				}

			} else {

				player.sendMessage(
						tools.chat("<prefix> &cHold an item in your hand and type &7/cook", command, player));
				if (player.hasPermission("toaster.friend") || player.hasPermission("toaster.friend.cook")
						|| player.hasPermission("toaster.op")) {
					player.sendMessage(
							tools.chat("&cor you can type &7/cook [player-name] &cto cook a player!", command, player));
				}

			}

		} else {
			plugin.console
			.sendMessage(tools.chat("&eLol, trying to cook food in the console?? ... smh", command, null));
		}

	}

}
