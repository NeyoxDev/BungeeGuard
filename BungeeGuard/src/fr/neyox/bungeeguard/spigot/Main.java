package fr.neyox.bungeeguard.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.neyox.bungeeguard.common.utils.LibManager;
import fr.neyox.bungeeguard.config.Config;
import fr.neyox.bungeeguard.config.ConfigManager;
import lombok.Getter;

@Getter
public class Main extends JavaPlugin {
	
	public static final Gson GSON = new GsonBuilder().serializeNulls().create();
	
	private ConfigManager configManager;
	
	private Config pluginConfig;
	
	@Override
	public void onEnable() {
		if (!System.getProperty("java.version").contains("1.8")) {
			Bukkit.getLogger().warning("Il faut avoir java 8 pour utiliser ce plugin.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		if (!SpigotConfig.bungee) {
			Bukkit.getLogger().warning("Il faut que ce serveur soit relier à un proxy pour utiliser ce plugin.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		loadLibs();
		loadConfig();
		this.getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	private void loadConfig() {
		this.configManager = new ConfigManager(this);
		this.pluginConfig = configManager.getPluginConfig();
	}
	
	private void loadLibs() {
		Bukkit.getLogger().info("Chargement des libraries...");
		try {
			saveResources("lombok.jar");
			LibManager.addLibs("lombok.jar");
		} catch (Exception e) {
			Bukkit.getLogger().warning("Erreur lors des chargements des libraries");
			e.printStackTrace();
			return;
		}
		Bukkit.getLogger().info("Libraries chargés !");
	}
	
	private void saveResources(String... strings) {
		for (String strr : strings) {
			saveResource(strr, true);
		}
	}

}
