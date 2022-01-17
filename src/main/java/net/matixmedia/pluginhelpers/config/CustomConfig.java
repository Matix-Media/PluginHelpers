package net.matixmedia.pluginhelpers.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private JavaPlugin plugin;

    private String fileName;
    private File file;
    private FileConfiguration config;

    public CustomConfig(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName);
    }

    public void saveDefaultConfig() {
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            plugin.saveResource(this.fileName, false);
        }
        this.config = new YamlConfiguration();
        try {
            this.config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.saveDefaultConfig();
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration get() {
        return this.config;
    }

    public String getFormattedString(String path) {
        return ChatColor.translateAlternateColorCodes('&', get().getString(path));
    }

}
