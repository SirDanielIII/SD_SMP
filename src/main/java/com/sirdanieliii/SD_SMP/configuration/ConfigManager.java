package com.sirdanieliii.SD_SMP.configuration;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sirdanieliii.SD_SMP.SD_SMP.getInstance;
import static org.bukkit.Bukkit.getLogger;

public class ConfigManager {
    protected File file;
    protected FileConfiguration config;
    // Set Variables
    public Double configVersion = Double.parseDouble(getInstance().getDescription().getVersion());
    public List<String> MoTD = new ArrayList<>();
    public List<String> welcome = new ArrayList<>();
    public boolean customJoinMessages;
    public boolean customQuitMessages;
    public boolean customSleepMessages;
    public boolean lightningOnPlayerKill;
    public boolean disableSmithingTableEnabled;
    public boolean disableSmithingTableSendMsg;
    public String disableSmithingTableMessage;
    public boolean preventCraftingToolsEnabled;
    public boolean preventCraftingToolsSendMsg;
    public String preventCraftingToolsMessage;
    public boolean preventCraftingArmourEnabled;
    public boolean preventCraftingArmourSendMsg;
    public String preventCraftingArmourMessage;

    public List<String> joinMessages = new ArrayList<>();
    public List<String> quitMessages = new ArrayList<>();
    public List<String> sleepMessages = new ArrayList<>();
    public List<String> describeKill = new ArrayList<>();
    public List<String> describeDeath = new ArrayList<>();

    public void setup() {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(
                getInstance().getName())).getDataFolder() + "/", "config.yml");

        // Create file if it doesn't exist
        if (!(file.exists())) {
            getLogger().info("[SD_SMP] No config file found, creating a new one...");
            generateConfig();
            getLogger().info("[SD_SMP] Successfully generated new config file");
        } else {  // Check Config Versions
            if (!(Objects.equals(YamlConfiguration.loadConfiguration(file).getDouble("config-version"), configVersion)) ||
                    YamlConfiguration.loadConfiguration(file).getDouble("config-version") != 0) {
                Bukkit.getConsoleSender().sendMessage("[SD_SMP] Older configuration file detected.");
                File old = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(getInstance().getName())).getDataFolder() + "/", "config-old.yml");
                if (old.exists()) {
                    try {
                        old.delete();
                        Bukkit.getConsoleSender().sendMessage("[SD_SMP] Deleting already existing config-old configuration file.");
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage("[SD_SMP] Could not delete already existing config-old configuration file.");
                    }
                }
                if (file.renameTo(old)) {
                    Bukkit.getConsoleSender().sendMessage("[SD_SMP] Successfully renamed old configuration file to \"config-old.yml\"");
                    generateConfig();
                    Bukkit.getConsoleSender().sendMessage("[SD_SMP] Please import your settings into the new config file");
                } else Bukkit.getConsoleSender().sendMessage("[SD_SMP] Error renaming old configuration file");
            }
        }
        reload();
    }

    private void generateConfig() {
        getInstance().saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(file);  // Load config file into variable
        config.set("config-version", configVersion);  // Set config version number based off plugin version
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Could not save to config file");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        configVersion = config.getDouble("config-version");

        for (String key : Objects.requireNonNull(config.getConfigurationSection("MoTD")).getKeys(false))
            MoTD.add(config.getString("MoTD." + key));
        for (String key : Objects.requireNonNull(config.getConfigurationSection("welcome")).getKeys(false))
            welcome.add(config.getString("welcome." + key));

        customJoinMessages = config.getBoolean("enable-custom-join-messages");
        customQuitMessages = config.getBoolean("enable-custom-quit-messages");
        customSleepMessages = config.getBoolean("enable-custom-sleep-messages");
        lightningOnPlayerKill = config.getBoolean("lightning_on_player_kill");
        disableSmithingTableEnabled = config.getBoolean("netherite.disable_smithing_table.enabled");
        disableSmithingTableSendMsg = config.getBoolean("netherite.disable_smithing_table.send_message");
        disableSmithingTableMessage = config.getString("netherite.disable_smithing_table.message");
        preventCraftingToolsEnabled = config.getBoolean("netherite.prevent_crafting_tools.enabled");
        preventCraftingToolsSendMsg = config.getBoolean("netherite.prevent_crafting_tools.send_message");
        preventCraftingToolsMessage = config.getString("netherite.prevent_crafting_tools.message");
        preventCraftingArmourEnabled = config.getBoolean("netherite.prevent_crafting_armour.enabled");
        preventCraftingArmourSendMsg = config.getBoolean("netherite.prevent_crafting_armour.send_message");
        preventCraftingArmourMessage = config.getString("netherite.prevent_crafting_armour.message");

        joinMessages = config.getStringList("join-messages");
        quitMessages = config.getStringList("quit-messages");
        sleepMessages = config.getStringList("sleep-messages");
        describeKill = config.getStringList("describe-kill");
        describeDeath = config.getStringList("describe-death");
    }
}
