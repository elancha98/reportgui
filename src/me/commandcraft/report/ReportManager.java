package me.commandcraft.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ReportManager implements Listener {

	private List<UUID> admins = new ArrayList<UUID>();
	FileConfiguration config;
	
	public void init(File folder) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player p : players) {
			if (p.hasPermission("report.admin")) {
				admins.add(p.getUniqueId());
			}
		}
		File f = new File(folder, "reports.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				Main.error(e.toString());
			}
		}
		config = YamlConfiguration.loadConfiguration(f);
	}
	
	public void save(File folder) {
		try {
			config.save(new File(folder, "reports.yml"));
		} catch (IOException e) {
			Main.error(e.toString());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("report.admin")) {
			admins.add(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("report.admin")) {
			admins.remove(p.getUniqueId());
		}
	}
	
	public void report(String player, String target, String reason) {
		String msg = ChatColor.RED + player + " reported " + target + " for: " + reason;
		for (UUID uuid : admins) {
			Player p = Bukkit.getPlayer(uuid);
			p.sendMessage(msg);
		}
		List<String> reports = config.getStringList("reports");
		reports.add(player);
		reports.add(target);
		reports.add(reason);
		config.set("reports", reports);
	}
	
	public List<String> getReports() {
		List<String> ret = new ArrayList<String>();
		List<String> list = config.getStringList("reports");
		int count = 0;
		for (int i = list.size()-1; i >= 0 && count < 50; i -= 3) {
			String txt = ChatColor.RED + list.get(i-2) + " reported " + list.get(i-1) + " for: " + list.get(i);
			ret.add(txt);
			count++;
		}
		return ret;
	}
	
	public List<String> getReports(String player) {
		List<String> ret = new ArrayList<String>();
		List<String> list = config.getStringList("reports");
		int count = 0;
		for (int i = list.size()-1; i >= 0 && count < 50; i -= 3) {
			String target = list.get(i-1);
			if (target.equals(player)) {
				String txt = ChatColor.RED + list.get(i-2) + " reported " + target + " for: " + list.get(i);
				ret.add(txt);
				count++;
			}
		}
		return ret;
	}
	
	public void add(Player player) {
		admins.add(player.getUniqueId());
	}
	
	public void remove(Player player) {
		admins.remove(player.getUniqueId());
	}
	
	public void removeReports(String p) {
		List<String> reports = config.getStringList("reports");
		for (int i = reports.size()-1; i >= 0; i -= 3) {
			String target = reports.get(i-1);
			if (target.equals(p)) {
				reports.remove(i);
				reports.remove(i-1);
				reports.remove(i-2);
			}
		}
		config.set("reports", reports);
	}
}
