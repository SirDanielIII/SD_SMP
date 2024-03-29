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
import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;

public class ConfigManager {
    protected static YamlDocument config;
    protected static YamlDocument errorMsgs;
    protected static YamlDocument messages;
    // Create Variables
    public static String smpName;
    public static String cmdHeader;
    public static List<String> motd = new ArrayList<>();
    public static boolean motd_enable;
    public static List<String> welcome = new ArrayList<>();
    public static boolean welcome_enable;
    public static int welcome_fade_in;
    public static int welcome_stay;
    public static int welcome_fade_out;
    public static boolean customJoinMessages;
    public static boolean customJoinMessagesSmileyFace;
    public static String customJoinMessagesClr;
    public static boolean customQuitMessages;
    public static String customQuitMessagesClr;
    public static boolean customSleepMessages;
    public static String customSleepMessagesClr;
    public static boolean lightningOnPlayerKill;
    public static boolean disableNetherPortal;
    public static boolean disableEndPortal;
    public static boolean disableMiningNetherite;
    public static boolean disableSmithingTable;
    public static boolean craftNetheriteTools;
    public static boolean craftNetheriteArmour;
    public static boolean elytraFlightOverworld;
    public static boolean elytraFlightNether;
    public static boolean elytraFlightTheEnd;
    public static boolean healthUnderName;
    public static String healthUnderNameText;
    public static int healthUnderNameUpdate;

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
            config = YamlDocument.create(new File(getInstance().getDataFolder(), "config.yml"), Objects.requireNonNull(getInstance()
                            .getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder()
                            .setDetailedErrors(true)
                            .setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT, UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("config-version"))
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS)
                            .build());
            config.update();
            // Configurable Messages Files
            errorMsgs = YamlDocument.create(new File(getInstance().getDataFolder(), "error_messages.yml"), Objects.requireNonNull(getInstance()
                            .getResource("error_messages.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder()
                            .setDetailedErrors(true)
                            .setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("config-version"))
                            .build());
            errorMsgs.update();
            messages = YamlDocument.create(new File(getInstance().getDataFolder(), "/messages.yml"), Objects.requireNonNull(getInstance()
                            .getResource("messages.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder()
                            .setDetailedErrors(true)
                            .setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("config-version"))
                            .build());
            messages.update();
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
            if (!conf.getString("uuid").equalsIgnoreCase(String.valueOf(player.getUniqueId()))) {
                conf.set("uuid", String.valueOf(player.getUniqueId()));  // Update player name if changed
                conf.save();
            }
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

    public static void reload() {
        try {
            config.reload();
            config.update();
            errorMsgs.reload();
            errorMsgs.update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String key : config.getSection("motd").getRoutesAsStrings(false)) if (!key.equals("enabled")) motd.add(config.getString("motd." + key));
        motd_enable = config.getBoolean("motd.enabled");

        welcome.add(config.getString("welcome.title"));
        welcome.add(config.getString("welcome.subtitle"));
        welcome_enable = config.getBoolean("welcome.enabled");
        welcome_fade_in = config.getInt("welcome.fade_in");
        welcome_stay = config.getInt("welcome.stay");
        welcome_fade_out = config.getInt("welcome.fade_out");

        smpName = config.getString("name");
        cmdHeader = config.getString("cmd_header");

        customJoinMessages = config.getBoolean("custom-join-messages-enable");
        customJoinMessagesClr = config.getString("custom-join-messages-clr");
        customJoinMessagesSmileyFace = config.getBoolean("custom-join-messages-smiley-face");
        customQuitMessages = config.getBoolean("custom-quit-messages-enable");
        customQuitMessagesClr = config.getString("custom-quit-messages-clr");
        customSleepMessages = config.getBoolean("custom-sleep-messages-enable");
        customSleepMessagesClr = config.getString("custom-sleep-messages-clr");

        lightningOnPlayerKill = config.getBoolean("lightning_on_player_kill");

        disableNetherPortal = config.getBoolean("disable_nether_portal");
        disableEndPortal = config.getBoolean("disable_end_portal");
        disableMiningNetherite = config.getBoolean("disable_end_portal");
        disableSmithingTable = config.getBoolean("disable_mining_netherite");

        craftNetheriteTools = config.getBoolean("disable_crafting_netherite_tools");
        craftNetheriteArmour = config.getBoolean("disable_crafting_netherite_armour");

        elytraFlightOverworld = config.getBoolean("elytra_flight.overworld");
        elytraFlightNether = config.getBoolean("elytra_flight.nether");
        elytraFlightTheEnd = config.getBoolean("elytra_flight.the_end");

        healthUnderName = config.getBoolean("health_under_name.enabled");
        healthUnderNameText = config.getString("health_under_name.text_after_health");
        healthUnderNameUpdate = config.getInt("health_under_name.text.update_interval");

        joinMessages = messages.getStringList("join_messages");
        quitMessages = messages.getStringList("quit_messages");
        sleepMessages = messages.getStringList("sleep_messages");
        describeKill = messages.getStringList("describe_kill");
        describeDeath = messages.getStringList("describe_death");

        errorHeader = errorMsgs.getString("error_header");
        errorClr = errorMsgs.getString("error_clr");
        for (String key : errorMsgs.getRoutesAsStrings(false)) errorMessages.put(key, errorMsgs.getString(key));
    }

    public static String errorMessage(String error) {
        return translateColourCodes(errorHeader + errorClr + " " + errorMessages.get(error));
    }
}
