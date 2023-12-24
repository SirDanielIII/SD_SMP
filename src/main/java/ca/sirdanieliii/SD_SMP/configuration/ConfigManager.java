package ca.sirdanieliii.SD_SMP.configuration;

import ca.sirdanieliii.SD_SMP.SD_SMP;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigErrors;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigMain;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigMessages;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;

public class ConfigManager {
    public static final String blockFooter = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
    public static String pluginName;
    public static String cmdHeader;
    public static String[] motd = new String[2];
    public static boolean motdEnable;
    public static List<String> welcome = new ArrayList<>();
    public static boolean welcome_enable;
    public static int welcome_fade_in;
    public static int welcome_stay;
    public static int welcome_fade_out;
    public static int linesPerPaginatedChat;
    public static boolean signColourCodeSupport;
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
    public static HashMap<String, String> generalMsgs = new HashMap<>();

    public static void setupPlayerConfig(Player player) {
        ConfigPlayer conf = new ConfigPlayer("/playerdata/", player.getUniqueId().toString(), "", "default_player_config");
        if (!conf.update(player)) {
            SD_SMP.getThisPlugin().getLogger().info(String.format("Unable to update %s", conf.getFile().getName()));
        } else {
            conf.reload();
            conf.updateName(player);
            SD_SMP.getThisPlugin().getLogger().info(String.format("Config for %s has been loaded!", player.getDisplayName()));
        }
    }

    public static void reloadConfigs() {
        ConfigMain config = new ConfigMain("", "config", "", "config");
        ConfigErrors errorMsgs = new ConfigErrors("", "error_messages", "", "error_messages");
        ConfigMessages customMsgs = new ConfigMessages("", "messages", "", "messages");
        config.reload();
        errorMsgs.reload();
        customMsgs.reload();
        if (!config.update()) SD_SMP.getThisPlugin().getLogger().severe(String.format("Unable to update %s! Problems may occur", config.getFile().getName()));
        if (!errorMsgs.update()) SD_SMP.getThisPlugin().getLogger().severe(String.format("Unable to update %s! Problems may occur", errorMsgs.getFile().getName()));
        if (!customMsgs.update()) SD_SMP.getThisPlugin().getLogger().severe(String.format("Unable to update %s! Problems may occur", customMsgs.getFile().getName()));

        for (Player p : Bukkit.getOnlinePlayers()) setupPlayerConfig(p);

        motd[0] = config.getConfig().getString("motd.line_1");
        motd[1] = config.getConfig().getString("motd.line_2");
        motdEnable = config.getConfig().getBoolean("motd.enabled");

        welcome.add(config.getConfig().getString("welcome.title"));
        welcome.add(config.getConfig().getString("welcome.subtitle"));
        welcome_enable = config.getConfig().getBoolean("welcome.enabled");
        welcome_fade_in = config.getConfig().getInt("welcome.fade_in");
        welcome_stay = config.getConfig().getInt("welcome.stay");
        welcome_fade_out = config.getConfig().getInt("welcome.fade_out");

        linesPerPaginatedChat = config.getConfig().getInt("lines_per_paginated_chat");

        signColourCodeSupport = config.getConfig().getBoolean("sign_colour_codes");

        pluginName = config.getConfig().getString("name");
        cmdHeader = config.getConfig().getString("cmd_header");

        customJoinMessages = config.getConfig().getBoolean("custom-join-messages-enable");
        customJoinMessagesClr = config.getConfig().getString("custom-join-messages-clr");
        customJoinMessagesSmileyFace = config.getConfig().getBoolean("custom-join-messages-smiley-face");
        customQuitMessages = config.getConfig().getBoolean("custom-quit-messages-enable");
        customQuitMessagesClr = config.getConfig().getString("custom-quit-messages-clr");
        customSleepMessages = config.getConfig().getBoolean("custom-sleep-messages-enable");
        customSleepMessagesClr = config.getConfig().getString("custom-sleep-messages-clr");

        lightningOnPlayerKill = config.getConfig().getBoolean("lightning_on_player_kill");

        disableNetherPortal = config.getConfig().getBoolean("disable_nether_portal");
        disableEndPortal = config.getConfig().getBoolean("disable_end_portal");
        disableMiningNetherite = config.getConfig().getBoolean("disable_mining_netherite");
        disableSmithingTable = config.getConfig().getBoolean("disable_smithing_table");

        craftNetheriteTools = config.getConfig().getBoolean("disable_crafting_netherite_tools");
        craftNetheriteArmour = config.getConfig().getBoolean("disable_crafting_netherite_armour");

        elytraFlightOverworld = config.getConfig().getBoolean("elytra_flight.overworld");
        elytraFlightNether = config.getConfig().getBoolean("elytra_flight.nether");
        elytraFlightTheEnd = config.getConfig().getBoolean("elytra_flight.the_end");

        healthUnderName = config.getConfig().getBoolean("health_under_name.enabled");
        healthUnderNameText = config.getConfig().getString("health_under_name.text_after_health");
        healthUnderNameUpdate = config.getConfig().getInt("health_under_name.text.update_interval");

        joinMessages = customMsgs.getConfig().getStringList("join_messages");
        quitMessages = customMsgs.getConfig().getStringList("quit_messages");
        sleepMessages = customMsgs.getConfig().getStringList("sleep_messages");
        describeKill = customMsgs.getConfig().getStringList("describe_kill");
        describeDeath = customMsgs.getConfig().getStringList("describe_death");

        errorHeader = errorMsgs.getConfig().getString("error_header");
        errorClr = errorMsgs.getConfig().getString("error_clr");
        for (String key : errorMsgs.getConfig().getKeys(false)) {
            generalMsgs.put(key, errorMsgs.getConfig().getString(key));
        }
    }

    public static String errorMessage(String error) {
        return translateMsgClr(errorHeader + errorClr + " " + generalMsgs.get(error));
    }
}
