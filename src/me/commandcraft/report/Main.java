package me.commandcraft.report;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	static PluginManager manager;
	static JavaPlugin instance;
	static ReportManager reportManager;
	static Logger logger;
	
	public void onEnable() {
		instance = this;
		logger = Bukkit.getLogger();
		manager = Bukkit.getPluginManager();
		reportManager = new ReportManager();
		manager.registerEvents(reportManager, this);
		reportManager.init(getDataFolder());
	}
	
	public void onDisable() {
		reportManager.save(getDataFolder());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("report")) {
			CommandManager.process(sender, args);
		}
		return true;
	}
	
	public static void createReport(String player, String target, String reason) {
		reportManager.report(player, target, reason);
	}
	
	public static void registerEvent(ReportInventory inv) {
		manager.registerEvents(inv, instance);
	}
	
	public static void error(String s) {
		logger.warning(s);
	}
}
