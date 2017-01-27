package me.commandcraft.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ReportInventory implements Listener {

	String player, target;
	Inventory reportInventory;

    public ReportInventory(String player, String target) {
    	this.player = player;
    	this.target = target;
    	
        reportInventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&4&lReport"));
        ItemStack redPane = createItem(Material.STAINED_GLASS_PANE, "&cReport Gui", Arrays.asList(" "), (byte) 14);
        for (int i = 9; i < 18; i++) {
            reportInventory.setItem(i, redPane);
        }
        reportInventory.setItem(24, createItem(Material.WOOL, "&4Exit", Arrays.asList("&cExit the menu!"), (byte) 14));


        reportInventory.setItem(0, createItem(Material.DIAMOND_SWORD, "&cKill Aura", Arrays.asList("&cReport: " + target)));

        reportInventory.setItem(1, createItem(Material.FEATHER, "&eFly", Arrays.asList("&eReport: " + target)));

        reportInventory.setItem(2, createItem(Material.SUGAR, "&aSpeed", Arrays.asList("&aReport: " + target)));

        reportInventory.setItem(3, createItem(Material.DIAMOND_CHESTPLATE, "&dNo Knockback", Arrays.asList("&dReport: " + target)));

        reportInventory.setItem(4, createItem(Material.DIAMOND_ORE, "&cX-Ray", Arrays.asList("&cReport: " + target)));

        reportInventory.setItem(5, createItem(Material.SOUL_SAND, "&4No Slowdown", Arrays.asList("&4Report: " + target)));

        reportInventory.setItem(6, createItem(Material.WATER_BUCKET, "&bWater walking (Jesus)", Arrays.asList("&bReport: " + target)));

        reportInventory.setItem(7, createItem(Material.ENDER_PEARL, "&5Blink", Arrays.asList("&5Report: " + target)));

        reportInventory.setItem(8, createItem(Material.PAPER, "&7Other Hack", Arrays.asList("&7Report: " + target)));

        reportInventory.setItem(20, skull(target));
        Main.registerEvent(this);
        openInventory();
    }

    private ItemStack createItem(Material material, String name, List<String> lore, byte data) {
        ItemStack itemStack = new ItemStack(material, 1, data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> tempLore = new ArrayList<>();
        lore.forEach(s -> tempLore.add(ChatColor.translateAlternateColorCodes('&', s)));
        itemMeta.setLore(tempLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> tempLore = new ArrayList<>();
        lore.forEach(s -> tempLore.add(ChatColor.translateAlternateColorCodes('&', s)));
        itemMeta.setLore(tempLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack skull(String nick) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Target");
        meta.setOwner(nick);
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Player: " + nick));
        item.setItemMeta(meta);
        return item;
    }
    
    public void openInventory() {
    	Bukkit.getPlayer(player).openInventory(reportInventory);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    	if (event.getClickedInventory().equals(reportInventory)) {
    		event.setCancelled(true);
    		ClickType type = event.getClick();
    		if (type.equals(ClickType.LEFT)) {
    			ItemStack item = event.getCurrentItem();
    			if (item != null) {
    				if (item.getType().equals(Material.SKULL_ITEM)) return;
    				if (!item.getType().equals(Material.WOOL)) {
	    				String reason = ChatColor.stripColor(item.getItemMeta().getDisplayName());
	    				Main.createReport(player, target, reason);
    				}
    				InventoryClickEvent.getHandlerList().unregister(this);
    				event.getWhoClicked().closeInventory();
    			}
    		}
    	}
    }
}
