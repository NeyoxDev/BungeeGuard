package fr.neyox.bungeeguard.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.bukkit.Bukkit;

import fr.neyox.bungeeguard.common.utils.IOUtil;
import fr.neyox.bungeeguard.spigot.Main;
import lombok.Getter;

public class ConfigManager {
	
	@Getter
	private Config pluginConfig;
	
	@SuppressWarnings("resource")
	public ConfigManager(Main main) {
		this.pluginConfig = main.getPluginConfig();
		Bukkit.getLogger().info("Chargement de la confiuration...");
		try {
			File configFile = new File(main.getDataFolder(), "/config.json");
			if (!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				this.pluginConfig = new Config("NULLIP", "&cMauvaise connexion %nl% &cConnectez-vous avez l'ip play.exemple.com");
				IOUtil.writeFromInputStream(new FileOutputStream(configFile), Main.GSON.toJson(pluginConfig));
				return;
			}
			try {
				this.pluginConfig = Main.GSON.fromJson(IOUtil.readInputStreamAsString(new FileInputStream(configFile)), Config.class);
			} catch (Exception e) {
				this.pluginConfig = new Config("NULLIP", "&cMauvaise connexion %nl% &cConnectez-vous avez l'ip play.exemple.com");
			}
			
			if (pluginConfig == null) {
				this.pluginConfig = new Config("NULLIP", "&cMauvaise connexion %nl% &cConnectez-vous avez l'ip play.exemple.com");
			}
			
		} catch (Exception e) {
			Bukkit.getLogger().warning("Erreur lors des chargements de la configuration");
			e.printStackTrace();
			return;
		}
		Bukkit.getLogger().info("Configuration chargée (Proxy IP: "+pluginConfig.getProxyIp()+")");
	}

}
