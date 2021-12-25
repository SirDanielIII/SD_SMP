package com.sirdanieliii.SD_SMP.configuration;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.sirdanieliii.SD_SMP.SD_SMP.getInstance;

public class ConfigManager {
    protected File file;
    protected FileConfiguration customFile;

    public void setup(String folder, String name) {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(
                getInstance().getName())).getDataFolder() + "/" + folder, name + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return customFile;
    }

    public void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Could not save to data file");
        }
    }

    public void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}