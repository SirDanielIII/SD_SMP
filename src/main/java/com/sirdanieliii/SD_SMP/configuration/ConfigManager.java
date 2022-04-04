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

public class ConfigManager {
    protected File file;
    protected FileConfiguration config;
    // Set Variables
    public Double configVersion = Double.parseDouble(getInstance().getDescription().getVersion());
    public String name = "";
    public List<String> MoTD = new ArrayList<>();
    public List<String> welcome = new ArrayList<>();
    public boolean customJoinMessages = true;
    public List<String> joinMessages = new ArrayList<>();
    public boolean customQuitMessages = true;
    public List<String> quitMessages = new ArrayList<>();
    public boolean customSleepMessages = true;
    public List<String> sleepMessages = new ArrayList<>();
    public List<String> describeKill = new ArrayList<>();
    public List<String> describeDeath = new ArrayList<>();
    // Create list of headers
    private final String[] headers = {"config-version", "name", "MoTD", "welcome", "enable-custom-join-messages", "join-messages",
            "enable-custom-quit-messages", "quit-messages", "enable-custom-sleep-messages", "sleep-messages", "describe-kill", "describe-death"};

    public void setup() {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(
                getInstance().getName())).getDataFolder() + "/", "config.yml");
        // Create file if it doesn't exist
        config = YamlConfiguration.loadConfiguration(newFile(file));
        save();
        // Check Config Versions
        if (!Objects.equals(config.getDouble("config-version"), configVersion) && config.getDouble("config-version") != 0) {
            Bukkit.getConsoleSender().sendMessage("[SD_SMP] Older configuration file detected.");
            File old = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin(
                    getInstance().getName())).getDataFolder() + "/", "config-old.yml");
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
                config = YamlConfiguration.loadConfiguration(newFile(file));
                Bukkit.getConsoleSender().sendMessage("[SD_SMP] Please import your settings into the new config file");
            } else Bukkit.getConsoleSender().sendMessage("[SD_SMP] Error renaming old configuration file");
        }

        String[] join_msg =
                {
                        "didn't qualify for World Cup",
                        "needs to get gooder",
                        "always sings on-key",
                        "is awesome",
                        "is awesomesauce",
                        "is a silly little goose",
                        "is not deserving of an insult",
                        "doesn't wear socks",
                        "is big brain",
                        "doesn't even life",
                        "is a jelly donut",
                        "can't breathe underwater",
                        "can't tie their own shoes",
                        "has nothing better to do right now",
                        "is a chad",
                };

        String[] quit_msg =
                {
                        "folded",
                        "went to go get a girlfriend",
                        "went to go get a snack",
                        "wanted to touch grass",
                        "went to find their dad at the grocery store",
                        "rage quit",
                        "has a date with Shrek",
                        "got cancelled",
                        "went to go play Fortnite",
                        "opened Premiere Pro",
                        "opened After Effects",
                        "was cringe and left",
                        "didn't want to play anymore",
                        "left the game",
                        "got a life",
                        "was kidnapped by Monika",
                        "§KLISD FH DFZIO UGHDRIO",
                        "dipped",
                        "has to do their homework",
                        "had to log off because their mom said so",
                        "yeeted themself",
                        "says bye",
                        "got tired of the people here",
                        "left to ponder their life decisions",
                        "left the stove on"
                };

        String[] sleep_msg =
                {
                        "fell asleep",
                        "dozed off dreaming",
                        "crashed like Sir Daniel III's PC",
                        "went AWOL",
                        "committed sleep",
                        "initiated hibernation",
                        "went to dream world",
                        "started snoozing like a chad",
                        "snores loudly",
                        "started to sleep",
                        "died of exhaustion",
                        "collapsed",
                        "started napping",
                        "started dreaming",
                        "is having a nightmare",
                        "has gone out like a lamp",
                        "wandered off to bed"};

        String[] kill_msg =
                {
                        "brutally murdered",
                        "slaughtered",
                        "clapped",
                        "massacred",
                        "slayed",
                        "dookie'ed on",
                        "360 OOGA BOOGA BOOGA'ED",
                        "killed"};

        String[] death_msg =
                {
                        "your own stupidity",
                        "lack of skill",
                        "incompetence",
                        "not having any earnings"};

        for (String header : headers) {
            if (!config.contains(header)) {
                config.createSection(header);
                // Set Default Values
                switch (header) {
                    case ("config-version") -> config.set("config-version", configVersion);
                    case ("name") -> config.set("name", "SMP");
                    case ("MoTD") -> {
                        config.set("MoTD.line_1", "A Minecraft SMP");
                        config.set("MoTD.line_2", "");
                    }
                    case ("welcome") -> {
                        config.set("welcome.title", "Hello There");
                        config.set("welcome.subtitle", "§6Welcome to the SMP");
                    }
                    case ("enable-custom-join-messages") -> config.set("enable-custom-join-messages", true);
                    case ("join-messages") -> config.set("join-messages", join_msg);
                    case ("enable-custom-quit-messages") -> config.set("enable-custom-quit-messages", true);
                    case ("quit-messages") -> config.set("quit-messages", quit_msg);
                    case ("enable-custom-sleep-messages") -> config.set("enable-custom-sleep-messages", true);
                    case ("sleep-messages") -> config.set("sleep-messages", sleep_msg);
                    case ("describe-kill") -> config.set("describe-kill", kill_msg);
                    case ("describe-death") -> config.set("describe-death", death_msg);
                }
            }
        }
        save();
        reload();
    }

    public File newFile(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("[SD_SMP] Did not detect a config file. Generating a new one");
            }
        }
        return f;
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
        joinMessages = config.getStringList("join-messages");
        customQuitMessages = config.getBoolean("enable-custom-quit-messages");
        quitMessages = config.getStringList("quit-messages");
        customSleepMessages = config.getBoolean("enable-custom-sleep-messages");
        sleepMessages = config.getStringList("quit-messages");
        describeKill = config.getStringList("describe-kill");
        describeDeath = config.getStringList("describe-death");
    }
}
