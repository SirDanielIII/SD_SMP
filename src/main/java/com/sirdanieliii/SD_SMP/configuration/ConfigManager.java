package com.sirdanieliii.SD_SMP.configuration;


import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static com.sirdanieliii.SD_SMP.SD_SMP.getInstance;

public class ConfigManager {
    protected YamlDocument config;
    protected YamlDocument configErrorMsg;
    // Set Variables
    public Double configVersion = Double.parseDouble(getInstance().getDescription().getVersion());
    public static String smpName;
    public static String cmdHeader;
    public static List<String> MoTD = new ArrayList<>();
    public static List<String> welcome = new ArrayList<>();
    public static boolean customJoinMessages;
    public static boolean customQuitMessages;
    public static boolean customSleepMessages;
    public static boolean lightningOnPlayerKill;
    public static boolean disableSmithingTableEnabled;
    public static boolean disableSmithingTableSendMsg;
    public static boolean preventCraftingToolsEnabled;
    public static boolean preventCraftingToolsSendMsg;
    public static boolean preventCraftingArmourEnabled;
    public static boolean preventCraftingArmourSendMsg;
    public static boolean elytraFlightOverworld;
    public static boolean elytraFlightOverworldSendMsg;
    public static boolean elytraFlightNether;
    public static boolean elytraFlightNetherSendMsg;
    public static boolean elytraFlightTheEnd;
    public static boolean elytraFlightTheEndSendMsg;

    public static List<String> joinMessages = new ArrayList<>();
    public static List<String> quitMessages = new ArrayList<>();
    public static List<String> sleepMessages = new ArrayList<>();
    public static List<String> describeKill = new ArrayList<>();
    public static List<String> describeDeath = new ArrayList<>();
    public static String errorHeader;
    public static String errorClr;
    public static HashMap<String, String> errorMessages = new HashMap<>();

    public void setup() {
        try {
            // Main Config File
            config = YamlDocument.create(new File(getInstance().getDataFolder(), "config.yml"),
                    Objects.requireNonNull(getInstance().getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
            config.update();
            // Configurable Error Messages File
            configErrorMsg = YamlDocument.create(new File(getInstance().getDataFolder(), "error_messages.yml"),
                    Objects.requireNonNull(getInstance().getResource("error_messages.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
            configErrorMsg.update();
            reload();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void playerSetup(Player player) {
        YamlDocument conf;
        try {
            conf = YamlDocument.create(new File(getInstance().getDataFolder(), "/playerdata/" + player.getUniqueId() + ".yml"),
                    Objects.requireNonNull(getInstance().getResource("default_player_config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
            conf.update();
            Bukkit.getLogger().log(Level.CONFIG, cmdHeader + " Successfully generated config for " + player.getDisplayName());

            if (!conf.getString("name").equalsIgnoreCase(player.getDisplayName())) {
                conf.set("name", player.getDisplayName());  // Update player name if changed
                Bukkit.getLogger().log(Level.CONFIG, cmdHeader + " Updated config name for " + player.getDisplayName());
                conf.save();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static YamlDocument getPlayerConfig(Player player) {
        try {
            YamlDocument conf = YamlDocument.create(new File(getInstance().getDataFolder(), "/playerdata/" + player.getUniqueId() + ".yml"));
            conf.update();
            return conf;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePlayerConfig(YamlDocument conf) {
        try {
            conf.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteConfigKey(YamlDocument config, String path) {
        FileConfiguration conf = YamlConfiguration.loadConfiguration(Objects.requireNonNull(config.getFile()));
        conf.set(path, null);
        try {
            conf.save(config.getFile());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    public void reload() {
        try {
            config.reload();
            config.update();
            configErrorMsg.reload();
            configErrorMsg.update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        configVersion = config.getDouble("config-version");

        for (String key : config.getSection("motd").getRoutesAsStrings(false)) MoTD.add(config.getString("motd." + key));
        for (String key : config.getSection("welcome").getRoutesAsStrings(false)) welcome.add(config.getString("welcome." + key));

        smpName = config.getString("name");
        cmdHeader = config.getString("cmd_header");
        customJoinMessages = config.getBoolean("enable-custom-join-messages");
        customQuitMessages = config.getBoolean("enable-custom-quit-messages");
        customSleepMessages = config.getBoolean("enable-custom-sleep-messages");
        lightningOnPlayerKill = config.getBoolean("lightning_on_player_kill");
        disableSmithingTableEnabled = config.getBoolean("netherite.disable_smithing_table.enabled");
        disableSmithingTableSendMsg = config.getBoolean("netherite.disable_smithing_table.send_notice_to_player");
        preventCraftingToolsEnabled = config.getBoolean("netherite.prevent_crafting_tools.enabled");
        preventCraftingToolsSendMsg = config.getBoolean("netherite.prevent_crafting_tools.send_notice_to_player");
        preventCraftingArmourEnabled = config.getBoolean("netherite.prevent_crafting_armour.enabled");
        preventCraftingArmourSendMsg = config.getBoolean("netherite.prevent_crafting_armour.send_notice_to_player");
        elytraFlightOverworld = config.getBoolean("disable_elytra_flight.overworld.enabled");
        elytraFlightOverworldSendMsg = config.getBoolean("disable_elytra_flight.overworld.send_notice_to_player");
        elytraFlightNether = config.getBoolean("disable_elytra_flight.nether.enabled");
        elytraFlightNetherSendMsg = config.getBoolean("disable_elytra_flight.nether.send_notice_to_player");
        elytraFlightTheEnd = config.getBoolean("disable_elytra_flight.the_end.enabled");
        elytraFlightTheEndSendMsg = config.getBoolean("disable_elytra_flight.the_end.send_notice_to_player");

        joinMessages = config.getStringList("join_messages");
        quitMessages = config.getStringList("quit_messages");
        sleepMessages = config.getStringList("sleep_messages");
        describeKill = config.getStringList("describe_kill");
        describeDeath = config.getStringList("describe_death");

        errorHeader = configErrorMsg.getString("error_header");
        errorClr = configErrorMsg.getString("error_clr");
        for (String key : configErrorMsg.getRoutesAsStrings(false)) errorMessages.put(key, configErrorMsg.getString(key));
    }

    public static String errorMessage(String error) {
        return errorHeader + errorClr + " " + errorMessages.get(error);
    }
}
