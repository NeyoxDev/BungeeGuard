package fr.neyox.bungeeguard.spigot;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.neyox.bungeeguard.common.utils.Fetcher;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class Events implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(AsyncPlayerPreLoginEvent ev) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(ev.getName()) || player.getUniqueId().equals(ev.getUniqueId()) || player.getUniqueId().toString().toLowerCase().replaceAll("-", "").equalsIgnoreCase(ev.getUniqueId().toString().toLowerCase().replaceAll("-", ""))) {
				ev.setKickMessage("§cVous êtes déjà connecté sur le proxy.");
				ev.setLoginResult(Result.KICK_WHITELIST);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e) {
		String proxyIp = Main.getPlugin(Main.class).getPluginConfig().getProxyIp();
		String formatedKickMsg = Main.getPlugin(Main.class).getPluginConfig().getKickMessage().replace("&", "§").replace("%nl%", "\n");
		String playerHost = e.getHostname();
		String uuid = e.getPlayer().getUniqueId().toString().replace("-", "");
		String offUuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + e.getPlayer().getName()).getBytes()).toString().replace("-", "");
		boolean kick = !proxyIp.equals(playerHost) || e.getAddress() == null || e.getRealAddress() == null || !e.getRealAddress().getHostAddress().equalsIgnoreCase(proxyIp);
		if (!uuid.contains(offUuid)) {
			String uuidReal = Fetcher.fetchOnline(e.getPlayer());
			if (uuidReal == null || !uuidReal.contains(uuid)) {
				kick = true;
			}
		}
		
		
		if (kick) {
			Bukkit.getLogger().info("[BungeeGuard] Player " + e.getPlayer().getName() + " is connecting with IP : " + playerHost);
			e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			e.setKickMessage(formatedKickMsg);
		}
		
	}

}
