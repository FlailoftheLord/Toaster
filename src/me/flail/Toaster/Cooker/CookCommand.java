package me.flail.Toaster.Cooker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.flail.Toaster.Toaster;
import me.flail.Toaster.Tools;
import net.milkbowl.vault.economy.Economy;

public class CookCommand {

	private Toaster plugin = Toaster.getPlugin(Toaster.class);

	private Tools tools = plugin.tools;

	private Economy eco = plugin.getEconomy();

	public Map<Player, Integer> cooldown = new HashMap<>();

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

					if (player.hasPermission("toaster.friend") || player.hasPermission("toaster.friend.cook")
							|| player.hasPermission("toaster.op")) {

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

						String itemName = tools.chat(
								config.getString("Friend.NameFormat").replaceAll("<item>", friendItem)
										.replaceAll("<result>", friendItem).replaceAll("<amount>", 1 + "")
										.replaceAll("<cost>", (price * 1) + "").replaceAll("<price>", price + "")
										.replaceAll("<exp>", expVar).replaceAll("<friend-name>", subject),
								command, player);

						List<String> itemLore = config.getStringList("Friend.Lore");

						Material itemMaterial = Material.matchMaterial(friendItem);

						ItemStack item = new ItemStack(itemMaterial);

						ItemMeta itemM = item.getItemMeta();

						List<String> lore = new ArrayList<>();

						for (String s : itemLore) {

							lore.add(tools.chat(s.replaceAll("<item>", friendItem).replaceAll("<result>", friendItem)
									.replaceAll("<amount>", 1 + "").replaceAll("<cost>", (price * 1) + "")
									.replaceAll("<price>", price + "").replaceAll("<exp>", expVar)
									.replaceAll("<friend-name>", subject), command, player));

						}

						itemM.setDisplayName(itemName);
						itemM.setLore(lore);

						item.setItemMeta(itemM);

						String friendCook = tools.toasterChat(
								config.getString("Friend.Success").replaceAll("<friend-name>", subject), player,
								command, "Friend", 1, "friend");
						String tooPoor = tools.toasterChat(
								config.getString("Friend.TooPoor").replaceAll("<friend-name>", subject), player,
								command, "Friend", 1, "friend");

						double playerBal = eco.getBalance(player);

						if (!(price > playerBal)) {

							if (validPlayer) {
								boolean broadcastMsg = config.getBoolean("Friend.Broadcast.Enabled");
								if (broadcastMsg) {
									String broadcast = config.getString("Friend.Broadcast.Message");
									plugin.server.broadcastMessage(
											tools.toasterChat(broadcast.replaceAll("<friend-name>", subject), player,
													command, "Friend", 1, "friend"));
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

								}

							} else {

							}

						} else {

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
