package me.commandcraft.report;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {
	
	public static void process(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			notPlayer(sender);
			return;
		}
		Player player = (Player) sender;
		if (args.length > 0) {
			switch (args[0]) {
			case "check":
				if (!player.hasPermission("report.admin")) {
					usage(sender);
					return;
				}
				if (args.length > 1) {
					OfflinePlayer target = getPlayer(args[1]);
					if (target != null) {
						List<String> reports = Main.reportManager.getReports(target.getName());
						for (String report : reports) {
							sender.sendMessage(report);
						}
					} else notFound(sender);
				} else {
					List<String> reports = Main.reportManager.getReports();
					for (String report : reports) {
						sender.sendMessage(report);
					}
				}
				break;
			case "notify":
				if (args.length > 1) {
					if (!player.hasPermission("report.admin")) {
						usage(sender);
						return;
					}
					if (args[1].equalsIgnoreCase("on")) {
						Main.reportManager.add(player);
					} else if (args[1].equalsIgnoreCase("off")) {
						Main.reportManager.remove(player);
					} else usage(sender);
				} else usage(sender);
				break;
			case "clear":
				if (args.length > 1) {
					if (!player.hasPermission("report.admin")) {
						usage(sender);
						return;
					}
					OfflinePlayer target = getPlayer(args[1]);
					if (target != null) {
						Main.reportManager.removeReports(target.getName());
					} else notFound(sender);
				} else usage(sender);
				break;
			default:
				OfflinePlayer target = getPlayer(args[0]);
				if (target != null) {
					new ReportInventory(player.getName(), target.getName());
				} else notFound(sender);
				break;
			}
		} else {
			usage(sender);
		}
	}
	
	public static void notFound(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "error: unable to find that player");
	}
	
	public static void notPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "error: only players can use this command");
	}
	
	public static void usage(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "usage:");
		sender.sendMessage(ChatColor.GREEN + " - /report <player> " + ChatColor.YELLOW + "to report a player");
		if (sender.hasPermission("report.admin")) {
			sender.sendMessage(ChatColor.GREEN + " - /report check " + ChatColor.YELLOW + "to see last 50 reports");
			sender.sendMessage(ChatColor.GREEN + " - /report check <player> " + ChatColor.YELLOW + "to see last 50 reports to a player");
			sender.sendMessage(ChatColor.GREEN + " - /report notify on/off " + ChatColor.YELLOW + "to turn on, off the notifications");
			sender.sendMessage(ChatColor.GREEN + " - /report clear <player> " + ChatColor.YELLOW + "to clear all reports to a player");
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use /report <check:notify:clear>");
		}
	}
	
	public static OfflinePlayer getPlayer(String target) {
		return Bukkit.getPlayer(target);
	}
}
